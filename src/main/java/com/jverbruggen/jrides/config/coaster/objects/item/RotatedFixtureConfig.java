package com.jverbruggen.jrides.config.coaster.objects.item;

import com.jverbruggen.jrides.animator.flatride.rotor.fixture.RotatedFixture;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public class RotatedFixtureConfig extends BaseConfig {
    private final boolean axisX;
    private final boolean axisY;
    private final boolean axisZ;

    public RotatedFixtureConfig(boolean axisX, boolean axisY, boolean axisZ) {
        this.axisX = axisX;
        this.axisY = axisY;
        this.axisZ = axisZ;
    }

    public boolean isAxisX() {
        return axisX;
    }

    public boolean isAxisY() {
        return axisY;
    }

    public boolean isAxisZ() {
        return axisZ;
    }

    public static RotatedFixtureConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        boolean axisX = getBoolean(configurationSection, "axisX", false);
        boolean axisY = getBoolean(configurationSection, "axisY", false);
        boolean axisZ = getBoolean(configurationSection, "axisZ", false);

        return new RotatedFixtureConfig(axisX, axisY, axisZ);
    }

    public RotatedFixture create() {
        return new RotatedFixture(axisX, axisY, axisZ);
    }
}
