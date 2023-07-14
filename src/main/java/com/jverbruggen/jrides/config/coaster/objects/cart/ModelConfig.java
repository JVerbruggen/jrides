package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.EntityConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModelConfig extends BaseConfig {
    private final ItemConfig item;
    private final EntityConfig entity;
    private final Vector3 position;
    private final Quaternion rotation;

    public ModelConfig(ItemConfig item, EntityConfig entity, Vector3 position, Quaternion rotation) {
        this.item = item;
        this.entity = entity;
        this.position = position;
        this.rotation = rotation;
    }

    public ItemConfig getItem() {
        return item;
    }

    public EntityConfig getEntity() {
        return entity;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public FlatRideModel toFlatRideModel(Vector3 rootPosition, ViewportManager viewportManager){
        Vector3 spawnPosition = Vector3.add(rootPosition, position);
        VirtualEntity virtualEntity;
        if(getItem() != null)
            virtualEntity = viewportManager.spawnVirtualArmorstand(spawnPosition, new TrainModelItem(item.createItemStack()));
        else if(getEntity() != null)
            virtualEntity = viewportManager.spawnVirtualEntity(spawnPosition, getEntity().getEntityType());
        else throw new RuntimeException("No model or entity configured in toFlatRideModel");

        return new FlatRideModel(virtualEntity, position.clone(), rotation.clone());
    }

    public static ModelConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        if(configurationSection == null) return new ModelConfig(null, null, Vector3.zero(), new Quaternion());

        ItemConfig item = null;
        EntityConfig entity = null;

        if(configurationSection.contains("item"))
            item = ItemConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "item"));
        else if(configurationSection.contains("entity"))
            entity = EntityConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "entity"));
        else throw new RuntimeException("Model config needs an item or an entity");

        Vector3 vector = Vector3.fromDoubleList(getDoubleList(configurationSection, "position", List.of(0d,0d,0d)));
        Quaternion rotation = Quaternion.fromDoubleList(getDoubleList(configurationSection, "rotation", List.of(0d,0d,0d)));

        return new ModelConfig(item, entity, vector, rotation);
    }

    public static List<ModelConfig> multipleFromConfigurationSection(ConfigurationSection configurationSection){
        List<ModelConfig> modelConfigs = new ArrayList<>();
        Set<String> keys = configurationSection.getKeys(false);
        for(String key : keys){
            ModelConfig modelConfig = fromConfigurationSection(getConfigurationSection(configurationSection, key));
            modelConfigs.add(modelConfig);
        }
        return modelConfigs;
    }
}
