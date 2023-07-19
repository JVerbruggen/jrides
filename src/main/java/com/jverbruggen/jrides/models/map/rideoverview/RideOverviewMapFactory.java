package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.map.AbstractMapFactory;
import com.jverbruggen.jrides.models.map.MapCreationException;
import com.jverbruggen.jrides.models.map.VirtualMap;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.map.MapView;

import java.util.List;

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
                loadRideOverviewMap((CoasterHandle) rideHandle);
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

    private void loadRideOverviewMap(CoasterHandle coasterHandle){
        if(!hasMaps()) throw new RuntimeException("Ride overview maps was still null while loading overview map");

        String rideIdentifier = coasterHandle.getRide().getIdentifier();
        int mapId = coasterHandle.getRideOverviewMapId();

        MapView mapView;
        try {
            mapView = createMapView(mapId);
        } catch (MapCreationException e) {
            JRidesPlugin.getLogger().warning("An error occured while loading ride overview map for " + rideIdentifier + ": " + e.getMessage());
            return;
        }

        MapScope mapScope = new MapScope();

        List<SectionVisual> sectionVisuals = sectionVisualFactory.createVisuals(coasterHandle, mapScope);
        List<TrainVisual> trainVisuals = trainVisualFactory.createVisuals(coasterHandle, mapScope);

        VirtualMap map = new RideOverviewMap(mapView, sectionVisuals, trainVisuals);
        addMap(rideIdentifier, map);
    }
}
