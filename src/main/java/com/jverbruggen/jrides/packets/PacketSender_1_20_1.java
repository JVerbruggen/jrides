package com.jverbruggen.jrides.packets;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.packet.v1_20.ArmorstandRotationServerPacket;
import com.jverbruggen.jrides.packets.packet.v1_20.EntityMetadataServerPacket;

import java.util.List;

public class PacketSender_1_20_1 extends PacketSender_1_19_2 {
    public PacketSender_1_20_1(boolean debugMode) {
        super(debugMode);
    }

    public void sendRotationPacket(Player player, int entityId, int rotationType, Vector3 rotation){
        sendDebugLog("sendRotationPacket (single) 1.20.1");

        new ArmorstandRotationServerPacket(
                protocolManager, entityId, rotationType, rotation
        ).send(player);
    }

    @Override
    public void sendRotationPacket(List<Player> players, int entityId, int rotationType, Vector3 rotation) {
        sendDebugLog("sendRotationPacket (multiple) 1.20.1");

        new ArmorstandRotationServerPacket(
                protocolManager, entityId, rotationType, rotation
        ).sendAll(players);
    }

    @Override
    public void sendEntityMetaDataPacket(Player player, int entityId, boolean invisible) {
        sendDebugLog("sendEntityMetaDataPacket (single) 1.20.1");

        new EntityMetadataServerPacket(
                protocolManager, entityId, invisible
        ).send(player);
    }
}
