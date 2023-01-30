package com.jverbruggen.jrides.models.entity.armorstand;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jverbruggen.jrides.models.entity.BaseVirtualEntity;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.packet.raw.ArmorstandRotationServerPacket;
import com.jverbruggen.jrides.state.viewport.ViewportManager;

import java.util.List;

public class VirtualArmorstand extends BaseVirtualEntity implements VirtualEntity {
    private Player passenger;
    private double yawRotation;
    private ArmorstandRotations rotations;
    private ArmorstandModels models;
    private boolean invisible;
    private int leashedToEntity;
    private boolean allowsPassengerValue;
    private Seat partOfSeat;

    private boolean passengerSyncCounterActive;
    private int passengerSyncCounter;

    public VirtualArmorstand(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, double yawRotation, int entityId) {
        super(packetSender, viewportManager, location, entityId);

        this.passenger = null;
        this.yawRotation = yawRotation;
        this.rotations = new ArmorstandRotations();
        this.models = new ArmorstandModels();
        this.invisible = true;
        this.leashedToEntity = -1;
        this.allowsPassengerValue = false;
        this.partOfSeat = null;

        this.passengerSyncCounterActive = false;
        this.passengerSyncCounter = 0;
    }

    @Override
    public Player getPassenger() {
        return passenger;
    }

    @Override
    public boolean allowsPassenger() {
        return allowsPassengerValue;
    }

    @Override
    public boolean hasPassenger() {
        return passenger != null;
    }

    @Override
    public void setPassenger(Player player) {
        this.passenger = player;

        packetSender.sendMountVirtualEntityPacket(viewers, player, entityId);

        if(player != null){
            this.passengerSyncCounterActive = true;
            this.passengerSyncCounter = 0;
        }else{
            this.passengerSyncCounterActive = false;
        }
    }

    @Override
    public void spawnFor(Player player) {
        addViewer(player);
        packetSender.spawnVirtualArmorstand(player, entityId, location, yawRotation, rotations, models, invisible, leashedToEntity);

        if(models.hasHead()){
            this.packetSender.sendApplyModelPacket(viewers, entityId, EnumWrappers.ItemSlot.HEAD, models.getHead());
        }

        if(passenger != null){
            packetSender.sendMountVirtualEntityPacket(List.of(player), passenger, entityId);
        }
    }

    @Override
    public boolean shouldRenderFor(Player player) {
        return false;
    }

    public void setHostSeat(Seat seat) {
        partOfSeat = seat;
        allowsPassengerValue = true;
    }

    public Seat getHostSeat() {
        return partOfSeat;
    }

    public void setHeadpose(Vector3 rotation) {
        rotations.setHead(rotation);
        packetSender.sendRotationPacket(viewers, entityId, ArmorstandRotationServerPacket.Type.HEAD, rotation);
    }

    public void setModel(TrainModelItem model){
        this.models.setHead(model);
        this.packetSender.sendApplyModelPacket(viewers, entityId, EnumWrappers.ItemSlot.HEAD, model);
    }

    @Override
    public void setLocation(Vector3 newLocation, double yawRotation) {
        super.setLocation(newLocation, yawRotation);

        if(passengerSyncCounterActive){
            if(passengerSyncCounter > 20){
                passengerSyncCounter = 0;

                this.passenger.setPositionWithoutTeleport(Vector3.add(newLocation, getHeadOffset()));
            }else passengerSyncCounter++;
        }
    }

    @Override
    protected void moveEntity(Vector3 delta, double yawRotation) {
        packetSender.moveVirtualArmorstand(this.getViewers(), entityId, delta, yawRotation);
    }

    @Override
    protected void teleportEntity(Vector3 newLocation) {
        packetSender.teleportVirtualEntity(this.getViewers(), entityId, newLocation);
    }

    public static final Vector3 getHeadOffset(){
        return new Vector3(0, 1.8, 0);
    }
}
