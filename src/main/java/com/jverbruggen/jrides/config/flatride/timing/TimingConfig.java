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

package com.jverbruggen.jrides.config.flatride.timing;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.timing.TimingSequence;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.InstructionSequenceItem;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TimingConfig extends BaseConfig {
    private final List<TimingPhaseConfig> phases;

    public TimingConfig(List<TimingPhaseConfig> phases) {
        this.phases = phases;
    }

    public List<TimingPhaseConfig> getPhases() {
        return phases;
    }

    public static TimingConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        List<TimingPhaseConfig> items = new ArrayList<>();

        if(configurationSection != null){
            Set<String> phases = configurationSection.getKeys(false);
            for(String phase : phases){
                ConfigurationSection itemConfigurationSection = getConfigurationSection(configurationSection, phase);
                if(itemConfigurationSection == null) throw new RuntimeException("No contents in phase " + phase);

                TimingPhaseConfig structureConfigItem = TimingPhaseConfig.fromConfigurationSection(
                        getConfigurationSection(configurationSection, phase), phase);

                if(structureConfigItem != null)
                    items.add(structureConfigItem);
            }
        }

        return new TimingConfig(items);
    }

    public TimingSequence createTimingSequence(FlatRideHandle flatRideHandle, List<FlatRideComponent> flatRideComponents) {
        List<InstructionSequenceItem> items = new ArrayList<>();
        for(TimingPhaseConfig phaseConfig : getPhases()){
            items.add(phaseConfig.getInstructionSequenceItem(flatRideHandle, flatRideComponents));
        }
        return new TimingSequence(items);
    }
}
