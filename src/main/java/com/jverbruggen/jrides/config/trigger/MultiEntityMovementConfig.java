package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MultiEntityMovementConfig extends BaseTriggerConfig{
    private final List<EntityMovementConfig> entityMovements;

    public MultiEntityMovementConfig(List<EntityMovementConfig> entityMovements) {
        super(TriggerType.MULTI_ARMORSTAND_MOVEMENT);
        this.entityMovements = entityMovements;
    }

    public List<EntityMovementConfig> getEntityMovements() {
        return entityMovements;
    }

    public List<TrainEffectTrigger> createTriggers(ViewportManager viewportManager){
        List<TrainEffectTrigger> armorstandMovements = new ArrayList<>();
        for(EntityMovementConfig entityMovementConfig : getEntityMovements()) {
            armorstandMovements.add(entityMovementConfig.createTrigger(viewportManager));
        }

        return armorstandMovements;
    }

    public static MultiEntityMovementConfig fromConfigurationSection(ConfigurationSection configurationSection){
        ConfigurationSection entitiesSection = configurationSection.getConfigurationSection("entities");
        if(entitiesSection == null) throw new RuntimeException("Trigger section needs 'entities' key");

        Set<String> entityIdentifiers = entitiesSection.getKeys(false);

        List<EntityMovementConfig> entityMovements = new ArrayList<>();
        for(String identifier : entityIdentifiers){
            EntityMovementConfig entityMovement = EntityMovementConfig.fromConfigurationSection(identifier, entitiesSection.getConfigurationSection(identifier));
            if(entityMovement == null){
                JRidesPlugin.getLogger().warning("Entity movement " + identifier + " was null");
                continue;
            }
            entityMovements.add(entityMovement);
        }

        return new MultiEntityMovementConfig(entityMovements);
    }
}
