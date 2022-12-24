package com.jverbruggen.jrides.config;

import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.ride.RideConfig;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public YamlConfiguration getYamlConfiguration(String fileName){
        File rootFolder = plugin.getDataFolder();
        File file = new File(rootFolder, fileName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }

        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return configuration;
    }

    public CoasterConfig getCoasterConfig(String identifier){
        String fileName = "coasters/" + identifier + ".coaster.yml";
        YamlConfiguration yamlConfiguration = getYamlConfiguration(fileName);
        CoasterConfig coasterConfig = (CoasterConfig) yamlConfiguration.get("config");

        return coasterConfig;
    }

    public RideConfig getRideConfig(){
        String fileName = "rides.yml";
        YamlConfiguration yamlConfiguration = getYamlConfiguration(fileName);
        RideConfig rideConfig = (RideConfig) yamlConfiguration.get("config");

        return rideConfig;
    }
}
