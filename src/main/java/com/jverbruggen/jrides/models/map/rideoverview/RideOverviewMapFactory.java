package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.RideHandle;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideOverviewMapFactory {
    private static final long MAP_UPDATE_INTERVAL_TICKS = 1;

    private final SectionVisualFactory sectionVisualFactory;
    private final TrainVisualFactory trainVisualFactory;
    private @Nullable Map<String, RideOverviewMap> rideOverviewMaps;
    private final PlayerManager playerManager;
    private final RideManager rideManager;
    private final LanguageFile languageFile;

    public RideOverviewMapFactory(){
        sectionVisualFactory = ServiceProvider.getSingleton(SectionVisualFactory.class);
        trainVisualFactory = ServiceProvider.getSingleton(TrainVisualFactory.class);
        playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        rideManager = ServiceProvider.getSingleton(RideManager.class);
        languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        rideOverviewMaps = null;
    }

    public void initializeMaps(){
        rideOverviewMaps = new HashMap<>();

        for(RideHandle rideHandle : rideManager.getRideHandles()){
            if(rideHandle instanceof CoasterHandle)
                loadCoasterOverviewMap((CoasterHandle) rideHandle);
        }

        startUpdateCycle();
    }

    private void loadCoasterOverviewMap(CoasterHandle coasterHandle){
        String rideIdentifier = coasterHandle.getRide().getIdentifier();
        int mapId = coasterHandle.getRideOverviewMapId();
        if(mapId == -1){
            JRidesPlugin.getLogger().warning("No ride overview map id configured for " + rideIdentifier + ", so skipping");
            return;
        }

        MapView mapView = Bukkit.getMap(mapId);
        if(mapView == null){
            JRidesPlugin.getLogger().severe("Configured ride overview map id for ride " + rideIdentifier + " did not exist, first create the map and assign the ID to the coaster afterwards");
            return;
        }
        mapView.getRenderers().forEach(mapView::removeRenderer);
        mapView.setLocked(true);
        mapView.setTrackingPosition(false);

        MapScope mapScope = new MapScope();

        List<SectionVisual> sectionVisuals = sectionVisualFactory.createVisuals(coasterHandle, mapScope);
        List<TrainVisual> trainVisuals = trainVisualFactory.createVisuals(coasterHandle, mapScope);

        RideOverviewMap map = new RideOverviewMap(coasterHandle, mapView, sectionVisuals, trainVisuals);
        rideOverviewMaps.put(rideIdentifier, map);
    }

    private void startUpdateCycle(){
        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(),
            () -> {
                if(rideOverviewMaps == null) return;
                Collection<Player> players = playerManager.getPlayers();

                rideOverviewMaps.values()
                        .forEach(m -> {
                            m.updateVisuals();
                            m.sendUpdate(players);
                        });
            }, MAP_UPDATE_INTERVAL_TICKS, MAP_UPDATE_INTERVAL_TICKS);
    }

    public void giveMap(Player player, CoasterHandle coasterHandle){
        if(rideOverviewMaps == null){
            languageFile.sendMessage(player, LanguageFileField.NOTIFICATION_PLUGIN_STILL_LOADING);
            return;
        }

        String rideIdentifier = coasterHandle.getRide().getIdentifier();
        RideOverviewMap map = rideOverviewMaps.get(rideIdentifier);
        if(map == null){
            languageFile.sendMessage(player, LanguageFileField.ERROR_RIDE_OVERVIEW_MAP_NOT_FOUND,
                    builder -> builder.add(LanguageFileTag.rideIdentifier, rideIdentifier));
            return;
        }

        map.give(player);
    }
}
