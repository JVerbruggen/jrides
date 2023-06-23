package com.jverbruggen.jrides.config.flatride.structure;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.flatride.structure.rotor.RotorConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StructureConfig extends BaseConfig {
    private List<StructureConfigItem> items;

    public StructureConfig(List<StructureConfigItem> items) {
        this.items = items;
    }

    public List<StructureConfigItem> getItems() {
        return items;
    }

    public static StructureConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        List<StructureConfigItem> items = new ArrayList<>();

        Set<String> keys = configurationSection.getKeys(false);
        for(String key : keys){
            ConfigurationSection itemConfigurationSection = getConfigurationSection(configurationSection, key);
            String type = getString(itemConfigurationSection, "type");

            StructureConfigItem structureConfigItem;
            switch(type){
                case "rotor":
                    structureConfigItem = RotorConfig.fromConfigurationSection(itemConfigurationSection, key);
                    break;
                default:
                    throw new RuntimeException("Unknown structure type '" + type + "'");
            }

            items.add(structureConfigItem);
        }

        return new StructureConfig(items);
    }
}
