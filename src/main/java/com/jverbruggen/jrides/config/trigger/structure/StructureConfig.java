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

package com.jverbruggen.jrides.config.trigger.structure;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.structure.StructureEffectTrigger;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class StructureConfig extends BaseTriggerConfig {
    private final String structureIdentifier;
    private final Vector3 position;
    private final int delayTicks;

    public StructureConfig(String structureIdentifier, Vector3 position, int delayTicks) {
        super(TriggerType.STRUCTURE);
        this.structureIdentifier = structureIdentifier;
        this.position = position;
        this.delayTicks = delayTicks;
    }

    public String getStructureIdentifier() {
        return structureIdentifier;
    }

    public Vector3 getPosition() {
        return position;
    }

    public int getDelayTicks() {
        return delayTicks;
    }

    public static StructureConfig fromConfigurationSection(ConfigurationSection configurationSection){
        String structureIdentifier = getString(configurationSection, "structureIdentifier");
        Vector3 position = Vector3.fromDoubleList(getDoubleList(configurationSection, "position"));
        int delayTicks = getInt(configurationSection, "delayTicks", 0);

        return new StructureConfig(structureIdentifier, position, delayTicks);
    }

    @Override
    public EffectTrigger createTrigger(String rideIdentifier) {
        return StructureEffectTrigger.createStructureEffectTrigger(getDelayTicks(), getPosition(), getStructureIdentifier());
    }
}
