package com.jverbruggen.jrides.config.flatride.timing;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
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

    public TimingSequence createTimingSequence(List<FlatRideComponent> flatRideComponents) {
        List<InstructionSequenceItem> items = new ArrayList<>();
        for(TimingPhaseConfig phaseConfig : getPhases()){
            items.add(phaseConfig.getInstructionSequenceItem(flatRideComponents));
        }
        return new TimingSequence(items);
    }
}
