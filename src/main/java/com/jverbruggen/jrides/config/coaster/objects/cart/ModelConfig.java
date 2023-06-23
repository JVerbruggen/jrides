package com.jverbruggen.jrides.config.coaster.objects.cart;

import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModelConfig extends BaseConfig {
    private final ItemConfig item;
    private final Vector3 position;

    public ModelConfig(ItemConfig item, Vector3 position) {
        this.item = item;
        this.position = position;
    }

    public ItemConfig getItem() {
        return item;
    }

    public Vector3 getPosition() {
        return position;
    }

    public FlatRideModel toFlatRideModel(Vector3 rootPosition, ViewportManager viewportManager){
        Vector3 spawnPosition = Vector3.add(rootPosition, position);
        VirtualEntity virtualEntity = viewportManager.spawnVirtualArmorstand(spawnPosition, new TrainModelItem(item.createItemStack()));
        return new FlatRideModel(virtualEntity, position.clone());
    }

    public static ModelConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        ItemConfig item = ItemConfig.fromConfigurationSection(getConfigurationSection(configurationSection, "item"));
        List<Double> position = getDoubleList(configurationSection, "position", List.of(0d,0d,0d));
        Vector3 vector = new Vector3(position.get(0), position.get(1), position.get(2));
        return new ModelConfig(item, vector);
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
