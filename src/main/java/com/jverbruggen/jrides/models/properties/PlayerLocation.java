/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.models.properties;

import com.jverbruggen.jrides.api.JRidesPlayerLocation;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class PlayerLocation implements JRidesPlayerLocation {
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

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public Vector3 toVector3(){
        return new Vector3(x,y,z);
    }

    public Location toBukkitLocation(World world){
        return new Location(world, x, y, z, (float)yaw, (float)pitch);
    }

    public static PlayerLocation fromDoubleList(List<Double> doubleList){
        if(doubleList == null)
            return null;
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

    public static PlayerLocation fromVector3(Vector3 vector3){
        return new PlayerLocation(vector3.getX(), vector3.getY(), vector3.getZ(), 0, 0);
    }
}
