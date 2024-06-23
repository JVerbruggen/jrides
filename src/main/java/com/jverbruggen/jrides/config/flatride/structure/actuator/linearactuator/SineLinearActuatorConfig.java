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

package com.jverbruggen.jrides.config.flatride.structure.actuator.linearactuator;

import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.LinearActuatorMode;
import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.SineMode;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Supplier;

public class SineLinearActuatorConfig extends BaseConfig implements LinearActuatorTypeConfig {
    private final float size;
    private final Supplier<Integer> phase;

    public SineLinearActuatorConfig(float size, Supplier<Integer> phase) {
        this.size = size;
        this.phase = phase;
    }

    @Override
    public LinearActuatorMode createActuatorMode() {
        return new SineMode(size, phase.get().shortValue());
    }

    public static SineLinearActuatorConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        float size = (float)getDouble(configurationSection, "size", 1d);
        Supplier<Integer> phase = getIntSupplier(configurationSection, "phase", 0);

        return new SineLinearActuatorConfig(size, phase);
    }
}
