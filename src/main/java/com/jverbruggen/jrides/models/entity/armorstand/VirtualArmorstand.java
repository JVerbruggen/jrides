package com.jverbruggen.jrides.models.entity.armorstand;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.BaseVirtualEntity;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.math.ArmorStandPose;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.packet.raw.ArmorstandRotationServerPacket;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;

import java.util.List;

public class VirtualArmorstand extends BaseVirtualEntity {
    private static final Vector3 HEAD_OFFSET = new Vector3(0, 1.7, 0);

    private double yawRotation;
    private final ArmorstandRotations rotations;
    private final ArmorstandModels models;
    private final boolean invisible;
    private final int leashedToEntity;

    public VirtualArmorstand(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, double yawRotation, int entityId) {
        super(packetSender, viewportManager, location, entityId);

        this.yawRotation = yawRotation;
        this.rotations = new ArmorstandRotations();
        this.models = new ArmorstandModels();
        this.invisible = true;
        this.leashedToEntity = -1;
    }

    @Override
    public Quaternion getRotation() {
        return Quaternion.fromYawPitchRoll(0, yawRotation, 0);
    }

    @Override
    public double getYaw() {
        return yawRotation;
    }

    public void setYaw(double yawRotation){
        this.yawRotation = yawRotation;
    }

    @Override
    public void spawnFor(Player player) {
        addViewer(player);
        packetSender.spawnVirtualArmorstand(player, entityId, location, yawRotation, rotations, models, invisible, leashedToEntity);

        if(models.hasHead()){
            this.packetSender.sendApplyModelPacket(viewers, entityId, EnumWrappers.ItemSlot.HEAD, models.getHead());
        }

        if(getPassenger() != null){
            Bukkit.getScheduler().runTaskLater(JRidesPlugin.getBukkitPlugin(),
                    () -> packetSender.sendMountVirtualEntityPacket(List.of(player), getPassenger(), entityId), 1L);
        }
    }

    @Override
    public boolean shouldRenderFor(Player player) {
        return false;
    }

    protected void setHeadPose(Vector3 rotation) {
        rotations.setHead(rotation);
        packetSender.sendRotationPacket(viewers, entityId, ArmorstandRotationServerPacket.Type.HEAD, rotation);
    }

    public void setModel(TrainModelItem model){
        this.models.setHead(model);
        this.packetSender.sendApplyModelPacket(viewers, entityId, EnumWrappers.ItemSlot.HEAD, model);
    }

    @Override
    public void setLocation(Vector3 newLocation) {
        super.setLocation(newLocation);

        syncPassenger(Vector3.add(newLocation, getHeadOffset()));
    }

    @Override
    public void setRotation(Quaternion orientation) {
        setHeadPose(ArmorStandPose.getArmorStandPose(orientation));
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
        return HEAD_OFFSET;
    }
}
