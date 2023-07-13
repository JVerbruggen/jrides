package com.jverbruggen.jrides.config.flatride.structure.actuator;

import com.jverbruggen.jrides.animator.flatride.rotor.RotorPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.RotorSpeedPlayerControl;
import com.jverbruggen.jrides.animator.flatride.rotor.RotorTargetPositionPlayerControl;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.flatride.structure.ControlConfig;
import com.jverbruggen.jrides.models.math.MathUtil;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.List;

public class RotorPlayerControlConfig extends BaseConfig implements ControlConfig {
    public final String type;
    public final float lowerSpeed;
    public final float upperSpeed;
    public final float lowerPosition;
    public final float upperPosition;

    public final float acceleration;
    public final String accumulator;

    public RotorPlayerControlConfig(String type, float lowerSpeed, float upperSpeed, float lowerPosition, float upperPosition, float acceleration, String accumulator) {
        this.type = type;
        this.lowerSpeed = lowerSpeed;
        this.upperSpeed = upperSpeed;
        this.lowerPosition = lowerPosition;
        this.upperPosition = upperPosition;
        this.acceleration = acceleration;
        this.accumulator = accumulator;
    }

    public String getType() {
        return type;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public static RotorPlayerControlConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        if(configurationSection == null) return null;

        String type = getString(configurationSection, "type");
        List<Double> speed = getDoubleList(configurationSection, "speed", List.of(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        List<Double> position = getDoubleList(configurationSection, "position", List.of(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        float acceleration = (float)getDouble(configurationSection, "acceleration");
        String accumulator = getString(configurationSection, "accumulator");

        if(speed.size() != 2)
            throw new RuntimeException("Only supports rotor control config with speed of length 2! [<lower_bound>, <upper_bound>] i.e. [-1, 1]");
        if(position.size() != 2)
            throw new RuntimeException("Only supports rotor control config with position of length 2! [<lower_bound>, <upper_bound>] i.e. [0, 45]");

        float lowerSpeed = speed.get(0).floatValue();
        float upperSpeed = speed.get(1).floatValue();
        float lowerPosition = MathUtil.floorMod(position.get(0).floatValue(), 360);
        float upperPosition = MathUtil.floorMod(position.get(1).floatValue(), 360);

        if(lowerSpeed != Float.NEGATIVE_INFINITY && lowerPosition != Float.NEGATIVE_INFINITY)
            throw new RuntimeException("Rotor control config can only contain either one of position or speed, not both!");

        return new RotorPlayerControlConfig(
                type, lowerSpeed, upperSpeed, lowerPosition, upperPosition, Math.abs(acceleration), accumulator
        );
    }

    @Override
    public RotorPlayerControl createPlayerControl() {
        if(lowerPosition != Float.NEGATIVE_INFINITY){
            return new RotorTargetPositionPlayerControl(lowerPosition, upperPosition, acceleration);
        }
        else
            return new RotorSpeedPlayerControl(lowerSpeed, upperSpeed, acceleration);
    }
}
