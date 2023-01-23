package com.jverbruggen.jrides.packets.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class VirtualEntityPacketListener extends PacketAdapter implements Listener {
    private final ViewportManager viewportManager;
    private final PlayerManager playerManager;

    public VirtualEntityPacketListener(Plugin plugin, ListenerPriority listenerPriority, PacketType[] types,
                                       ViewportManager viewportManager, PlayerManager playerManager) {
        super(plugin, listenerPriority, types);
        this.viewportManager = viewportManager;
        this.playerManager = playerManager;
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
            bukkitPlayer.sendMessage("Not found");
            return;
        }

        if (!entity.allowsPassenger()) {
            bukkitPlayer.sendMessage("Cannot sit");
            return;
        }

        if (bukkitPlayer.getLocation().toVector().distanceSquared(entity.getLocation().toBukkitVector()) > 49) {
            bukkitPlayer.sendMessage(ChatColor.DARK_RED + "Stand closer to the vehicle to enter");
            return;
        }

        Seat seat = entity.getHostSeat();
        Player player = playerManager.getPlayer(bukkitPlayer);
        seat.setPassenger(player);

//        bukkitPlayer.sendMessage("Entered");
    }

    private void onSteerVehicle(PacketEvent event) {
        boolean dismountVehicle = event.getPacket().getBooleans().read(1);
        if(!dismountVehicle) return;

        org.bukkit.entity.Player bukkitPlayer = event.getPlayer();
        Player player = playerManager.getPlayer(bukkitPlayer);
        Seat seat = player.getSeatedOn();
        if(seat == null){
            bukkitPlayer.sendMessage("No seat registered");
            return;
        }

        VirtualEntity entity = seat.getEntity();

        if(!entity.getPassenger().getBukkitPlayer().getUniqueId().equals(bukkitPlayer.getUniqueId())){
            bukkitPlayer.sendMessage("Not allowed to steer");
            return; // Can only steer vehicle that one is in
        }

//        bukkitPlayer.sendMessage("Exited");
        seat.setPassenger(null);
    }
}
