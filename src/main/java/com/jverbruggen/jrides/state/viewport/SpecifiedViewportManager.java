package com.jverbruggen.jrides.state.viewport;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.render.Viewport;

import java.util.ArrayList;
import java.util.List;

public class SpecifiedViewportManager implements ViewportManager{
    private List<Viewport> viewports;
    private final int renderDistance;
    private final int renderChunkSize;

    public SpecifiedViewportManager(int renderDistance, int renderChunkSize) {
        this.viewports = new ArrayList<>();
        this.renderDistance = renderDistance;
        this.renderChunkSize = renderChunkSize;
    }

    @Override
    public void updateVisuals(Player player) {
        updateVisuals(player, player.getLocation());
    }

    @Override
    public void updateVisuals(Player player, Vector3 playerLocation){
    }

    public void updateForEntity(VirtualEntity virtualEntity){
        Vector3 location = virtualEntity.getLocation();
        for(Viewport viewport : viewports){
            boolean isInViewport = viewport.hasEntity(virtualEntity);
            boolean shouldBeInViewport = viewport.isInViewport(location);

            if(shouldBeInViewport && !isInViewport){
                viewport.addEntity(virtualEntity);
            }else if(!shouldBeInViewport && isInViewport){
                viewport.removeEntity(virtualEntity);
            }
        }
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location) {
        return null;
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, double yawRotation) {
        return null;
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, TrainModelItem headModel) {
        return null;
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, double yawRotation, TrainModelItem headModel) {
        return null;
    }

    @Override
    public void despawnAll() {

    }

    @Override
    public VirtualEntity getEntity(int entityId) {
        return null;
    }

    @Override
    public int getRenderChunkSize() {
        return renderChunkSize;
    }

    @Override
    public int getRenderDistance() {
        return renderDistance;
    }

    @Override
    public void removeEntities(TrainHandle trainHandle) {

    }

    @Override
    public void removeEntities(List<TrainHandle> trainHandles) {

    }
}
