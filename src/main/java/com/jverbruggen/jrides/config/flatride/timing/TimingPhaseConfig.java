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
import com.jverbruggen.jrides.animator.flatride.timing.instruction.InstructionSequenceItem;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.TimingAction;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.List;

public class TimingPhaseConfig extends BaseConfig {
    @SuppressWarnings("unused")
    private final String phaseIdentifier; //TODO: integrate
    private final int durationTicks;
    private final List<ActionConfig> actions;
    public TimingPhaseConfig(String phaseIdentifier, int durationTicks, List<ActionConfig> actions) {
        this.phaseIdentifier = phaseIdentifier;
        this.durationTicks = durationTicks;
        this.actions = actions;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public static TimingPhaseConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection, String phaseIdentifier) {
        if(configurationSection == null) return null;

        int durationTicks = getInt(configurationSection, "durationTicks");
        List<ActionConfig> actions = ActionConfigFactory.fromConfigurationSection(
                getConfigurationSection(configurationSection, "actions"));

        return new TimingPhaseConfig(phaseIdentifier, durationTicks, actions);
    }

    public InstructionSequenceItem getInstructionSequenceItem(FlatRideHandle flatRideHandle, List<FlatRideComponent> flatRideComponents){
        int durationTicks = this.getDurationTicks();
        List<TimingAction> timingActions = actions.stream()
                .flatMap(a -> a.getTimingAction(flatRideHandle, flatRideComponents).stream())
                .toList();

        return new InstructionSequenceItem(durationTicks, timingActions);
    }
}
