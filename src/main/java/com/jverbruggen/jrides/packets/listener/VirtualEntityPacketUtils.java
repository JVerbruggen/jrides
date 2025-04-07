/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.packets.listener;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimation;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.SeatedOnContext;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.seat.InstructionType;
import com.jverbruggen.jrides.models.ride.seat.Seat;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VirtualEntityPacketUtils {
    private final LanguageFile languageFile;
    private final SmoothAnimation smoothAnimation;
    private final List<UUID> shiftPressedDebounce;
    private final PacketSender packetSender;

    public VirtualEntityPacketUtils(){
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.smoothAnimation = ServiceProvider.getSingleton(SmoothAnimation.class);
        this.shiftPressedDebounce = new ArrayList<>();
        this.packetSender = ServiceProvider.getSingleton(PacketSender.class);
    }

    public void onUseEntity(VirtualEntity entity, Player player){
        if (entity.hasCustomAction()){
            entity.runCustomAction(player);
        }
        if (!entity.allowsPassenger()) {
            return;
        }
        if (player.getLocation().toBukkitVector().distanceSquared(entity.getLocation().toBukkitVector()) > 49) {
            return;
        }

        Seat seat = entity.getHostSeat();

        if(seat == null) return;
        if(player.isSeated()) return;

        if(!seat.getParentRideHandle().isOpen()
                && !player.hasPermission(Permissions.ELEVATED_RIDE_CLOSED_ENTER_OVERRIDE)){
            languageFile.sendMessage(player, LanguageFileField.NOTIFICATION_CANNOT_ENTER_RIDE_CLOSED);
            return;
        }

        if(seat.restraintsActive()){
            if(!player.getBukkitPlayer().hasPermission(Permissions.ELEVATED_RESTRAINT_OVERRIDE)){
                languageFile.sendMessage(player, LanguageFileField.NOTIFICATION_RESTRAINT_ON_ENTER_ATTEMPT);
                return;
            }
            languageFile.sendMessage(player, LanguageFileField.NOTIFICATION_RESTRAINT_ENTER_OVERRIDE);
        }

        player.setSmoothAnimationSupport(smoothAnimation.isEnabled(player));
        seat.setPassenger(player);
    }

    public void processPlayerControl(float sidewaysLeft, float forwards, boolean jump, Seat seat){
        InstructionType type;
        if(sidewaysLeft > 0){
            type = InstructionType.A;
        }else if(sidewaysLeft < 0){
            type = InstructionType.D;
        }else if(forwards > 0){
            type = InstructionType.W;
        }else if(forwards < 0){
            type = InstructionType.S;
        }else if(jump){
            type = InstructionType.SPACE;
        }else{
            type = InstructionType.NONE;
        }

        seat.sendPlayerControlInstruction(type);
    }

    public void onSteerVehicle(Player player, float sidewaysLeft, float forwards, boolean jump, boolean dismountVehicle){
        org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();
        SeatedOnContext seatedOnContext = player.getSeatedOnContext();
        if(seatedOnContext == null) return;

        Seat seat = seatedOnContext.getSeat();

        processPlayerControl(sidewaysLeft, forwards, jump, seat);

        if(!dismountVehicle){
            removeShiftDebounce(bukkitPlayer);
            return;
        }

        if(seat == null){
            return;
        }

        VirtualEntity entity = seat.getEntity();

        if(entity.getPassenger() == null) return;
        if(!entity.getPassenger().getBukkitPlayer().getUniqueId().equals(bukkitPlayer.getUniqueId())){
            return; // Can only steer vehicle that one is in
        }

        UUID uuid = bukkitPlayer.getUniqueId();
        if(shiftPressedDebounce.contains(uuid)) return;
        shiftPressedDebounce.add(uuid);

        double teleportYaw = player.getBukkitPlayer().getLocation().getYaw();
        Ride ride = seat.getParentRideHandle().getRide();

        if(seat.restraintsActive()){
            if(bukkitPlayer.hasPermission(Permissions.ELEVATED_RESTRAINT_OVERRIDE)){
                Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(), () -> {
                    boolean ejected = seat.ejectPassengerSoft(false);
                    if(ejected){
                        languageFile.sendMessage(player, LanguageFileField.NOTIFICATION_SHIFT_EXIT_CONFIRMED);
                        player.teleport(entity.getLocation(), teleportYaw);
                    }
                });
                return;
            }else if(ride.canExitDuringRide()){
                Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(), () -> seat.ejectPassengerSoft(true));
                return;
            }

            languageFile.sendMessage(player, LanguageFileField.NOTIFICATION_RESTRAINT_ON_EXIT_ATTEMPT);
            return;
        }

        Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(), () -> {
            seat.setPassenger(null);
            player.teleport(entity.getLocation(), teleportYaw);
        });
    }

    private void removeShiftDebounce(org.bukkit.entity.Player player){
        if(!shiftPressedDebounce.isEmpty()) shiftPressedDebounce.remove(player.getUniqueId());
    }

    public void onSendingSpawnPlayerPacket(Player spawnedPlayer, Player receiver) {
        if(!spawnedPlayer.isSeated()) return;

        Bukkit.getScheduler().runTaskLater(JRidesPlugin.getBukkitPlugin(),
                () -> packetSender.sendMountVirtualEntityPacket(List.of(receiver), spawnedPlayer, spawnedPlayer.getSeatedOn().getEntity().getEntityId()), 10L);
    }
}
