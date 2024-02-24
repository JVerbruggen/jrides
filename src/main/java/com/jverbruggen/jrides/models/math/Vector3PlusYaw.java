package com.jverbruggen.jrides.models.math;

import com.jverbruggen.jrides.api.JRidesPlayerLocation;
import org.bukkit.util.Vector;

import java.util.List;

public class Vector3PlusYaw extends Vector3 {
    private static final Vector3PlusYaw ZERO = new Vector3PlusYaw(0,0,0,0);

    public double yaw;

    public Vector3PlusYaw() {
        super();
        this.yaw = 0.0;
    }

    public Vector3PlusYaw(Vector vector) {
        this(vector.getX(), vector.getY(), vector.getZ(), 0.0);
    }

    public Vector3PlusYaw(double x, double y, double z, double yaw)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
    }

    public static Vector3PlusYaw zero() {
        return Vector3PlusYaw.ZERO;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public static Vector3PlusYaw fromDoubleList(List<Double> doubleList){
        if(doubleList == null) return null;
        if(doubleList.size() == 3){
            return new Vector3PlusYaw(doubleList.get(0), doubleList.get(1), doubleList.get(2), 0.0);
        }else if(doubleList.size() == 4){
            return new Vector3PlusYaw(doubleList.get(0), doubleList.get(1), doubleList.get(2), doubleList.get(3));
        }

        throw new RuntimeException("Can only create vector+yaw from double list of length 3 or 4");
    }

    public static Vector3PlusYaw fromPlayerLocation(JRidesPlayerLocation playerLocation){
        return new Vector3PlusYaw(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw());
    }
}