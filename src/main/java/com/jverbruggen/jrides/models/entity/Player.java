package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.Location;

public class Player {
    private org.bukkit.entity.Player bukkitPlayer;

    public Player(org.bukkit.entity.Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    public Vector3 getLocation(){
        Location location = bukkitPlayer.getLocation();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return new Vector3(x, y, z);
    }

    public org.bukkit.entity.Player getBukkitPlayer(){
        return bukkitPlayer;
    }
}
