package com.jverbruggen.jrides.models.entity.display;

import com.jverbruggen.jrides.models.entity.BaseVirtualEntity;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.object.VirtualArmorstandConfiguration;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;

import javax.annotation.Nonnull;

public class VirtualDisplayEntity extends BaseVirtualEntity {

    private final Quaternion currentRotation;
    private final VirtualArmorstandConfiguration configuration;

    public VirtualDisplayEntity(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, Quaternion rotation, int entityId, @Nonnull VirtualArmorstandConfiguration configuration) {
        super(packetSender, viewportManager, location, entityId);

        this.currentRotation = rotation;
        this.configuration = configuration;
    }

    @Override
    protected void moveEntity(Vector3 delta, double yawRotation) {
        packetSender.moveVirtualArmorstand(this.getViewers(), entityId, delta, yawRotation);
    }

    @Override
    protected void teleportEntity(Vector3 newLocation) {
        packetSender.teleportVirtualEntity(this.getViewers(), entityId, newLocation);
    }

    @Override
    public Quaternion getRotation() {
        return currentRotation;
    }

    @Override
    public double getYaw() {
        return 0;
    }

    @Override
    public void setRotation(Quaternion orientation) {
        if(orientation == null) return;

        currentRotation.copyFrom(orientation);
        this.packetSender.sendItemDisplayRotationPacket(viewers, entityId, currentRotation, 3);
    }

    @Override
    public void spawnFor(Player player) {
        addViewer(player);
        if(!rendered) return;
        packetSender.spawnVirtualEntity(player, entityId, location, 0, EntityType.ITEM_DISPLAY, false, -1);

        this.packetSender.sendItemDisplayMetaDataPacket(player, entityId, 3);

        if(configuration.models().hasHead()){
            this.packetSender.sendApplyItemDisplayModelPacket(player, entityId, ItemDisplay.ItemDisplayTransform.HEAD, configuration.models().getHead());
        }
    }

    @Override
    public boolean shouldRenderFor(Player player) {
        return false;
    }

    @Override
    public void setModel(TrainModelItem model) {
        this.configuration.models().setHead(model);
        this.packetSender.sendApplyItemDisplayModelPacket(viewers, entityId, ItemDisplay.ItemDisplayTransform.HEAD, model);
    }

    @Override
    public void setLocation(Vector3 newLocation) {
        super.setLocation(newLocation);

        if(newLocation == null) return;

        syncPassenger(newLocation);
    }
}
