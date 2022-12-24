package com.jverbruggen.jrides.packets;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandRotations;
import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandModels;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.packet.raw.*;
import org.bukkit.util.Vector;

import java.util.UUID;

public class PacketSender_1_19_2 implements PacketSender {
    private final ProtocolManager protocolManager;

    public PacketSender_1_19_2(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    private void sendRotationPacket(Player player, int entityId, int rotationType, Vector3 rotation){
        new ArmorstandRotationPacket(
                protocolManager, entityId, rotationType, rotation
        ).send(player);
    }

    private void sendApplyModelPacket(Player player, int entityId, EnumWrappers.ItemSlot itemSlot, TrainModelItem model){
        if(model == null) return;

        new EntityEquipmentPacket(
                protocolManager, entityId, itemSlot, model
        ).send(player);
    }

    private void sendAttachLeashPacket(Player player, int entityId, int leashToEntityId){
        if(leashToEntityId == -1) return;

        new AttachEntityPacket(
                protocolManager, entityId, leashToEntityId
        ).send(player);
    }

    @Override
    public void spawnVirtualArmorstand(Player player, int entityId, Vector3 location, double yawRotation, ArmorstandRotations rotations, ArmorstandModels models, boolean invisible, int leashedToEntity) {
        int entityType = 2;
        double locationX = location.getX();
        double locationY = location.getY();
        double locationZ = location.getZ();
        UUID uuid = UUID.randomUUID();

        new SpawnArmorstandPacket(
                protocolManager, entityId, entityType, locationX, locationY, locationZ, yawRotation, uuid
        ).send(player);
        new EntityMetadataPacket(
                protocolManager, entityId, invisible
        ).send(player);

        sendApplyModelPacket(player, entityId, EnumWrappers.ItemSlot.HEAD, models.getHead());
        sendApplyModelPacket(player, entityId, EnumWrappers.ItemSlot.MAINHAND, models.getMainHand());
        sendApplyModelPacket(player, entityId, EnumWrappers.ItemSlot.OFFHAND, models.getOffHand());

        sendRotationPacket(player, entityId, ArmorstandRotationPacket.Type.HEAD, rotations.getHead());
        sendRotationPacket(player, entityId, ArmorstandRotationPacket.Type.BODY, rotations.getBody());
        sendRotationPacket(player, entityId, ArmorstandRotationPacket.Type.OFF_HAND, rotations.getOffHand());
        sendRotationPacket(player, entityId, ArmorstandRotationPacket.Type.MAIN_HAND, rotations.getMainHand());
        sendRotationPacket(player, entityId, ArmorstandRotationPacket.Type.LEFT_LEG, rotations.getLeftLeg());
        sendRotationPacket(player, entityId, ArmorstandRotationPacket.Type.RIGHT_LEG, rotations.getRightLeg());

        sendAttachLeashPacket(player, entityId, leashedToEntity);
    }

    public void moveVirtualArmorstand(Player player, int entityId, Vector3 location, double yawRotation){
        Vector vector = location.toBukkitVector();

        new ArmorstandMovePacket(
                protocolManager, entityId, location, yawRotation
        ).send(player);
    }

    public void destroyVirtualEntity(Player player, int entityId){
        new EntityDestroyPacket(
                protocolManager, entityId
        ).send(player);
    }

    public void teleportVirtualEntity(Player player, int entityId, Vector3 location){
        new EntityTeleportPacket(entityId, location).send(player);
    }
}
