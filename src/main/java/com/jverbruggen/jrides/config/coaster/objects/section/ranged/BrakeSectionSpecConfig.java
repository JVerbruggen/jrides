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

public class BrakeSectionSpecConfig extends BaseConfig {
    private final double driveSpeed;
    private final double deceleration;

    public BrakeSectionSpecConfig(double driveSpeed, double deceleration) {
        this.driveSpeed = driveSpeed;
        this.deceleration = deceleration;
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public static BrakeSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        double driveSpeed = getDouble(configurationSection, "driveSpeed");
        double deceleration = getDouble(configurationSection, "deceleration");

        return new BrakeSectionSpecConfig(driveSpeed, deceleration);
    }
}
