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

package com.jverbruggen.jrides.config.coaster.objects.section.ranged.transfer;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class TransferSectionPositionSpecConfig extends BaseConfig {
    private final String sectionAtStart;
    private final String sectionAtEnd;
    private final boolean sectionAtStartForwards;
    private final boolean sectionAtEndForwards;
    private final boolean sectionAtStartConnectsToStart;
    private final boolean sectionAtEndConnectsToStart;
    private final Vector3 position;
    private final Vector3 rotation;
    private final int moveTicks;

    public TransferSectionPositionSpecConfig(String sectionAtStart, String sectionAtEnd, boolean sectionAtStartForwards, boolean sectionAtEndForwards, boolean sectionAtStartConnectsToStart, boolean sectionAtEndConnectsToStart, Vector3 position, Vector3 rotation, int moveTicks) {
        this.sectionAtStart = sectionAtStart;
        this.sectionAtEnd = sectionAtEnd;
        this.sectionAtStartForwards = sectionAtStartForwards;
        this.sectionAtEndForwards = sectionAtEndForwards;
        this.sectionAtStartConnectsToStart = sectionAtStartConnectsToStart;
        this.sectionAtEndConnectsToStart = sectionAtEndConnectsToStart;
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

    public boolean isSectionAtStartConnectsToStart() {
        return sectionAtStartConnectsToStart;
    }

    public boolean isSectionAtEndConnectsToStart() {
        return sectionAtEndConnectsToStart;
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
        boolean sectionAtStartConnectsToStart = getBoolean(configurationSection, "sectionAtStartConnectsToStart", false);
        boolean sectionAtEndConnectsToStart = getBoolean(configurationSection, "sectionAtEndConnectsToStart", true);

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

        return new TransferSectionPositionSpecConfig(sectionAtStart, sectionAtEnd, sectionAtStartForwards, sectionAtEndForwards, sectionAtStartConnectsToStart, sectionAtEndConnectsToStart, position, rotation, moveTicks);
    }
}
