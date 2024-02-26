package com.jverbruggen.jrides.config.ride;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.map.ridecounter.RideCounterMapType;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import dev.cerus.maps.api.graphics.ColorCache;
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
    private final Integer rideNameLine;
    private final Integer typeLine;
    private final String typeText;
    private final byte primaryColor;
    private final byte secondaryColor;
    private final byte tertiaryColor;

    public RideCounterMapConfig(String rideCounterMapIdentifier, RideCounterMapType rideCounterMapType, List<Integer> lines, List<Integer> mapIds,
                                List<BufferedImage> backgroundImages, String lineFormat, Integer rideNameLine, Integer typeLine, String typeText,
                                byte primaryColor, byte secondaryColor, byte tertiaryColor) {
        this.rideCounterMapIdentifier = rideCounterMapIdentifier;
        this.rideCounterMapType = rideCounterMapType;
        this.lines = lines;
        this.mapIds = mapIds;
        this.backgroundImages = backgroundImages;
        this.lineFormat = lineFormat;
        this.rideNameLine = rideNameLine;
        this.typeLine = typeLine;
        this.typeText = typeText;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.tertiaryColor = tertiaryColor;
    }

    public RideCounterMapConfig() {
        this(null, null, null, null, null, null, null, null, null, ColorCache.rgbToMap(0, 0, 0), ColorCache.rgbToMap(0, 0, 0), ColorCache.rgbToMap(0, 0, 0));
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

    public Integer getRideNameLine() {
        return rideNameLine;
    }

    public Integer getTypeLine() {
        return typeLine;
    }

    public String getTypeText() {
        return typeText;
    }

    public byte getPrimaryColor() {
        return primaryColor;
    }

    public byte getSecondaryColor() {
        return secondaryColor;
    }

    public byte getTertiaryColor() {
        return tertiaryColor;
    }

    private static byte convertListToColorByte(List<Integer> colorList) {
        if(colorList == null || colorList.size() != 3) return ColorCache.rgbToMap(0, 0, 0);
        return ColorCache.rgbToMap(colorList.get(0), colorList.get(1), colorList.get(2));
    }

    public static RideCounterMapConfig fromConfigurationSection(String rideIdentifier, ConfigurationSection configurationSection) {
        if(configurationSection == null) return new RideCounterMapConfig();

        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);

        String rideCounterMapIdentifier = configurationSection.getName();
        RideCounterMapType rideCounterMapType = RideCounterMapType.valueOf(getString(configurationSection, "type", "top").toUpperCase());
        List<Integer> lines = getIntegerList(configurationSection, "lines", new ArrayList<>());
        List<Integer> mapIds = getIntegerList(configurationSection, "mapIds", new ArrayList<>());
        String lineFormat = getString(configurationSection, "lineFormat", "%RANK%. %NAME%: %COUNT%");
        Integer rideNameLine = getInt(configurationSection, "rideNameLine", -1);
        Integer typeLine = getInt(configurationSection, "typeLine", -1);

        if(lines.isEmpty()) {
            JRidesPlugin.getLogger().warning("No lines configured for " + rideIdentifier + " ridecounter map " + rideCounterMapIdentifier + ", the map will not be generated");
        }

        if(mapIds.isEmpty()) {
            JRidesPlugin.getLogger().warning("No map ids configured for " + rideIdentifier + " ridecounter map " + rideCounterMapIdentifier + ", the map will not be generated");
        }

        String typeText = rideCounterMapType.toString().substring(0, 1).toUpperCase() + rideCounterMapType.toString().substring(1).toLowerCase() + " ridecounters";
        typeText = getString(configurationSection, "typeText", typeText);

        byte primaryColor = convertListToColorByte(getIntegerList(configurationSection, "primaryColor", null));
        byte secondaryColor = convertListToColorByte(getIntegerList(configurationSection, "secondaryColor", null));
        byte tertiaryColor = convertListToColorByte(getIntegerList(configurationSection, "tertiaryColor", null));

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

        return new RideCounterMapConfig(rideCounterMapIdentifier, rideCounterMapType, lines, mapIds, backgroundImages, lineFormat, rideNameLine, typeLine, typeText,
                primaryColor, secondaryColor, tertiaryColor);
    }
}
