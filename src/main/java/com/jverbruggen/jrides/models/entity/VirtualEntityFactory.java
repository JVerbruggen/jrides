package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.packets.PacketSender;

public class VirtualEntityFactory {
    private PacketSender packetSender;

    public VirtualEntityFactory(PacketSender packetSender) {
        this.packetSender = packetSender;
    }

    public VirtualArmorstand spawnVirtualArmorstand(){
        VirtualArmorstand virtualArmorstand = new VirtualArmorstand(packetSender);
        return virtualArmorstand;
    }

}
