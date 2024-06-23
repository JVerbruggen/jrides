/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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
    private final Map<String, Map<RideCounterMapType, Integer>> linesPerType;

    public RideCounterMapFactory() {
        super(MAP_UPDATE_INTERVAL_TICKS);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.defaultBackgroundImage = null;
        this.linesPerType = new HashMap<>();
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

                // Check and set the line count for this map type
                if(!linesPerType.containsKey(rideIdentifier)) {
                    linesPerType.put(rideIdentifier, new HashMap<>());
                }
                Map<RideCounterMapType, Integer> typeMap = linesPerType.get(rideIdentifier);
                if(typeMap.containsKey(rideCounterMapConfig.getRideCounterMapType())) {
                    Integer typeCount = typeMap.get(rideCounterMapConfig.getRideCounterMapType());
                    if(rideCounterMapConfig.getLines().size() > typeCount) {
                        linesPerType.get(rideIdentifier).put(rideCounterMapConfig.getRideCounterMapType(), rideCounterMapConfig.getLines().size());
                    }
                }else {
                    linesPerType.get(rideIdentifier).put(rideCounterMapConfig.getRideCounterMapType(), rideCounterMapConfig.getLines().size());
                }

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

    public Integer getLineCount(String rideIdentifier, RideCounterMapType rideCounterMapType) {
        if(!linesPerType.containsKey(rideIdentifier)) return 0;
        if(!linesPerType.get(rideIdentifier).containsKey(rideCounterMapType)) return 0;
        return linesPerType.get(rideIdentifier).get(rideCounterMapType);
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
