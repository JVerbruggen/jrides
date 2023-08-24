package com.jverbruggen.jrides.models.map.ridecounter;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.AbstractRideHandle;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.config.ride.RideCounterMapConfig;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.map.AbstractMapFactory;
import com.jverbruggen.jrides.models.map.VirtualMap;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.Bukkit;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class RideCounterMapFactory extends AbstractMapFactory {
    private static final int MAP_UPDATE_INTERVAL_TICKS = 20;

    private final RideManager rideManager;
    private final LanguageFile languageFile;
    private BufferedImage defaultBackgroundImage;

    public RideCounterMapFactory() {
        super(MAP_UPDATE_INTERVAL_TICKS);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.defaultBackgroundImage = null;
    }

    public void loadMaps() {
        defaultBackgroundImage = loadDefaultBackgroundImage();
        for(RideHandle rideHandle : rideManager.getRideHandles()) {
            if(rideHandle instanceof AbstractRideHandle) {
                loadRideCountMap((AbstractRideHandle) rideHandle);
            }
        }
    }

    private BufferedImage loadDefaultBackgroundImage() {
        File file = new File(JRidesPlugin.getBukkitPlugin().getDataFolder() + "/default_ridecountermap.png");
        if(!file.exists()) {
            JRidesPlugin.getLogger().warning("Default ride counter map image not found! Using a blank background instead.");
        }else {
            try {
                return ImageIO.read(file);
            } catch (Exception e) {
                JRidesPlugin.getLogger().warning("Failed to load default ride counter map image! Using a blank background instead.");
            }
        }

        BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < 128; x++) {
            for(int y = 0; y < 128; y++) {
                image.setRGB(x, y, 0xFFFFFFFF);
            }
        }
        return image;
    }

    private void loadRideCountMap(AbstractRideHandle rideHandle) {
        if(!hasMaps()) throw new RuntimeException("Ride counter maps was still null while loading ride counter map");

        String rideIdentifier = rideHandle.getRide().getIdentifier();
        if(rideHandle.getRideCounterMapConfigs() == null || rideHandle.getRideCounterMapConfigs().getRideCounterMapConfigs() == null || rideHandle.getRideCounterMapConfigs().getRideCounterMapConfigs().isEmpty()) {
            JRidesPlugin.getLogger().warning("No ride counter map configuration found for " + rideIdentifier + ", so skipping");
            return;
        }
        for(RideCounterMapConfig rideCounterMapConfig : rideHandle.getRideCounterMapConfigs().getRideCounterMapConfigs().values()) {
            List<Integer> mapIds = rideCounterMapConfig.getMapIds();
            List<BufferedImage> backgroundImages = rideCounterMapConfig.getBackgroundImages();
            int bufferPixels = 2 * MinecraftFont.Font.getHeight() + 2;
            int mapIndex = 0;
            int ridecountIndex = 0;
            for(Integer mapId : mapIds) {
                if(mapId == -1) {
                    JRidesPlugin.getLogger().warning("No ride counter map id configured for " + rideIdentifier + ", so skipping");
                    continue;
                }

                BufferedImage backgroundImage = null;
                if(backgroundImages != null && backgroundImages.size() > mapIds.indexOf(mapId)) {
                    backgroundImage = backgroundImages.get(mapIds.indexOf(mapId));
                }
                if(backgroundImage == null) backgroundImage = defaultBackgroundImage;

                Map<Integer, Integer> mapLines = new HashMap<>();
                for(int i = 0; i < rideCounterMapConfig.getLines().size(); i++) {
                    int currentRangeMin = mapIndex * 128;
                    int currentRangeMax = currentRangeMin + 128;
                    int currentLine = rideCounterMapConfig.getLines().get(i);

                    if(currentLine >= currentRangeMin - bufferPixels && currentLine < currentRangeMin) {
                        int difference = currentRangeMin - currentLine;
                        mapLines.put(ridecountIndex - 1, -difference);
                        continue;
                    }

                    if(currentLine >= currentRangeMin && currentLine < currentRangeMax) {
                        mapLines.put(ridecountIndex++, currentLine - currentRangeMin);
                    }
                }

                Integer rideNameLine = rideCounterMapConfig.getRideNameLine();
                Integer typeLine = rideCounterMapConfig.getTypeLine();
                String typeText = rideCounterMapConfig.getTypeText();
                byte primaryColor = rideCounterMapConfig.getPrimaryColor();
                byte secondaryColor = rideCounterMapConfig.getSecondaryColor();
                byte tertiaryColor = rideCounterMapConfig.getTertiaryColor();

                // If this is the second map in a list, remove the ride name and type lines
                if(mapIndex > 0) {
                    rideNameLine = null;
                    typeLine = null;
                    typeText = null;
                }

                MapView mapView = Bukkit.getMap(mapId);
                if(mapView == null) {
                    JRidesPlugin.getLogger().warning("No map found for id " + mapId + ". first create the map and assign the ID to the map configuration afterwards");
                    continue;
                }

                mapView.getRenderers().forEach(mapView::removeRenderer);
                mapView.setLocked(true);
                mapView.setTrackingPosition(false);

                RideCounterMap map = new RideCounterMap(rideHandle, mapView, mapLines, rideCounterMapConfig.getLineFormat(), backgroundImage, rideNameLine, typeLine,
                        typeText, primaryColor, secondaryColor, tertiaryColor);
                addMap(String.format("%s %s_%s", rideIdentifier, rideCounterMapConfig.getRideCounterMapIdentifier(), mapIndex++), map);
            }
        }
    }

    public List<String> getBoardIdentifiersByRide(String rideIdentifier) {
        Map<String, VirtualMap> maps = getMaps();
        if(maps == null) return new ArrayList<>();
        return maps.keySet().stream()
                .filter(key -> key.startsWith(rideIdentifier))
                .map(key -> key.split(" ")[1])
                .toList();
    }

    public void giveMap(Player player, RideHandle rideHandle, String rideCounterMapIdentifier) {
        if(!hasMaps()) {
            languageFile.sendMessage(player, LanguageFileField.NOTIFICATION_PLUGIN_STILL_LOADING);
            return;
        }

        String rideIdentifier = rideHandle.getRide().getIdentifier();
        VirtualMap map = getMap(String.format("%s %s", rideIdentifier, rideCounterMapIdentifier));
        if(map == null){
            languageFile.sendMessage(player, LanguageFileField.ERROR_RIDE_OVERVIEW_MAP_NOT_FOUND,
                    builder -> builder.add(LanguageFileTag.rideIdentifier, rideIdentifier));
            return;
        }

        map.give(player);
    }

}
