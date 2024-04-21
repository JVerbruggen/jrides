package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.state.viewport.ViewportManager;

public class VirtualFallingBlock extends BaseVirtualEntity {
    public VirtualFallingBlock(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, int entityId) {
        super(packetSender, viewportManager, location, entityId);
    }

    @Override
    protected void moveEntity(Vector3 delta, double yawRotation) {

    }

    @Override
    protected void teleportEntity(Vector3 newLocation) {

    }

    @Override
    public Player getPassenger() {
        return null;
    }

    @Override
    public boolean allowsPassenger() {
        return false;
    }

    @Override
    public boolean hasPassenger() {
        return false;
    }

    @Override
    public void setPassenger(Player player) {
        return;
    }

    @Override
    public Quaternion getRotation() {
        return new Quaternion();
    }

    @Override
    public double getYaw() {
        return 0;
    }

    @Override
    public void spawnFor(Player player) {
        addViewer(player);

        if(!rendered)
            return;

        return;
    }

    @Override
    public boolean shouldRenderFor(Player player) {
        return false;
    }

    @Override
    public void setModel(TrainModelItem model) {
        throw new RuntimeException("Not implemented");
    }
}
