package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.packets.PacketSender;

public class VirtualEntityFactory {
    private final PacketSender packetSender;
    private final EntityIdFactory entityIdFactory;

    public VirtualEntityFactory(PacketSender packetSender, EntityIdFactory entityIdFactory) {
        this.packetSender = packetSender;
        this.entityIdFactory = entityIdFactory;
    }

    public VirtualArmorstand spawnVirtualArmorstand(){
        int entityId = entityIdFactory.newId();
        VirtualArmorstand virtualArmorstand = new VirtualArmorstand(packetSender, entityId);
        return virtualArmorstand;
    }

}
