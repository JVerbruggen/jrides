package com.jverbruggen.jrides.config;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.flatride.FlatRideConfig;
import com.jverbruggen.jrides.config.ride.RidesConfig;
import com.jverbruggen.jrides.config.trigger.TriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerConfigFactory;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ConfigManager {
    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private File getFile(String fileName){
        File rootFolder = plugin.getDataFolder();
        File file = new File(rootFolder, fileName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public YamlConfiguration getYamlConfiguration(String fileName){
        File file = getFile(fileName);

        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            return null;
        }
        return configuration;
    }

    public YamlConfiguration getOrCreateConfiguration(String fileName){
        YamlConfiguration found = getYamlConfiguration(fileName);
        if(found != null) return found;

        found = new YamlConfiguration();
        File file = getFile(fileName);
        try {
            file.createNewFile();
            found.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            JRidesPlugin.getLogger().severe("Could not create configuration file with name '" + fileName + "'!!");
        }

        return found;
    }

    public String getPluginName() {
        return this.plugin.getDescription().getName();
    }

    private String prepareConfigString(String configString) {
        int lastLine = 0;
        int headerLine = 0;

        String[] lines = configString.split("\n");
        StringBuilder config = new StringBuilder("");
        String[] arrayOfString1;
        int j = (arrayOfString1 = lines).length;
        for (int i = 0; i < j; i++) {
            String line = arrayOfString1[i];
            if (line.startsWith(getPluginName() + "_COMMENT")) {
                String comment = "#" + line.trim().substring(line.indexOf(":") + 1);
                if (comment.startsWith("# +-")) {
                    if (headerLine == 0) {
                        config.append(comment + "\n");
                        lastLine = 0;
                        headerLine = 1;
                    } else if (headerLine == 1) {
                        config.append(comment + "\n\n");
                        lastLine = 0;
                        headerLine = 0;
                    }
                } else {
                    String normalComment;
                    if (comment.startsWith("# ' ")) {
                        normalComment =

                                comment.substring(0, comment.length() - 1).replaceFirst("# ' ", "# ");
                    } else {
                        normalComment = comment;
                    }
                    if (lastLine == 0) {
                        config.append(normalComment + "\n");
                    } else if (lastLine == 1) {
                        config.append("\n" + normalComment + "\n");
                    }
                    lastLine = 0;
                }
            } else {
                config.append(line + "\n");
                lastLine = 1;
            }
        }
        return config.toString();
    }

    public void saveConfig(String configString, File file) {
        String configuration = prepareConfigString(configString);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(configuration);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig(YamlConfiguration configuration, String fileName){
        File file = getFile(fileName);
        saveConfig(configuration.saveToString(), file);
    }

    public String getCoasterFolder(String rideIdentifier){
        return "coasters/" + rideIdentifier;
    }

    public String getFlatrideFolder(String rideIdentifier){
        return "flatrides/" + rideIdentifier;
    }

    public String getTriggerFolder(String rideIdentifier){
        return getCoasterFolder(rideIdentifier) + "/triggers";
    }

    public ConfigurationSection getAllEffectsConfigSection(String rideIdentifier, String trackIdentifier){
        String fileName = getCoasterFolder(rideIdentifier) + "/" + rideIdentifier + "." + trackIdentifier + ".trigger.yml";
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

        TriggerConfigFactory triggerConfigFactory = ServiceProvider.getSingleton(TriggerConfigFactory.class);
        return triggerConfigFactory.fromConfigurationSection(rideIdentifier, effectName, yamlConfiguration.getConfigurationSection("trigger"));
    }

    public CoasterConfig getCoasterConfig(String rideIdentifier){
        String fileName = getCoasterFolder(rideIdentifier) + "/" + rideIdentifier + ".coaster.yml";
        YamlConfiguration yamlConfiguration = getYamlConfiguration(fileName);
        if(yamlConfiguration == null){
            JRidesPlugin.getLogger().severe(rideIdentifier + ".coaster.yml config not found for " + rideIdentifier);
            return null;
        }

        return CoasterConfig.fromConfigurationSection(yamlConfiguration.getConfigurationSection("config"));
    }

    public FlatRideConfig getFlatRideConfig(String rideIdentifier){
        String fileName = getFlatrideFolder(rideIdentifier) + "/" + rideIdentifier + ".flatride.yml";
        YamlConfiguration yamlConfiguration = getYamlConfiguration(fileName);
        if(yamlConfiguration == null){
            JRidesPlugin.getLogger().severe(rideIdentifier + ".flatride.yml config not found for " + rideIdentifier);
            return null;
        }

        return FlatRideConfig.fromConfigurationSection(yamlConfiguration.getConfigurationSection("config"));
    }

    public RidesConfig getRideConfig(){
        String fileName = "rides.yml";
        YamlConfiguration yamlConfiguration = getYamlConfiguration(fileName);
        if(yamlConfiguration == null) {
            JRidesPlugin.getLogger().severe("rides.yml config not found");
            return null;
        }

        return RidesConfig.fromConfigurationSection(yamlConfiguration.getConfigurationSection("config"));
    }

    public void updateConfigFile(String configFile, String yamlRootKey, Object object){
        YamlConfiguration configuration = getOrCreateConfiguration(configFile);

        configuration.set(yamlRootKey, object);

        saveConfig(configuration, configFile);
    }

    public <T> Optional<T> getConfigFileObject(String configFile, String yamlRootKey, Class<T> clazz){
        YamlConfiguration configuration = getOrCreateConfiguration(configFile);

        if(!configuration.contains(yamlRootKey))
            return Optional.empty();

        Object tObject = configuration.get(yamlRootKey);
        T t;
        try{
            t = clazz.cast(tObject);
        }catch(ClassCastException exception){
            throw new RuntimeException("Config file " + configFile + " did not contain object of type " + clazz.getTypeName());
        }

        assert t != null;
        return Optional.of(t);
    }

    public Map<String, String> getLanguageFile() {
        YamlConfiguration yamlConfiguration = getYamlConfiguration("language.yml");
        if(yamlConfiguration == null) return null;

        ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("language");
        Set<String> keys = configurationSection.getKeys(false);
        Map<String, String> result = new HashMap<>();

        for(String key : keys){
            result.put(key, configurationSection.getString(key));
        }

        return result;
    }
}
