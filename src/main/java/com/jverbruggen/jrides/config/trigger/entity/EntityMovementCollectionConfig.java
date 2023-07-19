package com.jverbruggen.jrides.config.trigger.entity;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.effect.platform.EntityMovementTrigger;
import com.jverbruggen.jrides.effect.train.SequenceTrainEffectTrigger;
import com.jverbruggen.jrides.effect.train.TrainEffectTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.LocRot;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityMovementCollectionConfig extends BaseConfig {
    @SuppressWarnings("unused")
    private final String identifier; //TODO: integrate
    private final ItemConfig itemConfig;
    private final Vector3 spawnLocation;
    private final Vector3 spawnRotation;
    private final List<EntityMovementConfig> entityMovementConfigs;

    public EntityMovementCollectionConfig(String identifier, ItemConfig itemConfig, Vector3 spawnLocation, Vector3 spawnRotation, List<EntityMovementConfig> entityMovementConfigs) {
        this.identifier = identifier;
        this.itemConfig = itemConfig;
        this.spawnLocation = spawnLocation;
        this.spawnRotation = spawnRotation;
        this.entityMovementConfigs = entityMovementConfigs;
    }

    static EntityMovementCollectionConfig fromConfigurationSection(String identifier, @Nullable ConfigurationSection configurationSection){
        if(configurationSection == null) return null;

        ConfigurationSection animationConfigurationSection = configurationSection.getConfigurationSection("animation");

        List<EntityMovementConfig> entityMovements = new ArrayList<>();
        if(animationConfigurationSection != null){
            Set<String> keys = animationConfigurationSection.getKeys(false);
            for(String key : keys){
                EntityMovementConfig movementConfig = getMovementConfig(identifier, animationConfigurationSection.getConfigurationSection(key));
                if(movementConfig == null) continue;

                entityMovements.add(movementConfig);
            }
        }

        Vector3 spawnLocation = Vector3.fromDoubleList(getDoubleList(configurationSection, "spawnLocation", null));
        Vector3 spawnRotation = Vector3.fromDoubleList(getDoubleList(configurationSection, "spawnRotation", null));
        ItemConfig itemConfig = ItemConfig.fromConfigurationSection(configurationSection);

        return new EntityMovementCollectionConfig(identifier, itemConfig, spawnLocation, spawnRotation, entityMovements);
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

    private LocRot getSpawnLocation(){
        if(spawnLocation != null) {
            return LocRot.fromLocationRotation(spawnLocation, spawnRotation);
        }

        if(entityMovementConfigs.size() == 0) throw new RuntimeException("Cannot get spawn location");

        LocRot initialLocation = null;
        for(EntityMovementConfig config : entityMovementConfigs){
            Vector3 possibleLocation = config.getInitialLocation();
            if(possibleLocation != null){
                initialLocation = new LocRot(possibleLocation, config.getInitialRotation());
                break;
            }
        }

        if(initialLocation == null) throw new RuntimeException("Animation does not support initial location, cannot spawn entity");

        return initialLocation;
    }

    public TrainEffectTrigger createTrigger(ViewportManager viewportManager) {
        LocRot spawnLocation = getSpawnLocation();
        VirtualEntity virtualEntity = itemConfig.spawnEntity(viewportManager, spawnLocation.location());
        virtualEntity.setRotation(Quaternion.fromAnglesVector(spawnLocation.rotation()));

        List<EntityMovementTrigger> triggerSequence = entityMovementConfigs.stream()
                .map(c -> c.createTrigger(virtualEntity))
                .collect(Collectors.toList());

        return new SequenceTrainEffectTrigger(triggerSequence);
    }
}
