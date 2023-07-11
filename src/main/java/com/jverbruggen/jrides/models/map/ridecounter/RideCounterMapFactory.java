package com.jverbruggen.jrides.models.map.ridecounter;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.AbstractRideHandle;
import com.jverbruggen.jrides.animator.RideHandle;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RideCounterMapFactory {
    private @Nullable Map<String, RideCounterMap> rideCounterMaps;
    private final PlayerManager playerManager;
    private final RideManager rideManager;
    private final LanguageFile languageFile;

    public RideCounterMapFactory(){
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        rideCounterMaps = null;
    }

    public void initializeMaps() {
        rideCounterMaps = new HashMap<>();

        for(RideHandle rideHandle : rideManager.getRideHandles()) {
            if(rideHandle instanceof AbstractRideHandle) {
                loadRideCountMap((AbstractRideHandle) rideHandle);
            }
        }
    }

    private void loadRideCountMap(AbstractRideHandle rideHandle) {
        String rideIdentifier = rideHandle.getRide().getIdentifier();
        if(rideHandle.getRideCounterMapConfigs() == null || rideHandle.getRideCounterMapConfigs().getRideCounterMapConfigs() == null || rideHandle.getRideCounterMapConfigs().getRideCounterMapConfigs().isEmpty()) {
            JRidesPlugin.getLogger().warning("No ride counter map configuration found for " + rideIdentifier + ", so skipping");
            return;
        }
        for(RideCounterMapConfig rideCounterMapConfig : rideHandle.getRideCounterMapConfigs().getRideCounterMapConfigs().values()) {
            List<Integer> mapIds = rideCounterMapConfig.getMapIds();
            for(Integer mapId : mapIds) {
                if(mapId == -1) {
                    JRidesPlugin.getLogger().warning("No ride counter map id configured for " + rideIdentifier + ", so skipping");
                    continue;
                }

                MapView mapView = Bukkit.getMap(mapId);
                if(mapView == null) {
                    JRidesPlugin.getLogger().warning("No map found for id " + mapId + ". first create the map and assign the ID to the map configuration afterwards");
                    continue;
                }

                mapView.getRenderers().forEach(mapView::removeRenderer);
                mapView.setLocked(true);
                mapView.setTrackingPosition(false);

                RideCounterMap map = new RideCounterMap(rideHandle, mapView);
                rideCounterMaps.put(String.format("%s %s", rideIdentifier, rideCounterMapConfig.getRideCounterMapIdentifier()), map);
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
