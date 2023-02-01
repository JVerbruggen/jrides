package com.jverbruggen.jrides.config.coaster.objects.section.transfer;

import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class TransferSectionSpecConfig {
    private final String sectionAtStart;
    private final String sectionAtEnd;
    private final Vector3 position;
    private final Vector3 rotation;
    private final int moveTicks;

    public TransferSectionSpecConfig(String sectionAtStart, String sectionAtEnd, Vector3 position, Vector3 rotation, int moveTicks) {
        this.sectionAtStart = sectionAtStart;
        this.sectionAtEnd = sectionAtEnd;
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

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public int getMoveTicks() {
        return moveTicks;
    }

    public static TransferSectionSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String sectionAtStart = configurationSection.getString("sectionAtStart");
        String sectionAtEnd = configurationSection.getString("sectionAtEnd");
        int moveTicks = configurationSection.getInt("moveTicks");
        Vector3 position = Vector3.fromDoubleList(configurationSection.getDoubleList("position"));
        Vector3 rotation = Vector3.fromDoubleList(configurationSection.getDoubleList("rotation"));
        return new TransferSectionSpecConfig(sectionAtStart, sectionAtEnd, position, rotation, moveTicks);
    }
}
