package com.jverbruggen.jrides.models.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.map.MapView;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;

public abstract class AbstractMapFactory {
    private final PlayerManager playerManager;
    private final int mapUpdateIntervalTicks;
    private @Nullable Map<String, VirtualMap> maps;

    public AbstractMapFactory(int mapUpdateIntervalTicks){
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        this.mapUpdateIntervalTicks = mapUpdateIntervalTicks;
        this.maps = null;
    }

    public void initializeMaps(){
        this.maps = new HashMap<>();

        loadMaps();

        startUpdateCycle();
    }

    private void startUpdateCycle(){
        Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(),
            () -> {
                if(!hasMaps()) return;
                Collection<Player> players = playerManager.getPlayers();

                maps.values()
                        .forEach(m -> {
                            m.updateVisuals();
                            m.sendUpdate(players);
                    });
                
            }, this.mapUpdateIntervalTicks, this.mapUpdateIntervalTicks);
    }

    public void addMap(String identifier, VirtualMap map){
        if(!hasMaps()) return;
        maps.put(identifier, map);
    }

    public VirtualMap getMap(String identifier){
        if(!hasMaps()) return null;
        return maps.get(identifier);
    }

    public abstract void loadMaps();

    protected boolean hasMaps(){
        return this.maps != null;
    }

    protected MapView createMapView(int mapId) throws MapCreationException {
        if(mapId == -1){
            throw new MapCreationException("No ride overview map id configured");
        }

        @SuppressWarnings("deprecation")
        MapView mapView = Bukkit.getMap(mapId);
        if(mapView == null){
            throw new MapCreationException("Configured ride overview map id did not exist, first create the map and assign the ID to the coaster afterwards");
        }
        
        mapView.getRenderers().forEach(mapView::removeRenderer);
        mapView.setLocked(true);
        mapView.setTrackingPosition(false);
        return mapView;
    }
}
