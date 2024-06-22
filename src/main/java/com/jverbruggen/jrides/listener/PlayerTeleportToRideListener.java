package com.jverbruggen.jrides.listener;

import com.jverbruggen.jrides.api.JRidesPlayerLocation;
import com.jverbruggen.jrides.event.player.PlayerTeleportByJRidesEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerTeleportToRideListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleportToRide(PlayerTeleportByJRidesEvent e){
        if(!e.isCancelled() || e.isHardTeleport()){
            Player bukkitPlayer = e.getPlayer().getBukkitPlayer();
            JRidesPlayerLocation playerLocation = e.getLocation();
            bukkitPlayer.teleport(new Location(bukkitPlayer.getWorld(),
                    playerLocation.getX(),
                    playerLocation.getY(),
                    playerLocation.getZ(),
                    (float) playerLocation.getYaw(),
                    (float) playerLocation.getPitch()));
        }
    }
}
