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
