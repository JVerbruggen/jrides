package com.jverbruggen.jrides.config.coaster.objects.item;

import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import javax.annotation.Nullable;

public class EntityConfig implements ItemConfig{
    private final EntityType entityType;

    public EntityConfig(EntityType entityType) {
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public VirtualEntity spawnEntity(ViewportManager viewportManager, Vector3 spawnPosition, Quaternion spawnRotation, String customName) {
        return viewportManager.spawnVirtualEntity(spawnPosition, getEntityType());
    }

    public static EntityConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        if(configurationSection == null) return new EntityConfig(null);

        EntityType entityType = EntityType.valueOf(configurationSection.getString("type"));
        return new EntityConfig(entityType);
    }
}
