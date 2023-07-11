package com.jverbruggen.jrides.models.map.ridecounter;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.AbstractRideHandle;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.ride.RideCounterMapConfig;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.Bukkit;
import org.bukkit.map.MapView;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RideCounterMapFactory {
    private @Nullable Map<String, RideCounterMap> rideCounterMaps;
    private final PlayerManager playerManager;
    private final RideManager rideManager;
    private final LanguageFile languageFile;
    private final ConfigManager configManager;
    private BufferedImage defaultBackgroundImage;

    public RideCounterMapFactory(){
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
        this.defaultBackgroundImage = null;
        rideCounterMaps = null;
    }

    public void initializeMaps() {
        rideCounterMaps = new HashMap<>();
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
        String rideIdentifier = rideHandle.getRide().getIdentifier();
        if(rideHandle.getRideCounterMapConfigs() == null || rideHandle.getRideCounterMapConfigs().getRideCounterMapConfigs() == null || rideHandle.getRideCounterMapConfigs().getRideCounterMapConfigs().isEmpty()) {
            JRidesPlugin.getLogger().warning("No ride counter map configuration found for " + rideIdentifier + ", so skipping");
            return;
        }
        for(RideCounterMapConfig rideCounterMapConfig : rideHandle.getRideCounterMapConfigs().getRideCounterMapConfigs().values()) {
            List<Integer> mapIds = rideCounterMapConfig.getMapIds();
            List<BufferedImage> backgroundImages = rideCounterMapConfig.getBackgroundImages();
            int mapIndex = 0;
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

                MapView mapView = Bukkit.getMap(mapId);
                if(mapView == null) {
                    JRidesPlugin.getLogger().warning("No map found for id " + mapId + ". first create the map and assign the ID to the map configuration afterwards");
                    continue;
                }

                mapView.getRenderers().forEach(mapView::removeRenderer);
                mapView.setLocked(true);
                mapView.setTrackingPosition(false);

                RideCounterMap map = new RideCounterMap(rideHandle, mapView, backgroundImage);
                rideCounterMaps.put(String.format("%s %s_%s", rideIdentifier, rideCounterMapConfig.getRideCounterMapIdentifier(), mapIndex++), map);
            }
        }
    }

    public void updateMapsByRide(String rideIdentifier) {
        assert rideCounterMaps != null;
        rideCounterMaps.keySet().stream()
                .filter(key -> key.startsWith(rideIdentifier))
                .forEach(key -> {
                    RideCounterMap rideCounterMap = rideCounterMaps.get(key);
                    if (rideCounterMap == null) return;
                    rideCounterMap.updateVisuals();
                    rideCounterMap.sendUpdate(ServiceProvider.getSingleton(PlayerManager.class).getPlayers());
                });
    }

    public List<String> getBoardIdentifiersByRide(String rideIdentifier) {
        assert rideCounterMaps != null;
        return rideCounterMaps.keySet().stream()
                .filter(key -> key.startsWith(rideIdentifier))
                .map(key -> key.split(" ")[1])
                .toList();
    }

    public void giveMap(Player player, RideHandle rideHandle, String rideCounterMapIdentifier) {
        if(rideCounterMaps == null) {
            languageFile.sendMessage(player, LanguageFileField.NOTIFICATION_PLUGIN_STILL_LOADING);
            return;
        }

        String rideIdentifier = rideHandle.getRide().getIdentifier();
        RideCounterMap rideCounterMap = rideCounterMaps.get(String.format("%s %s", rideIdentifier, rideCounterMapIdentifier));
        if(rideCounterMap == null){
            languageFile.sendMessage(player, LanguageFileField.ERROR_RIDE_OVERVIEW_MAP_NOT_FOUND,
                    builder -> builder.add(LanguageFileTag.rideIdentifier, rideIdentifier));
            return;
        }

        rideCounterMap.give(player);
    }
}
