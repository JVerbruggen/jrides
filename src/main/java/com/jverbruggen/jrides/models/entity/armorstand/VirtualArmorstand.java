package com.jverbruggen.jrides.models.entity.armorstand;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jverbruggen.jrides.models.entity.BaseVirtualEntity;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.packet.raw.ArmorstandRotationPacket;
import com.jverbruggen.jrides.state.viewport.ViewportManager;

public class VirtualArmorstand extends BaseVirtualEntity implements VirtualEntity {
    private Player passenger;
    private double yawRotation;
    private ArmorstandRotations rotations;
    private ArmorstandModels models;
    private boolean invisible;
    private int leashedToEntity;

    public VirtualArmorstand(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, int entityId) {
        super(packetSender, viewportManager, location, entityId);

        this.passenger = null;
        this.yawRotation = 0d;
        this.rotations = new ArmorstandRotations();
        this.models = new ArmorstandModels();
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
        addViewer(player);
        packetSender.spawnVirtualArmorstand(player, entityId, location, yawRotation, rotations, models, invisible, leashedToEntity);

        if(models.hasHead()){
            this.packetSender.sendApplyModelPacket(viewers, entityId, EnumWrappers.ItemSlot.HEAD, models.getHead());
        }
    }

    public void setHeadpose(Vector3 rotation) {
        packetSender.sendRotationPacket(viewers, entityId, ArmorstandRotationPacket.Type.HEAD, rotation);
    }

    public void setModel(TrainModelItem model){
        this.models.setHead(model);
        this.packetSender.sendApplyModelPacket(viewers, entityId, EnumWrappers.ItemSlot.HEAD, model);
    }
}
