package com.jverbruggen.jrides.models.entity.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PassengerListener implements Listener {
// TODO: alles hier

//    @EventHandler
//    public void onPlayerFly(PlayerToggleFlightEvent e) {
//        Player p = e.getPlayer();
//        if(!EntityStorage.currentlyInRideEntities.contains(e.getPlayer()))
//            return;
//        if(p.getAllowFlight()) {
//            e.setCancelled(true);
//            p.setFlying(true);
//        }
//    }
//
//    @EventHandler
//    public void onQuit(PlayerQuitEvent e) {
//        Player p = e.getPlayer();
//        if(!EntityStorage.currentlyInRideEntities.contains(e.getPlayer()))
//            return;
//        EntityStorage.currentlyInRideEntities.remove(e.getPlayer());
//        if(p.getAllowFlight()) {
//            p.setAllowFlight(false);
//            p.setFlying(false);
//        }
//        for(Ride ride : Main.rideAPI.getAllRides()) {
//            if(ride.getViewers().contains(p)) {
//                ride.removeViewer(p);
//            }
//        }

}
