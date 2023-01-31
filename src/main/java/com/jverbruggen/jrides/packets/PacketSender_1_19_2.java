package com.jverbruggen.jrides.packets;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandRotations;
import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandModels;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.packet.raw.*;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

public class PacketSender_1_19_2 implements PacketSender {
    private final ProtocolManager protocolManager;
    private final JRidesLogger logger;

    public PacketSender_1_19_2() {
        this.logger = ServiceProvider.getSingleton(JRidesLogger.class);
        this.protocolManager = ServiceProvider.getSingleton(ProtocolManager.class);
    }

    private void sendLog(String msg){
        logger.info(LogType.PACKET, msg);
    }

    public void sendRotationPacket(Player player, int entityId, int rotationType, Vector3 rotation){
        new ArmorstandRotationServerPacket(
                protocolManager, entityId, rotationType, rotation
        ).send(player);
    }

    @Override
    public void sendRotationPacket(List<Player> players, int entityId, int rotationType, Vector3 rotation) {
        new ArmorstandRotationServerPacket(
                protocolManager, entityId, rotationType, rotation
        ).sendAll(players);
    }

    public void sendApplyModelPacket(Player player, int entityId, EnumWrappers.ItemSlot itemSlot, TrainModelItem model){
        if(model == null) return;

        new EntityEquipmentServerPacket(
                protocolManager, entityId, itemSlot, model
        ).send(player);
    }

    @Override
    public void sendApplyModelPacket(List<Player> players, int entityId, EnumWrappers.ItemSlot itemSlot, TrainModelItem model) {
        if(model == null) return;

        new EntityEquipmentServerPacket(
                protocolManager, entityId, itemSlot, model
        ).sendAll(players);
    }

    public void sendAttachLeashPacket(Player player, int entityId, int leashToEntityId){
        if(leashToEntityId == -1) return;

        new AttachEntityServerPacket(
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

        new SpawnArmorstandServerPacket(
                protocolManager, entityId, entityType, locationX, locationY, locationZ, yawRotation, uuid
        ).send(player);

        new EntityMetadataServerPacket(
                protocolManager, entityId, invisible
        ).send(player);

        sendApplyModelPacket(player, entityId, EnumWrappers.ItemSlot.HEAD, models.getHead());
        sendApplyModelPacket(player, entityId, EnumWrappers.ItemSlot.MAINHAND, models.getMainHand());
        sendApplyModelPacket(player, entityId, EnumWrappers.ItemSlot.OFFHAND, models.getOffHand());

        sendRotationPacket(player, entityId, ArmorstandRotationServerPacket.Type.HEAD, rotations.getHead());
        sendRotationPacket(player, entityId, ArmorstandRotationServerPacket.Type.BODY, rotations.getBody());
        sendRotationPacket(player, entityId, ArmorstandRotationServerPacket.Type.OFF_HAND, rotations.getOffHand());
        sendRotationPacket(player, entityId, ArmorstandRotationServerPacket.Type.MAIN_HAND, rotations.getMainHand());
        sendRotationPacket(player, entityId, ArmorstandRotationServerPacket.Type.LEFT_LEG, rotations.getLeftLeg());
        sendRotationPacket(player, entityId, ArmorstandRotationServerPacket.Type.RIGHT_LEG, rotations.getRightLeg());

        sendAttachLeashPacket(player, entityId, leashedToEntity);
    }

    @Override
    public void spawnVirtualArmorstand(List<Player> players, int entityId, Vector3 location, double yawRotation, ArmorstandRotations rotations, ArmorstandModels models, boolean invisible, int leashedToEntity) {
        for(Player player : players){
            spawnVirtualArmorstand(player, entityId, location, yawRotation, rotations, models, invisible, leashedToEntity);
        }
    }

    @Override
    public void spawnVirtualFallingBlock(Player player, int entityId, Vector3 location) {
        throw new RuntimeException("Not implemented");
    }

    public void moveVirtualArmorstand(Player player, int entityId, Vector3 location, double yawRotation){
        Vector vector = location.toBukkitVector();

        new ArmorstandMoveServerPacket(
                protocolManager, entityId, location, yawRotation
        ).send(player);
    }

    @Override
    public void moveVirtualArmorstand(List<Player> players, int entityId, Vector3 location, double yawRotation) {
        new ArmorstandMoveServerPacket(
                protocolManager, entityId, location, yawRotation
        ).sendAll(players);
    }

    public void destroyVirtualEntity(Player player, int entityId){
        new EntityDestroyServerPacket(
                protocolManager, entityId
        ).send(player);
    }

    @Override
    public void destroyVirtualEntity(List<Player> players, int entityId) {
        new EntityDestroyServerPacket(
                protocolManager, entityId
        ).sendAll(players);
    }

    public void teleportVirtualEntity(Player player, int entityId, Vector3 blockLocation){
        new EntityTeleportServerPacket(protocolManager, entityId, blockLocation).send(player);
    }

    @Override
    public void teleportVirtualEntity(List<Player> players, int entityId, Vector3 blockLocation) {
        new EntityTeleportServerPacket(protocolManager, entityId, blockLocation).sendAll(players);
    }

    @Override
    public void sendMountVirtualEntityPacket(List<Player> players, Player mounted, int entityId) {
        new EntityMountServerPacket(protocolManager, entityId, mounted).sendAll(players);
    }

    @Override
    public void sendClientPositionPacket(Player movedPlayer, Vector3 position) {
        new PlayerPositionServerPacket(protocolManager, position).send(movedPlayer);
    }
}
