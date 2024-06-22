package com.jverbruggen.jrides.config.flatride.structure.actuator.rotor;

import com.jverbruggen.jrides.animator.flatride.rotor.mode.RotorActuatorMode;
import com.jverbruggen.jrides.animator.flatride.rotor.mode.SineMode;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Supplier;

public class SineRotorConfig extends BaseConfig implements RotorTypeConfig {
    private final float size;
    private final Supplier<Integer> phase;

    public SineRotorConfig(float size, Supplier<Integer> phase) {
        this.size = size;
        this.phase = phase;
    }

    @Override
    public RotorActuatorMode createActuatorMode() {
        return new SineMode(size, phase.get().shortValue());
    }

    public static SineRotorConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        float size = (float)getDouble(configurationSection, "size", 1d);
        Supplier<Integer> phase = getIntSupplier(configurationSection, "phase", 0);

        return new SineRotorConfig(size, phase);
    }
}
