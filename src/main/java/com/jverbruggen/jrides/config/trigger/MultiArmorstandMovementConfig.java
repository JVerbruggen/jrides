package com.jverbruggen.jrides.config.trigger;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MultiArmorstandMovementConfig extends BaseTriggerConfig{
    private final List<ArmorstandMovementConfig> armorstands;

    public MultiArmorstandMovementConfig(List<ArmorstandMovementConfig> armorstands) {
        super(TriggerType.MULTI_ARMORSTAND_MOVEMENT);
        this.armorstands = armorstands;
    }

    public List<ArmorstandMovementConfig> getArmorstands() {
        return armorstands;
    }

    public static MultiArmorstandMovementConfig fromConfigurationSection(ConfigurationSection configurationSection){
        ConfigurationSection armorstandsSection = configurationSection.getConfigurationSection("armorstands");
        Set<String> armorstandsIdentifiers = armorstandsSection.getKeys(false);

        List<ArmorstandMovementConfig> armorstands = new ArrayList<>();
        for(String identifier : armorstandsIdentifiers){
            ArmorstandMovementConfig movementConfig = ArmorstandMovementConfig.fromConfigurationSection(identifier, armorstandsSection.getConfigurationSection(identifier));
            armorstands.add(movementConfig);
        }

        return new MultiArmorstandMovementConfig(armorstands);
    }
}
