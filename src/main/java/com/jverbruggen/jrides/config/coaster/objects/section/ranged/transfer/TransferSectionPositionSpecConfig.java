package com.jverbruggen.jrides.config.coaster.objects.section.ranged.transfer;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class TransferSectionPositionSpecConfig extends BaseConfig {
    private final String sectionAtStart;
    private final String sectionAtEnd;
    private final boolean sectionAtStartForwards;
    private final boolean sectionAtEndForwards;
    private final Vector3 position;
    private final Vector3 rotation;
    private final int moveTicks;

    public TransferSectionPositionSpecConfig(String sectionAtStart, String sectionAtEnd, boolean sectionAtStartForwards, boolean sectionAtEndForwards, Vector3 position, Vector3 rotation, int moveTicks) {
        this.sectionAtStart = sectionAtStart;
        this.sectionAtEnd = sectionAtEnd;
        this.sectionAtStartForwards = sectionAtStartForwards;
        this.sectionAtEndForwards = sectionAtEndForwards;
        this.position = position;
        this.rotation = rotation;
        this.moveTicks = moveTicks;
    }

    public String getSectionAtStart() {
        return sectionAtStart;
    }

    public String getSectionAtEnd() {
        return sectionAtEnd;
    }

    public boolean isSectionAtStartForwards() {
        return sectionAtStartForwards;
    }

    public boolean isSectionAtEndForwards() {
        return sectionAtEndForwards;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public int getMoveTicks() {
        return moveTicks;
    }

    @Override
    public String toString() {
        return "Transfer position of " + getPosition() + " ticks " + getMoveTicks();
    }

    public static TransferSectionPositionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String sectionAtStart = configurationSection.getString("sectionAtStart");
        String sectionAtEnd = configurationSection.getString("sectionAtEnd");
        boolean sectionAtStartForwards = getBoolean(configurationSection, "sectionAtStartForwards", false);
        boolean sectionAtEndForwards = getBoolean(configurationSection, "sectionAtEndForwards", true);

        int moveTicks;
        if(configurationSection.contains("moveTicks"))
            moveTicks = configurationSection.getInt("moveTicks");
        else moveTicks = 0;

        Vector3 position;
        if(configurationSection.contains("position"))
            position = Vector3.fromDoubleList(configurationSection.getDoubleList("position"));
        else position = new Vector3(0,0,0);

        Vector3 rotation;
        if(configurationSection.contains("rotation"))
            rotation = Vector3.fromDoubleList(configurationSection.getDoubleList("rotation"));
        else rotation = new Vector3(0,0,0);

        return new TransferSectionPositionSpecConfig(sectionAtStart, sectionAtEnd, sectionAtStartForwards, sectionAtEndForwards, position, rotation, moveTicks);
    }
}
