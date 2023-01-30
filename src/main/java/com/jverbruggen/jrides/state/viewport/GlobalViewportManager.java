package com.jverbruggen.jrides.state.viewport;

import com.jverbruggen.jrides.models.entity.EntityIdFactory;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.render.GlobalViewport;
import com.jverbruggen.jrides.packets.PacketSender;

import java.util.HashMap;

public class GlobalViewportManager implements ViewportManager {
    private final GlobalViewport globalViewport;
    private final PacketSender packetSender;
    private final EntityIdFactory entityIdFactory;

    private final HashMap<Integer, VirtualEntity> entities;

    public GlobalViewportManager(GlobalViewport globalViewport, PacketSender packetSender, EntityIdFactory entityIdFactory) {
        this.globalViewport = globalViewport;
        this.packetSender = packetSender;
        this.entityIdFactory = entityIdFactory;
        this.entities = new HashMap<>();
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
    public void updateVisuals(Player player) {
        globalViewport.updateFor(player);
    }

    @Override
    public void updateForEntity(VirtualEntity virtualEntity) {
        globalViewport.updateEntityViewers(virtualEntity);
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location) {
        return spawnVirtualArmorstand(location, null);
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, double yawRotation) {
        return spawnVirtualArmorstand(location, yawRotation, null);
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, TrainModelItem model) {
        return spawnVirtualArmorstand(location, 0, model);
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, double yawRotation, TrainModelItem model) {
        int entityId = entityIdFactory.newId();
        VirtualArmorstand virtualArmorstand = new VirtualArmorstand(packetSender, this, location, yawRotation, entityId);
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
}
