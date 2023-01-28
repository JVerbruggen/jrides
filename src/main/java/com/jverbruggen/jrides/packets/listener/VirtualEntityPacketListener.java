package com.jverbruggen.jrides.packets.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimation;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.permissions.Permissions;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VirtualEntityPacketListener extends PacketAdapter implements Listener {
    private final ViewportManager viewportManager;
    private final PlayerManager playerManager;
    private final SmoothAnimation smoothAnimation;
    private final List<UUID> shiftPressedDebounce;
    private final boolean canExitDuringRide;

    public VirtualEntityPacketListener() {
        super(JRidesPlugin.getBukkitPlugin(), ListenerPriority.NORMAL,
                PacketType.Play.Client.USE_ENTITY,
                PacketType.Play.Client.STEER_VEHICLE);
        this.viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        this.smoothAnimation = ServiceProvider.getSingleton(SmoothAnimation.class);
        this.shiftPressedDebounce = new ArrayList<>();
        this.canExitDuringRide = true;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketType packetType = event.getPacketType();
        if (packetType.equals(PacketType.Play.Client.USE_ENTITY)) {
            onUseEntity(event);
        } else if(packetType.equals(PacketType.Play.Client.STEER_VEHICLE)) {
            onSteerVehicle(event);
        } else {
            org.bukkit.entity.Player bukkitPlayer = event.getPlayer();
            if (bukkitPlayer != null) bukkitPlayer.sendMessage("Not implemented yet");
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
            bukkitPlayer.sendMessage(ChatColor.DARK_RED + "Stand closer to the vehicle to enter");
            return;
        }

        VirtualArmorstand virtualArmorstand = (VirtualArmorstand) entity;
        Seat seat = virtualArmorstand.getHostSeat();
        Player player = playerManager.getPlayer(bukkitPlayer);

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
            bukkitPlayer.sendMessage("Not allowed to steer");
            return; // Can only steer vehicle that one is in
        }

        UUID uuid = bukkitPlayer.getUniqueId();
        if(shiftPressedDebounce.contains(uuid)) return;
        shiftPressedDebounce.add(uuid);

        if(seat.restraintsActive()){
            if(bukkitPlayer.hasPermission(Permissions.SEAT_RESTRAINT_OVERRIDE)){
                boolean ejected = seat.ejectPassengerSoft();

                if(ejected) bukkitPlayer.sendMessage(ChatColor.GRAY + "You just exited the ride while the restraints were closed");
                return;
            }else if(canExitDuringRide){
                seat.ejectPassengerSoft();
                return;
            }

            bukkitPlayer.sendMessage("The restraints are closed");
            return;
        }
        seat.setPassenger(null);
    }
}
