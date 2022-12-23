package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;

import java.util.UUID;

public class VirtualArmorstand implements VirtualEntity {
    private UUID uuid;
    private PacketSender packetSender;
    private Player passenger;
    private Vector3 location;

    public VirtualArmorstand(PacketSender packetSender) {
        this.uuid = UUID.randomUUID();
        this.packetSender = packetSender;
        this.passenger = null;
        this.location = new Vector3(0,0,0);
    }

    @Override
    public String getUniqueIdentifier() {
        return uuid.toString();
    }

    @Override
    public Player getPassenger() {
        return passenger;
    }

    @Override
    public void setPassenger(Player player) {
        this.passenger = player;
    }

    @Override
    public Vector3 getLocation() {
        return location;
    }
}
