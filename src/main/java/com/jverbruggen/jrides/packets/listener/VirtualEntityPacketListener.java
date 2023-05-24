package com.jverbruggen.jrides.packets.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimation;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileFields;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterSeat;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VirtualEntityPacketListener extends PacketAdapter implements Listener {
    private final ViewportManager viewportManager;
    private final PlayerManager playerManager;
    private final SmoothAnimation smoothAnimation;
    private final List<UUID> shiftPressedDebounce;
    private final boolean canExitDuringRide;
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
        this.canExitDuringRide = true;
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
        if(!(entity instanceof VirtualArmorstand)){
            return;
        }
        if (!entity.allowsPassenger()) {
            return;
        }
        if (bukkitPlayer.getLocation().toVector().distanceSquared(entity.getLocation().toBukkitVector()) > 49) {
            return;
        }

        VirtualArmorstand virtualArmorstand = (VirtualArmorstand) entity;
        Seat seat = virtualArmorstand.getHostSeat();
        Player player = playerManager.getPlayer(bukkitPlayer);

        if(player.isSeated()) return;

        if(seat.restraintsActive()){
            if(!player.getBukkitPlayer().hasPermission(Permissions.SEAT_RESTRAINT_OVERRIDE)){
                languageFile.sendMessage(player, LanguageFileFields.NOTIFICATION_RESTRAINT_ON_ENTER_ATTEMPT);
                return;
            }
            languageFile.sendMessage(player, LanguageFileFields.NOTIFICATION_RESTRAINT_ENTER_OVERRIDE);
        }

        player.setSmoothAnimationSupport(smoothAnimation.isEnabled(player));
        seat.setPassenger(player);
    }

    private void onSteerVehicle(PacketEvent event) {
        boolean dismountVehicle = event.getPacket().getBooleans().read(1);
        if(!dismountVehicle){
            if(shiftPressedDebounce.size() > 0) shiftPressedDebounce.remove(event.getPlayer().getUniqueId());
            return;
        }

        org.bukkit.entity.Player bukkitPlayer = event.getPlayer();
        Player player = playerManager.getPlayer(bukkitPlayer);
        Seat seat = player.getSeatedOn();
        if(seat == null){
            return;
        }

        VirtualEntity entity = seat.getEntity();

        if(!entity.getPassenger().getBukkitPlayer().getUniqueId().equals(bukkitPlayer.getUniqueId())){
            return; // Can only steer vehicle that one is in
        }

        UUID uuid = bukkitPlayer.getUniqueId();
        if(shiftPressedDebounce.contains(uuid)) return;
        shiftPressedDebounce.add(uuid);

        double teleportYaw = player.getBukkitPlayer().getLocation().getYaw();

        if(seat.restraintsActive()){
            if(bukkitPlayer.hasPermission(Permissions.SEAT_RESTRAINT_OVERRIDE)){
                Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(), () -> {
                    boolean ejected = seat.ejectPassengerSoft(false);
                    if(ejected){
                        languageFile.sendMessage(player, LanguageFileFields.NOTIFICATION_SHIFT_EXIT_CONFIRMED);
                        player.teleport(Vector3.add(entity.getLocation(), CoasterSeat.getHeightCompensation()), teleportYaw);
                    }
                });
                return;
            }else if(canExitDuringRide){
                Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(), () -> seat.ejectPassengerSoft(true));
                return;
            }

            languageFile.sendMessage(player, LanguageFileFields.NOTIFICATION_RESTRAINT_ON_EXIT_ATTEMPT);
            return;
        }

        Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(), () -> {
            seat.setPassenger(null);
            player.teleport(Vector3.add(entity.getLocation(), CoasterSeat.getHeightCompensation()), teleportYaw);
        });
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
