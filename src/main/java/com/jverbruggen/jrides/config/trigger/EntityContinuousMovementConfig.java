package com.jverbruggen.jrides.config.trigger;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class EntityContinuousMovementConfig extends BaseConfig {
    private final ItemConfig itemConfig;

    private final Vector3 locationDelta;
    private final Vector3 rotationDelta;
    private final int animationTimeTicks;

    public EntityContinuousMovementConfig(ItemConfig itemConfig, Vector3 locationDelta, Vector3 rotationDelta, int animationTimeTicks) {
        this.itemConfig = itemConfig;
        this.locationDelta = locationDelta;
        this.rotationDelta = rotationDelta;
        this.animationTimeTicks = animationTimeTicks;
    }

    public static EntityContinuousMovementConfig fromConfigurationSection(ConfigurationSection configurationSection){
        ItemConfig itemConfig = ItemConfig.fromConfigurationSection(configurationSection.getConfigurationSection("item"));
        Vector3 locationDelta = Vector3.fromDoubleList(getDoubleList(configurationSection, "locationDelta", null));
        Vector3 rotationDelta = Vector3.fromDoubleList(getDoubleList(configurationSection, "rotationDelta", null));
        int animationTimeTicks = getInt(configurationSection, "animationTimeTicks");

        return new EntityContinuousMovementConfig(itemConfig, locationDelta, rotationDelta, animationTimeTicks);
    }
}
