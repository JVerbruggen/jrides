package com.jverbruggen.jrides.config.ride;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.map.ridecounter.RideCounterMapType;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.ConfigurationSection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RideCounterMapConfig extends BaseConfig {
    private final String rideCounterMapIdentifier;
    private final RideCounterMapType rideCounterMapType;
    private final List<Integer> lines;
    private final List<Integer> mapIds;
    private final List<BufferedImage> backgroundImages;
    private final String lineFormat;

    public RideCounterMapConfig(String rideCounterMapIdentifier, RideCounterMapType rideCounterMapType, List<Integer> lines, List<Integer> mapIds, List<BufferedImage> backgroundImages, String lineFormat) {
        this.rideCounterMapIdentifier = rideCounterMapIdentifier;
        this.rideCounterMapType = rideCounterMapType;
        this.lines = lines;
        this.mapIds = mapIds;
        this.backgroundImages = backgroundImages;
        this.lineFormat = lineFormat;
    }

    public RideCounterMapConfig() {
        this(null, null, null, null, null, null);
    }

    public String getRideCounterMapIdentifier() {
        return rideCounterMapIdentifier;
    }

    public RideCounterMapType getRideCounterMapType() {
        return rideCounterMapType;
    }

    public List<Integer> getLines() {
        return lines;
    }

    public List<Integer> getMapIds() {
        return mapIds;
    }

    public List<BufferedImage> getBackgroundImages() {
        return backgroundImages;
    }

    public String getLineFormat() {
        return lineFormat;
    }

    public static RideCounterMapConfig fromConfigurationSection(String rideIdentifier, ConfigurationSection configurationSection) {
        if(configurationSection == null) return new RideCounterMapConfig();

        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);

        String rideCounterMapIdentifier = configurationSection.getName();
        RideCounterMapType rideCounterMapType = RideCounterMapType.valueOf(getString(configurationSection, "type", "").toUpperCase());
        List<Integer> lines = getIntegerList(configurationSection, "lines", null);
        List<Integer> mapIds = getIntegerList(configurationSection, "mapIds", null);
        String lineFormat = getString(configurationSection, "lineFormat", "%RANK%. %NAME%: %COUNT%");

        List<BufferedImage> backgroundImages = new ArrayList<>();

        for(String backgroundImage : getStringList(configurationSection, "backgroundImages", new ArrayList<>())) {
            String imagePath = JRidesPlugin.getBukkitPlugin().getDataFolder() + "/" + configManager.getCoasterFolder(rideIdentifier) + "/assets/" + backgroundImage;
            File file = new File(imagePath);
            if(!file.exists()) {
                JRidesPlugin.getLogger().warning("Background image for " + rideIdentifier + " at " + imagePath + " does not exist, using default background");
                backgroundImages.add(null);
                continue;
            }
            try {
                backgroundImages.add(ImageIO.read(file));
            } catch(IOException e) {
                JRidesPlugin.getLogger().warning("Background image for " + rideIdentifier + " at " + imagePath + " could not be read, using default background");
                backgroundImages.add(null);
            }
        }

        return new RideCounterMapConfig(rideCounterMapIdentifier, rideCounterMapType, lines, mapIds, backgroundImages, lineFormat);
    }
}
