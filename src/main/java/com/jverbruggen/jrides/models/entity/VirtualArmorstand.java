package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.packets.PacketSender;

public class VirtualArmorstand extends BaseVirtualEntity implements VirtualEntity {
    private Player passenger;
    private double yawRotation;

    public VirtualArmorstand(PacketSender packetSender) {
        super(packetSender);

        this.passenger = null;
        this.yawRotation = 0d;
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
    public void spawnFor(Player player) {
        packetSender.sendSpawnVirtualEntityPacket(player, location, yawRotation);
    }
}
