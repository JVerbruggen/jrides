package com.jverbruggen.jrides.models.render;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class VirtualEntityViewport implements Viewport{
    protected List<VirtualEntity> virtualEntities;
    protected List<Player> viewers;

    public VirtualEntityViewport() {
        this.virtualEntities = new ArrayList<>();
        this.viewers = new ArrayList<>();
    }

    @Override
    public List<VirtualEntity> getEntities() {
        return virtualEntities;
    }

    @Override
    public List<Player> getViewers() {
        return viewers;
    }

    @Override
    public void addViewer(Player player) {
        if(hasViewer(player)) return;
        viewers.add(player);

        for(VirtualEntity virtualEntity : virtualEntities){
            if(virtualEntity.isViewer(player)) continue;
            virtualEntity.spawnFor(player);
        }
    }

    @Override
    public void removeViewer(Player player) {
        if(!hasViewer(player)) return;
        viewers.remove(player);

        for(VirtualEntity virtualEntity : virtualEntities){
            if(!virtualEntity.isViewer(player)) continue; // TODO: Same as this::removeEntity TODO
            virtualEntity.despawnFor(player);
        }
    }

    @Override
    public boolean hasViewer(Player player) {
        return viewers.contains(player);
    }

    @Override
    public void addEntity(VirtualEntity virtualEntity) {
        if(hasEntity(virtualEntity)) return;

        virtualEntities.add(virtualEntity);
        virtualEntity.spawnForAll(viewers);
    }

    @Override
    public void removeEntity(VirtualEntity virtualEntity) {
        if(!hasEntity(virtualEntity)) return;

        virtualEntities.remove(virtualEntity);
        //TODO: Should it be despawned? What if player and viewport are in a different viewport as well?
    }

    @Override
    public boolean hasEntity(VirtualEntity virtualEntity) {
        return virtualEntities.contains(virtualEntity);
    }
}
