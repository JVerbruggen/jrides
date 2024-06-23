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

package com.jverbruggen.jrides.config.flatride.structure.actuator.rotor;

import com.jverbruggen.jrides.animator.flatride.rotor.mode.ContinuousMode;
import com.jverbruggen.jrides.animator.flatride.rotor.mode.RotorActuatorMode;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

public class ContinuousRotorConfig extends BaseConfig implements RotorTypeConfig {
    private final Double lowerBound;
    private final Double upperBound;

    public ContinuousRotorConfig(Double lowerBound, Double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public RotorActuatorMode createActuatorMode() {
        return new ContinuousMode(lowerBound, upperBound);
    }

    public static ContinuousRotorConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        Double lowerBound = getDoubleObj(configurationSection, "lowerBound", null);
        Double upperBound = getDoubleObj(configurationSection, "upperBound", null);
        return new ContinuousRotorConfig(lowerBound, upperBound);
    }
}
