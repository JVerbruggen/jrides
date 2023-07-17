package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public interface EntityMovementConfig {
    TrainEffectTrigger createTrigger(ViewportManager viewportManager);

    static EntityMovementConfig fromConfigurationSection(String identifier, @Nullable ConfigurationSection configurationSection){
        if(configurationSection == null) return null;

        if(configurationSection.contains("locationFrom") || configurationSection.contains("rotationFrom")){
            return EntityFromToMovementConfig.fromConfigurationSection(identifier, configurationSection);
        }else if(configurationSection.contains("locationDelta") || configurationSection.contains("rotationDelta")){
            return EntityContinuousMovementConfig.fromConfigurationSection(identifier, configurationSection);
        }

        throw new RuntimeException("Unknown entity movement in " + identifier);
    }
}
