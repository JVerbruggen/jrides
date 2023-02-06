package com.jverbruggen.jrides.models.entity.armorstand;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.BaseVirtualEntity;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.packet.raw.ArmorstandRotationServerPacket;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;

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
        this.invisible = false;
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
    public double getYaw() {
        return yawRotation;
    }

    @Override
    public void spawnFor(Player player) {
        addViewer(player);
        packetSender.spawnVirtualArmorstand(player, entityId, location, yawRotation, rotations, models, invisible, leashedToEntity);

        if(models.hasHead()){
            this.packetSender.sendApplyModelPacket(viewers, entityId, EnumWrappers.ItemSlot.HEAD, models.getHead());
        }

        if(passenger != null){
            Bukkit.getScheduler().runTaskLater(JRidesPlugin.getBukkitPlugin(),
                    () -> packetSender.sendMountVirtualEntityPacket(List.of(player), passenger, entityId), 1L);
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
    public void setLocation(Vector3 newLocation, Quaternion orientation) {
        super.setLocation(newLocation, orientation);

        if(orientation != null)
            this.yawRotation = orientation.getYaw() - 90;

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

    public static Vector3 getHeadOffset(){
        return new Vector3(0, 1.8, 0);
    }
}
