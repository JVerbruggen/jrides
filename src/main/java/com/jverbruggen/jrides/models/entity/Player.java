package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Player {
    private org.bukkit.entity.Player bukkitPlayer;
    private Seat seatedOn;
    private boolean smoothAnimationSupport;

    public Player(org.bukkit.entity.Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.seatedOn = null;
        this.smoothAnimationSupport = false;
    }

    public Vector3 getLocation(){
        Location location = bukkitPlayer.getLocation();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return new Vector3(x, y, z);
    }

    public void sendMessage(String message){
        bukkitPlayer.sendMessage(message);
    }

    public org.bukkit.entity.Player getBukkitPlayer(){
        return bukkitPlayer;
    }

    public void setSeatedOn(Seat seat){
        this.seatedOn = seat;
    }

    public Seat getSeatedOn(){
        return this.seatedOn;
    }

    public boolean isSeated(){
        return this.seatedOn != null;
    }

    public void setPositionWithoutTeleport(Vector3 position){
        JRidesPlugin.getPacketSender().sendClientPositionPacket(this, position);
    }

    public boolean hasSmoothAnimationSupport() {
        return smoothAnimationSupport;
    }

    public void setSmoothAnimationSupport(boolean smoothAnimationSupport) {
        this.smoothAnimationSupport = smoothAnimationSupport;
        Bukkit.broadcastMessage("Smoothcoasters " + (smoothAnimationSupport ? "enabled" : "disabled") + " for player " + this.bukkitPlayer.getName());
    }

    public void clearSmoothAnimationRotation(){
        if(!hasSmoothAnimationSupport()) return;
        JRidesPlugin.getSmoothAnimation().clearRotation(this);
    }

    public void setSmoothAnimationRotation(Quaternion orientation){
        if(!hasSmoothAnimationSupport()) return;
        JRidesPlugin.getSmoothAnimation().setRotation(this, orientation);
    }
}
