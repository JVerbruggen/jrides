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

package com.jverbruggen.jrides.models.render;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            virtualEntity.despawnFor(player, true);
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
    public void updateFor(Player player, Vector3 playerLocation) {
        if(!hasViewer(player)) addViewer(player);

        for(VirtualEntity virtualEntity : virtualEntities){
            renderLogic(virtualEntity, player, playerLocation);
        }
    }

    @Override
    public void updateEntityViewers(VirtualEntity virtualEntity) {
        if(!hasEntity(virtualEntity)) addEntity(virtualEntity);

        for(Player player : viewers){
            renderLogic(virtualEntity, player, player.getLocation());
        }
    }

    @Override
    public void flushDeadEntities() {
        virtualEntities = getEntities().stream()
                .filter(VirtualEntity::isAlive)
                .collect(Collectors.toList());
    }

    private void renderLogic(VirtualEntity virtualEntity, Player player, Vector3 playerLocation){
        double distanceSquared = virtualEntity.getLocation().distanceSquared(playerLocation);
        if(player.isViewing(virtualEntity)){
            if(distanceSquared > maxRenderDistance*maxRenderDistance){
                virtualEntity.despawnFor(player, true);
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
