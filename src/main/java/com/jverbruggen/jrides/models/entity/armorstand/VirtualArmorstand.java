package com.jverbruggen.jrides.models.entity.armorstand;

import com.jverbruggen.jrides.models.entity.BaseVirtualEntity;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.packets.PacketSender;

public class VirtualArmorstand extends BaseVirtualEntity implements VirtualEntity {
    private Player passenger;
    private double yawRotation;
    private ArmorstandRotations rotations;
    private boolean invisible;
    private int leashedToEntity;

    public VirtualArmorstand(PacketSender packetSender, int entityId) {
        super(packetSender, entityId);

        this.passenger = null;
        this.yawRotation = 0d;
        this.rotations = new ArmorstandRotations();
        this.invisible = false;
        this.leashedToEntity = -1;
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
        packetSender.spawnVirtualArmorstand(player, entityId, location, yawRotation, rotations, invisible, leashedToEntity);
    }
}
