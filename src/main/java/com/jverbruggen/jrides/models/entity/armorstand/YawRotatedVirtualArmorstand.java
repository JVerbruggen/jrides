package com.jverbruggen.jrides.models.entity.armorstand;

import com.jverbruggen.jrides.models.math.ArmorStandPose;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.state.viewport.ViewportManager;

public class YawRotatedVirtualArmorstand extends VirtualArmorstand {
    public YawRotatedVirtualArmorstand(PacketSender packetSender, ViewportManager viewportManager, Vector3 location, double yawRotation, int entityId) {
        super(packetSender, viewportManager, location, yawRotation, entityId);
    }

    @Override
    public void setRotation(Quaternion orientation) {
        Vector3 headPose = ArmorStandPose.getArmorStandPose(orientation);
        headPose.y = 0;

        setHeadpose(headPose);
        setYaw(orientation.getPacketYaw());
        moveEntity(Vector3.zero(), getYaw());
    }
}
