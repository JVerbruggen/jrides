package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.RideHandle;
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

import java.util.List;

import javax.annotation.Nullable;

public class RideOverviewMapFactory extends AbstractMapFactory {
    private static final int MAP_UPDATE_INTERVAL_TICKS = 1;

    private final SectionVisualFactory sectionVisualFactory;
    private final TrainVisualFactory trainVisualFactory;
    private final RideManager rideManager;
    private final LanguageFile languageFile;

    public RideOverviewMapFactory(){
        super(MAP_UPDATE_INTERVAL_TICKS);
        sectionVisualFactory = ServiceProvider.getSingleton(SectionVisualFactory.class);
        trainVisualFactory = ServiceProvider.getSingleton(TrainVisualFactory.class);
        rideManager = ServiceProvider.getSingleton(RideManager.class);
        languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    public void loadMaps(){
        for(RideHandle rideHandle : rideManager.getRideHandles()){
            if(rideHandle instanceof CoasterHandle)
                loadCoasterOverviewMap((CoasterHandle) rideHandle);
        }
    }

    public void giveMap(Player player, CoasterHandle coasterHandle){
        if(!hasMaps()){
            languageFile.sendMessage(player, LanguageFileField.NOTIFICATION_PLUGIN_STILL_LOADING);
            return;
        }

        String rideIdentifier = coasterHandle.getRide().getIdentifier();
        VirtualMap map = getMap(rideIdentifier);
        if(map == null){
            languageFile.sendMessage(player, LanguageFileField.ERROR_RIDE_OVERVIEW_MAP_NOT_FOUND,
                    builder -> builder.add(LanguageFileTag.rideIdentifier, rideIdentifier));
            return;
        }

        map.give(player);
    }

    private void loadCoasterOverviewMap(CoasterHandle coasterHandle){
        if(!hasMaps()) throw new RuntimeException("Ride overview maps was still null while loading overview map");

        String rideIdentifier = coasterHandle.getRide().getIdentifier();
        int mapId = coasterHandle.getRideOverviewMapId();

        MapView mapView = createMapView(rideIdentifier, mapId);
        if(mapView == null) return;

        MapScope mapScope = new MapScope();

        List<SectionVisual> sectionVisuals = sectionVisualFactory.createVisuals(coasterHandle, mapScope);
        List<TrainVisual> trainVisuals = trainVisualFactory.createVisuals(coasterHandle, mapScope);

        RideOverviewMap map = new RideOverviewMap(mapView, sectionVisuals, trainVisuals);
        addMap(rideIdentifier, map);
    }

    private @Nullable MapView createMapView(String rideIdentifier, int mapId){
        if(mapId == -1){
            JRidesPlugin.getLogger().warning("No ride overview map id configured for " + rideIdentifier + ", so skipping");
            return null;
        }

        @SuppressWarnings("deprecation")
        MapView mapView = Bukkit.getMap(mapId);
        if(mapView == null){
            JRidesPlugin.getLogger().severe("Configured ride overview map id for ride " + rideIdentifier + " did not exist, first create the map and assign the ID to the coaster afterwards");
            return null;
        }
        
        mapView.getRenderers().forEach(mapView::removeRenderer);
        mapView.setLocked(true);
        mapView.setTrackingPosition(false);
        return mapView;
    }
}
