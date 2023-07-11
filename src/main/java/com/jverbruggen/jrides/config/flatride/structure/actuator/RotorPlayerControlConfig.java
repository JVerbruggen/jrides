package com.jverbruggen.jrides.config.flatride.structure.actuator;

import com.jverbruggen.jrides.animator.flatride.rotor.RotorPlayerControl;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.flatride.structure.ControlConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.List;

public class RotorPlayerControlConfig extends BaseConfig implements ControlConfig {
    public final String type;
    public final float lowerSpeed;
    public final float upperSpeed;
    public final float acceleration;
    public final String accumulator;

    public RotorPlayerControlConfig(String type, float lowerSpeed, float upperSpeed, float acceleration, String accumulator) {
        this.type = type;
        this.lowerSpeed = lowerSpeed;
        this.upperSpeed = upperSpeed;
        this.acceleration = acceleration;
        this.accumulator = accumulator;
    }

    public String getType() {
        return type;
    }

    public float getLowerSpeed() {
        return lowerSpeed;
    }

    public float getUpperSpeed() {
        return upperSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public String getAccumulator() {
        return accumulator;
    }

    public static RotorPlayerControlConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        if(configurationSection == null) return null;

        String type = getString(configurationSection, "type");
        List<Double> speed = getDoubleList(configurationSection, "speed");
        float acceleration = (float)getDouble(configurationSection, "acceleration");
        String accumulator = getString(configurationSection, "accumulator");

        if(speed.size() != 2)
            throw new RuntimeException("Only supports rotor control config with speed of length 2! [<lower_bound>, <upper_bound>] i.e. [-1, 1]");

        return new RotorPlayerControlConfig(
                type, speed.get(0).floatValue(), speed.get(1).floatValue(), acceleration, accumulator
        );
    }

    @Override
    public RotorPlayerControl createPlayerControl() {
        return new RotorPlayerControl(lowerSpeed, upperSpeed, acceleration);
    }
}
