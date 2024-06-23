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
import com.jverbruggen.jrides.models.entity.*;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.entity.armorstand.YawRotatedVirtualArmorstand;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.render.GlobalViewport;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.object.VirtualArmorstandConfiguration;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.List;

public class GlobalViewportManager implements ViewportManager {
    private final GlobalViewport globalViewport;
    private final PacketSender packetSender;
    private final EntityIdFactory entityIdFactory;
    private final HashMap<Integer, VirtualEntity> entities;

    private final int renderDistance;
    private final int renderChunkSize;

    public GlobalViewportManager(GlobalViewport globalViewport, PacketSender packetSender, EntityIdFactory entityIdFactory,
                                 int renderDistance, int renderChunkSize) {
        this.globalViewport = globalViewport;
        this.packetSender = packetSender;
        this.entityIdFactory = entityIdFactory;
        this.entities = new HashMap<>();

        this.renderDistance = renderDistance;
        this.renderChunkSize = renderChunkSize;
    }

    private void addEntity(VirtualEntity entity){
        entities.put(entity.getEntityId(), entity);
    }

    private void removeEntity(int entityId){
        entities.remove(entityId);
    }

    public VirtualEntity getEntity(int entityId){
        return entities.get(entityId);
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
        trainHandle.getTrain().despawn();
        flushDeadEntities();
    }

    @Override
    public void removeEntities(List<TrainHandle> trainHandles) {
        trainHandles.forEach(t -> t.getTrain().despawn());
        flushDeadEntities();
    }

    @Override
    public void updateVisuals(Player player) {
        updateVisuals(player, player.getLocation());
    }

    @Override
    public void updateVisuals(Player player, Vector3 playerLocation) {
        globalViewport.updateFor(player, playerLocation);
    }

    @Override
    public void updateForEntity(VirtualEntity virtualEntity) {
        globalViewport.updateEntityViewers(virtualEntity);
    }

    @Override
    public VirtualEntity spawnModelEntity(Vector3 location, TrainModelItem headModel) {
        return spawnVirtualArmorstand(location, new Quaternion(), headModel, VirtualArmorstandConfiguration.createDefault());
    }

    @Override
    public VirtualEntity spawnModelEntity(Vector3 location, Quaternion rotation, TrainModelItem headModel, String customName) {
        return spawnVirtualArmorstand(location, rotation, headModel, VirtualArmorstandConfiguration.createWithName(customName));
    }

    @Override
    public VirtualEntity spawnVirtualEntity(Vector3 location, EntityType entityType) {
        return spawnVirtualEntity(location, entityType, 0);
    }

    @Override
    public VirtualEntity spawnVirtualEntity(Vector3 location, EntityType entityType, double yawRotation) {
        int entityId = entityIdFactory.newId();
        VirtualEntity virtualEntity = new VirtualBukkitEntity(packetSender, this, location, entityType, yawRotation, entityId);

        addEntity(virtualEntity);

        updateForEntity(virtualEntity);
        return virtualEntity;
    }

    @Override
    public VirtualArmorstand spawnSeatEntity(Vector3 location, double yawRotation, TrainModelItem model){
        int entityId = entityIdFactory.newId();
        Quaternion rotation = new Quaternion();
        VirtualArmorstand virtualArmorstand = new YawRotatedVirtualArmorstand(packetSender, this, location, rotation, yawRotation, entityId, VirtualArmorstandConfiguration.createDefault());
        if(model != null){
            virtualArmorstand.setModel(model);
        }

        addEntity(virtualArmorstand);

        updateForEntity(virtualArmorstand);
        return virtualArmorstand;
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location) {
        return spawnVirtualArmorstand(location, new Quaternion(), null, VirtualArmorstandConfiguration.createDefault());
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, double yawRotation) {
        return spawnVirtualArmorstand(location, yawRotation);
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, Quaternion rotation, TrainModelItem model, VirtualArmorstandConfiguration configuration) {
        return spawnVirtualArmorstand(location, rotation, 0, model, configuration);
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, Quaternion rotation, double yawRotation, TrainModelItem model, VirtualArmorstandConfiguration configuration) {
        int entityId = entityIdFactory.newId();
        VirtualArmorstand virtualArmorstand = new VirtualArmorstand(packetSender, this, location, rotation, yawRotation, entityId, configuration);
        if(model != null){
            virtualArmorstand.setModel(model);
        }

        addEntity(virtualArmorstand);

        updateForEntity(virtualArmorstand);
        return virtualArmorstand;
    }

    @Override
    public void despawnAll() {
        for(VirtualEntity virtualEntity : globalViewport.getEntities()){
            removeEntity(virtualEntity.getEntityId());
            virtualEntity.despawn();
        }
    }

    private void flushDeadEntities(){
        globalViewport.flushDeadEntities();
    }
}
