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

package com.jverbruggen.jrides.config.coaster.objects.section.ranged;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class DriveAndReleaseSectionSpecConfig extends BaseConfig {
    private final double engage;
    private final double driveSpeed;
    private final double acceleration;
    private final double deceleration;
    private final int waitTicks;

    public DriveAndReleaseSectionSpecConfig(double engage, double driveSpeed, double acceleration, double deceleration, int waitTicks) {
        this.engage = engage;
        this.driveSpeed = driveSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.waitTicks = waitTicks;
    }

    public double getEngage() {
        return engage;
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public int getWaitTicks() {
        return waitTicks;
    }

    public static DriveAndReleaseSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double driveSpeed = getDouble(configurationSection, "driveSpeed", 1.0);
        double acceleration = getDouble(configurationSection, "acceleration", 1.0);
        double deceleration = getDouble(configurationSection, "deceleration", acceleration);
        double engage = getDouble(configurationSection, "engage", .5);
        int waitTicks = getInt(configurationSection, "waitIntervalTicks", 1);

        return new DriveAndReleaseSectionSpecConfig(engage, driveSpeed, acceleration, deceleration, waitTicks);
    }
}
