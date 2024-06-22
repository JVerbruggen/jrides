package com.jverbruggen.jrides.config.coaster.objects.item;

import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public interface ItemConfig {
    VirtualEntity spawnEntity(ViewportManager viewportManager, Vector3 spawnPosition, Quaternion spawnRotation, String customName);

    static ItemConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection){
        if(configurationSection == null) return null;

        if(configurationSection.contains("item"))
            return ItemStackConfig.fromConfigurationSection(configurationSection.getConfigurationSection("item"));
        else if(configurationSection.contains("entity"))
            return EntityConfig.fromConfigurationSection(configurationSection.getConfigurationSection("entity"));
        else throw new RuntimeException("Model config needs an item or an entity");
    }
}
