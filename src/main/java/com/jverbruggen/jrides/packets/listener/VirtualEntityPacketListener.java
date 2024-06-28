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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimation;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.SeatedOnContext;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.seat.InstructionType;
import com.jverbruggen.jrides.models.ride.seat.Seat;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterSeat;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VirtualEntityPacketListener extends PacketAdapter implements Listener {
    private final ViewportManager viewportManager;
    private final PlayerManager playerManager;
    private final SmoothAnimation smoothAnimation;
    private final List<UUID> shiftPressedDebounce;
    private final PacketSender packetSender;
    private final LanguageFile languageFile;

    public VirtualEntityPacketListener() {
        super(JRidesPlugin.getBukkitPlugin(), ListenerPriority.NORMAL,
                PacketType.Play.Client.USE_ENTITY,
                PacketType.Play.Client.STEER_VEHICLE,
                PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        this.viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        this.smoothAnimation = ServiceProvider.getSingleton(SmoothAnimation.class);
        this.packetSender = ServiceProvider.getSingleton(PacketSender.class);
        this.shiftPressedDebounce = new ArrayList<>();
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketType packetType = event.getPacketType();
        if (packetType.equals(PacketType.Play.Client.USE_ENTITY)) {
            onUseEntity(event);
        } else if (packetType.equals(PacketType.Play.Client.STEER_VEHICLE)) {
            onSteerVehicle(event);
        }
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketType packetType = event.getPacketType();
        if(packetType.equals(PacketType.Play.Server.NAMED_ENTITY_SPAWN)){
            onSendingSpawnPlayerPacket(event);
        }
    }

    private void onUseEntity(PacketEvent event) {
        int entityId = event.getPacket().getIntegers().read(0);
        VirtualEntity entity = viewportManager.getEntity(entityId);
        org.bukkit.entity.Player bukkitPlayer = event.getPlayer();

        if (entity == null) {
            return;
        }
        Player player = playerManager.getPlayer(bukkitPlayer);
        if (entity.hasCustomAction()){
            entity.runCustomAction(player);
        }
        if (!entity.allowsPassenger()) {
            return;
        }
        if (bukkitPlayer.getLocation().toVector().distanceSquared(entity.getLocation().toBukkitVector()) > 49) {
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

    private void processPlayerControl(PacketEvent event, Seat seat){
        if(seat == null || !seat.supportsPlayerControl()){
            return;
        }

        float sidewaysLeft = event.getPacket().getFloat().read(0); // Left is positive, right is negative
        float forwards = event.getPacket().getFloat().read(1);
        boolean jump = event.getPacket().getBooleans().read(0);

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

    private void onSteerVehicle(PacketEvent event) {
        boolean dismountVehicle = event.getPacket().getBooleans().read(1);

        org.bukkit.entity.Player bukkitPlayer = event.getPlayer();
        Player player = playerManager.getPlayer(bukkitPlayer);
        SeatedOnContext seatedOnContext = player.getSeatedOnContext();
        if(seatedOnContext == null) return;

        Seat seat = seatedOnContext.getSeat();

        processPlayerControl(event, seat);

        if(!dismountVehicle){
            removeShiftDebounce(event.getPlayer());
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
        if(shiftPressedDebounce.size() > 0) shiftPressedDebounce.remove(player.getUniqueId());
    }

    private void onSendingSpawnPlayerPacket(PacketEvent event){
        UUID uuid = event.getPacket().getUUIDs().read(0);

        org.bukkit.entity.Player bukkitPlayer = Bukkit.getPlayer(uuid);
        if(bukkitPlayer == null) return;
        Player player = playerManager.getPlayer(bukkitPlayer);
        if(!player.isSeated()) return;

        Player receiver = playerManager.getPlayer(event.getPlayer());

        Bukkit.getScheduler().runTaskLater(JRidesPlugin.getBukkitPlugin(),
                () -> packetSender.sendMountVirtualEntityPacket(List.of(receiver), player, player.getSeatedOn().getEntity().getEntityId()), 1L);
    }
}
