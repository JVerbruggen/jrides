package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.effect.platform.MultipleEffectExecutorTrigger;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MultiEntityMovementConfig extends BaseTriggerConfig{
    private final List<EntityMovementCollectionConfig> entityMovements;

    public MultiEntityMovementConfig(List<EntityMovementCollectionConfig> entityMovements) {
        super(TriggerType.MULTI_ENTITY_MOVEMENT);
        this.entityMovements = entityMovements;
    }

    public List<EntityMovementCollectionConfig> getEntityMovements() {
        return entityMovements;
    }

    public List<TrainEffectTrigger> createTriggers(ViewportManager viewportManager){
        List<TrainEffectTrigger> armorstandMovements = new ArrayList<>();
        for(EntityMovementCollectionConfig entityMovementConfig : getEntityMovements()) {
            armorstandMovements.add(entityMovementConfig.createTrigger(viewportManager));
        }

        return armorstandMovements;
    }

    public static MultiEntityMovementConfig fromConfigurationSection(ConfigurationSection configurationSection){
        ConfigurationSection entitiesSection = configurationSection.getConfigurationSection("entities");
        if(entitiesSection == null) throw new RuntimeException("Trigger section needs 'entities' key");

        Set<String> entityIdentifiers = entitiesSection.getKeys(false);

        List<EntityMovementCollectionConfig> entityMovements = new ArrayList<>();
        for(String identifier : entityIdentifiers){
            EntityMovementCollectionConfig entityMovementCollection = EntityMovementCollectionConfig.fromConfigurationSection(identifier, entitiesSection.getConfigurationSection(identifier));
            if(entityMovementCollection == null){
                JRidesPlugin.getLogger().warning("Entity movement " + identifier + " was null");
                continue;
            }
            entityMovements.add(entityMovementCollection);
        }

        return new MultiEntityMovementConfig(entityMovements);
    }

    @Override
    public TrainEffectTrigger createTrigger() {
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
        List<TrainEffectTrigger> entityMovementTriggers = createTriggers(viewportManager);

        return new MultipleEffectExecutorTrigger(entityMovementTriggers);
    }
}
