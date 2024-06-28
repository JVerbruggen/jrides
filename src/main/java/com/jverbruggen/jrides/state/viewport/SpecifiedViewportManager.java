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

package com.jverbruggen.jrides.state.viewport;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.render.Viewport;
import com.jverbruggen.jrides.packets.object.VirtualArmorstandConfiguration;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class SpecifiedViewportManager implements ViewportManager{
    private final List<Viewport> viewports;
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
    public VirtualEntity spawnModelEntity(Vector3 location, TrainModelItem trainModelItem) {
        return null;
    }

    @Override
    public VirtualEntity spawnModelEntity(Vector3 location, Quaternion rotation, TrainModelItem trainModelItem, String customName) {
        return null;
    }

    @Override
    public VirtualEntity spawnSeatEntity(Vector3 location, double yawRotation, TrainModelItem model) {
        return null;
    }

    @Override
    public VirtualEntity spawnVirtualBukkitEntity(Vector3 location, EntityType entityType) {
        return null;
    }

    @Override
    public VirtualEntity spawnVirtualBukkitEntity(Vector3 location, EntityType entityType, double yawRotation) {
        return null;
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
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, Quaternion rotation, TrainModelItem headModel, VirtualArmorstandConfiguration configuration) {
        return null;
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, Quaternion rotation, double yawRotation, TrainModelItem headModel, VirtualArmorstandConfiguration configuration) {
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
