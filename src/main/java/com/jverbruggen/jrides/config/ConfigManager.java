package com.jverbruggen.jrides.config;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.ride.RideConfig;
import com.jverbruggen.jrides.config.trigger.TriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerConfigFactory;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final JavaPlugin plugin;
    private final TriggerConfigFactory triggerConfigFactory;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.triggerConfigFactory = ServiceProvider.getSingleton(TriggerConfigFactory.class);
    }

    public YamlConfiguration getYamlConfiguration(String fileName){
        File rootFolder = plugin.getDataFolder();
        File file = new File(rootFolder, fileName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }

        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            return null;
        }
        return configuration;
    }

    public String getFolder(String rideIdentifier){
        return "coasters/" + rideIdentifier;
    }

    public String getTriggerFolder(String rideIdentifier){
        return getFolder(rideIdentifier) + "/triggers";
    }

    public ConfigurationSection getAllEffectsConfigSection(String rideIdentifier, String trackIdentifier){
        String fileName = getFolder(rideIdentifier) + "/" + rideIdentifier + "." + trackIdentifier + ".trigger.yml";
        YamlConfiguration yamlConfiguration = getYamlConfiguration(fileName);
        if(yamlConfiguration == null){
            JRidesPlugin.getLogger().warning("No trigger file for ride " + rideIdentifier);
            return null;
        }

        return yamlConfiguration.getConfigurationSection("triggers");
    }

    public TriggerConfig getTriggerConfig(String rideIdentifier, String effectName){
        String fileName = getTriggerFolder(rideIdentifier) + "/" + effectName + ".yml";
        YamlConfiguration yamlConfiguration = getYamlConfiguration(fileName);
        if(yamlConfiguration == null){
            JRidesPlugin.getLogger().severe("Trigger " + effectName + " not found for ride " + rideIdentifier);
            return null;
        }

        return triggerConfigFactory.fromConfigurationSection(rideIdentifier, effectName, yamlConfiguration.getConfigurationSection("trigger"));
    }

    public CoasterConfig getCoasterConfig(String rideIdentifier){
        String fileName = getFolder(rideIdentifier) + "/" + rideIdentifier + ".coaster.yml";
        YamlConfiguration yamlConfiguration = getYamlConfiguration(fileName);
        if(yamlConfiguration == null){
            JRidesPlugin.getLogger().severe(rideIdentifier + ".coaster.yml config not found for " + rideIdentifier);
            return null;
        }

        return CoasterConfig.fromConfigurationSection(yamlConfiguration.getConfigurationSection("config"));
    }

    public RideConfig getRideConfig(){
        String fileName = "rides.yml";
        YamlConfiguration yamlConfiguration = getYamlConfiguration(fileName);
        if(yamlConfiguration == null) {
            JRidesPlugin.getLogger().severe("rides.yml config not found");
            return null;
        }

        return RideConfig.fromConfigurationSection(yamlConfiguration.getConfigurationSection("config"));
    }
}
