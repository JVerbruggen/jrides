package com.jverbruggen.jrides.packets.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
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
        if(packetType.equals(PacketType.Play.Client.USE_ENTITY)){
            onUseEntity(event);
        }else{
            org.bukkit.entity.Player bukkitPlayer = event.getPlayer();
            if(bukkitPlayer != null) bukkitPlayer.sendMessage("Not implemented yet");
        }
    }

    private void onUseEntity(PacketEvent event){
        int entityId = event.getPacket().getIntegers().read(0);
        VirtualEntity entity = viewportManager.getEntity(entityId);
        org.bukkit.entity.Player bukkitPlayer = event.getPlayer();

        if(entity == null){
            bukkitPlayer.sendMessage("Not found");
            return;
        }

        if(!entity.allowsPassenger()){
            bukkitPlayer.sendMessage("Cannot sit");
            return;
        }

        if(bukkitPlayer.getLocation().toVector().distanceSquared(entity.getLocation().toBukkitVector()) > 49){
            bukkitPlayer.sendMessage(ChatColor.DARK_RED + "Stand closer to the vehicle to enter");
            return;
        }

        Player player = playerManager.getPlayer(bukkitPlayer);
        entity.setPassenger(player);

        bukkitPlayer.sendMessage("Entered");
    }
}
