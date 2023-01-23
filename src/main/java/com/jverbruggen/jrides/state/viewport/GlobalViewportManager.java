package com.jverbruggen.jrides.state.viewport;

import com.jverbruggen.jrides.models.entity.EntityIdFactory;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.render.GlobalViewport;
import com.jverbruggen.jrides.packets.PacketSender;

public class GlobalViewportManager implements ViewportManager {
    private final GlobalViewport globalViewport;
    private final PacketSender packetSender;
    private final EntityIdFactory entityIdFactory;

    public GlobalViewportManager(GlobalViewport globalViewport, PacketSender packetSender, EntityIdFactory entityIdFactory) {
        this.globalViewport = globalViewport;
        this.packetSender = packetSender;
        this.entityIdFactory = entityIdFactory;
    }

    @Override
    public void updateVisuals(Player player) {
        globalViewport.addViewer(player);
    }

    @Override
    public void updateForEntity(VirtualEntity virtualEntity) {
        globalViewport.addEntity(virtualEntity);
    }

    @Override
    public VirtualArmorstand spawnVirtualArmorstand(Vector3 location, TrainModelItem model) {
        int entityId = entityIdFactory.newId();
        VirtualArmorstand virtualArmorstand = new VirtualArmorstand(packetSender, this, location, entityId);
        if(model != null){
            virtualArmorstand.setModel(model);
        }

        updateForEntity(virtualArmorstand);
        return virtualArmorstand;
    }

    @Override
    public void despawnAll() {
        for(VirtualEntity virtualEntity : globalViewport.getEntities()){
            virtualEntity.despawn();
        }
    }
}
