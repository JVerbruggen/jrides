package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.effect.platform.EntityMovementTrigger;
import com.jverbruggen.jrides.effect.train.SequenceTrainEffectTrigger;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityMovementCollectionConfig {
    @SuppressWarnings("unused")
    private final String identifier; //TODO: integrate
    private final ItemConfig itemConfig;
    private final List<EntityMovementConfig> entityMovementConfigs;

    public EntityMovementCollectionConfig(String identifier, ItemConfig itemConfig, List<EntityMovementConfig> entityMovementConfigs) {
        this.identifier = identifier;
        this.itemConfig = itemConfig;
        this.entityMovementConfigs = entityMovementConfigs;
    }

    static EntityMovementCollectionConfig fromConfigurationSection(String identifier, @Nullable ConfigurationSection configurationSection){
        if(configurationSection == null) return null;

        ConfigurationSection animationConfigurationSection = configurationSection.getConfigurationSection("animation");
        if(animationConfigurationSection == null) throw new RuntimeException("Entity movement " + identifier + " does not have an animation");

        Set<String> keys = animationConfigurationSection.getKeys(false);
        List<EntityMovementConfig> entityMovements = new ArrayList<>();

        for(String key : keys){
            EntityMovementConfig movementConfig = getMovementConfig(identifier, animationConfigurationSection.getConfigurationSection(key));
            if(movementConfig == null) continue;

            entityMovements.add(movementConfig);
        }

        ItemConfig itemConfig = ItemConfig.fromConfigurationSection(configurationSection);

        return new EntityMovementCollectionConfig(identifier, itemConfig, entityMovements);
    }

    static EntityMovementConfig getMovementConfig(String identifier, @Nullable ConfigurationSection configurationSection){
        if(configurationSection == null) return null;

        if(configurationSection.contains("locationFrom") || configurationSection.contains("rotationFrom")){
            return EntityFromToMovementConfig.fromConfigurationSection(configurationSection);
        }else if(configurationSection.contains("locationDelta") || configurationSection.contains("rotationDelta")){
            return EntityContinuousMovementConfig.fromConfigurationSection(configurationSection);
        }

        throw new RuntimeException("Unknown entity movement in " + identifier);
    }

    private Vector3 getSpawnLocation(){
        if(entityMovementConfigs.size() == 0) throw new RuntimeException("Cannot get spawn location");

        Vector3 initialLocation = null;
        for(EntityMovementConfig config : entityMovementConfigs){
            Vector3 possibleLocation = config.getInitialLocation();
            if(possibleLocation != null){
                initialLocation = possibleLocation;
                break;
            }
        }

        if(initialLocation == null) throw new RuntimeException("Animation does not support initial location, cannot spawn entity");

        return initialLocation;
    }

    public TrainEffectTrigger createTrigger(ViewportManager viewportManager) {
        VirtualEntity virtualEntity = itemConfig.spawnEntity(viewportManager, getSpawnLocation());

        List<EntityMovementTrigger> triggerSequence = entityMovementConfigs.stream()
                .map(c -> c.createTrigger(virtualEntity))
                .collect(Collectors.toList());

        return new SequenceTrainEffectTrigger(triggerSequence);
    }
}
