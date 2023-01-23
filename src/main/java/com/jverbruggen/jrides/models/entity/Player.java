package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;
import org.bukkit.Location;

public class Player {
    private org.bukkit.entity.Player bukkitPlayer;
    private Seat seatedOn;

    public Player(org.bukkit.entity.Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.seatedOn = null;
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
}
