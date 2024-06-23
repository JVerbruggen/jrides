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

    public Map<String, VirtualMap> getMaps() {
        if(!hasMaps()) return null;
        return maps;
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
