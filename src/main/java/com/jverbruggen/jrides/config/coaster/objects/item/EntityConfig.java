package com.jverbruggen.jrides.config.coaster.objects.item;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import javax.annotation.Nullable;

public class EntityConfig {
    private final EntityType entityType;

    public EntityConfig(EntityType entityType) {
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public static EntityConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        if(configurationSection == null) return new EntityConfig(null);

        EntityType entityType = EntityType.valueOf(configurationSection.getString("type"));
        return new EntityConfig(entityType);
    }
}
