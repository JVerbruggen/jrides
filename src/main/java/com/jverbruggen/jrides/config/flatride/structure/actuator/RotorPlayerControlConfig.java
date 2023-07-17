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
        return playerControlConfig.createPlayerControl();
    }
}
