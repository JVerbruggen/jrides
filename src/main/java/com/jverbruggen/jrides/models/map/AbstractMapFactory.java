package com.jverbruggen.jrides.models.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;

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
}
