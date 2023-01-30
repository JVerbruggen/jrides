package com.jverbruggen.jrides.models.render;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public abstract class VirtualEntityViewport implements Viewport{
    protected List<VirtualEntity> virtualEntities;
    protected List<Player> viewers;
    private final int maxRenderDistance;

    public VirtualEntityViewport(int maxRenderDistance) {
        this.virtualEntities = new ArrayList<>();
        this.viewers = new ArrayList<>();
        this.maxRenderDistance = maxRenderDistance;
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
        updateEntityViewers(virtualEntity);
//        virtualEntity.spawnForAll(viewers);
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

    @Override
    public void updateFor(Player player) {
        if(!hasViewer(player)) addViewer(player);

        for(VirtualEntity virtualEntity : virtualEntities){
            renderLogic(virtualEntity, player);
        }
    }

    @Override
    public void updateEntityViewers(VirtualEntity virtualEntity) {
        if(!hasEntity(virtualEntity)) addEntity(virtualEntity);

        for(Player player : viewers){
            renderLogic(virtualEntity, player);
        }
    }

    private void renderLogic(VirtualEntity virtualEntity, Player player){
        double distanceSquared = virtualEntity.getLocation().distanceSquared(player.getLocation());
        if(player.isViewing(virtualEntity)){
            if(distanceSquared > maxRenderDistance*maxRenderDistance){

                virtualEntity.despawnFor(player);
                player.removeViewing(virtualEntity);
            }
        }
        else{
            if(distanceSquared <= maxRenderDistance*maxRenderDistance){
                virtualEntity.spawnFor(player);
                player.addViewing(virtualEntity);
            }
        }
    }
}
