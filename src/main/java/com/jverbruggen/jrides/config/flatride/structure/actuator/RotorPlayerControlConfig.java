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

package com.jverbruggen.jrides.config.flatride.structure.actuator;

import com.jverbruggen.jrides.animator.flatride.rotor.RotorPlayerControl;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.flatride.structure.ControlConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.playercontrol.PlayerControlConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.playercontrol.SpeedControlConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.playercontrol.TowardsPositionControlConfig;
import com.jverbruggen.jrides.models.math.MathUtil;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.List;

public class RotorPlayerControlConfig extends BaseConfig implements ControlConfig {
    public final String type;
    public final PlayerControlConfig playerControlConfig;
    public final float accelerate;
    public final String accumulator;

    public RotorPlayerControlConfig(String type, PlayerControlConfig playerControlConfig, float accelerate, String accumulator) {
        this.type = type;
        this.playerControlConfig = playerControlConfig;
        this.accelerate = accelerate;
        this.accumulator = accumulator;
    }

    public String getType() {
        return type;
    }

    public static RotorPlayerControlConfig fromConfigurationSection(String identifier, @Nullable ConfigurationSection configurationSection) {
        if(configurationSection == null) return null;

        String type = getString(configurationSection, "type");
        List<Double> speed = getDoubleList(configurationSection, "speed", List.of(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        List<Double> position = getDoubleList(configurationSection, "position", List.of(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        float accelerate = (float)getDouble(configurationSection, "accelerate");
        String accumulator = getString(configurationSection, "accumulator");

        if(speed.size() != 2)
            throw new RuntimeException("Only supports rotor control config with speed of length 2! [<lower_bound>, <upper_bound>] i.e. [-1, 1]");
        if(position.size() != 2)
            throw new RuntimeException("Only supports rotor control config with position of length 2! [<lower_bound>, <upper_bound>] i.e. [0, 45]");

        float lowerSpeed = speed.get(0).floatValue();
        float upperSpeed = speed.get(1).floatValue();
        float lowerPosition = position.get(0).floatValue();
        float upperPosition = position.get(1).floatValue();

        PlayerControlConfig playerControlConfig = null;
        if(lowerSpeed != Float.NEGATIVE_INFINITY){
            playerControlConfig = new SpeedControlConfig(lowerSpeed, upperSpeed, accelerate);
        }
        if(lowerPosition != Float.NEGATIVE_INFINITY){
            if(playerControlConfig != null)
                throw new RuntimeException("Rotor control config in '" + identifier + "' can only contain either one of position or speed, not both!");

            playerControlConfig = new TowardsPositionControlConfig(MathUtil.floorMod(lowerPosition, 360), MathUtil.floorMod(upperPosition, 360), accelerate);
        }

        return new RotorPlayerControlConfig(
                type, playerControlConfig, Math.abs(accelerate), accumulator
        );
    }

    @Override
    public RotorPlayerControl createPlayerControl() {
        if(type.equalsIgnoreCase("space_bar_control")){
            return playerControlConfig.createSpaceBarPlayerControl();
        }else if(type.equalsIgnoreCase("w_s_control")){
            return playerControlConfig.createWSPlayerControl();
        }else if(type.equalsIgnoreCase("a_d_control")){
            return playerControlConfig.createADPlayerControl();
        }

        throw new RuntimeException("Unknown player control type '" + type + "'");
    }
}
