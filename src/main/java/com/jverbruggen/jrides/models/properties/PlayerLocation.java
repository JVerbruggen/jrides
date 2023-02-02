package com.jverbruggen.jrides.models.properties;

import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class PlayerLocation {
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;

    public PlayerLocation(double x, double y, double z, double yaw, double pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public Vector3 toVector3(){
        return new Vector3(x,y,z);
    }

    public Location toBukkitLocation(World world){
        return new Location(world, x, y, z, (float)yaw, (float)pitch);
    }

    public static PlayerLocation fromDoubleList(List<Double> doubleList){
        if(doubleList.size() != 3 && doubleList.size() != 5)
            return null;

        double x = doubleList.get(0);
        double y = doubleList.get(1);
        double z = doubleList.get(2);
        double yaw = 0;
        double pitch = 0;

        if(doubleList.size() == 5){
            yaw = doubleList.get(3);
            pitch = doubleList.get(4);
        }

        return new PlayerLocation(x, y, z, yaw, pitch);
    }
}
