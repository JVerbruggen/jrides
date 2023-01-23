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

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class PacketSender_1_19_2 implements PacketSender {
    private final ProtocolManager protocolManager;
    private final Logger logger;

    public PacketSender_1_19_2(Logger logger, ProtocolManager protocolManager) {
        this.logger = logger;
        this.protocolManager = protocolManager;
    }

    private void sendLog(String msg){
        logger.config(msg);
    }

    public void sendRotationPacket(Player player, int entityId, int rotationType, Vector3 rotation){
        new ArmorstandRotationServerPacket(
                protocolManager, entityId, rotationType, rotation
        ).send(player);

        sendLog("sendRotationPacket");
    }

    @Override
    public void sendRotationPacket(List<Player> players, int entityId, int rotationType, Vector3 rotation) {
        new ArmorstandRotationServerPacket(
                protocolManager, entityId, rotationType, rotation
        ).sendAll(players);

        sendLog("sendRotationPacket");
    }

    public void sendApplyModelPacket(Player player, int entityId, EnumWrappers.ItemSlot itemSlot, TrainModelItem model){
        if(model == null) return;

        new EntityEquipmentServerPacket(
                protocolManager, entityId, itemSlot, model
        ).send(player);

        sendLog("sendApplyModelPacket");
    }

    @Override
    public void sendApplyModelPacket(List<Player> players, int entityId, EnumWrappers.ItemSlot itemSlot, TrainModelItem model) {
        if(model == null) return;

        new EntityEquipmentServerPacket(
                protocolManager, entityId, itemSlot, model
        ).sendAll(players);

        sendLog("sendApplyModelPacket");
    }

    public void sendAttachLeashPacket(Player player, int entityId, int leashToEntityId){
        if(leashToEntityId == -1) return;

        new AttachEntityServerPacket(
                protocolManager, entityId, leashToEntityId
        ).send(player);

        sendLog("sendAttachLeashPacket");
    }

    @Override
    public void spawnVirtualArmorstand(Player player, int entityId, Vector3 location, double yawRotation, ArmorstandRotations rotations, ArmorstandModels models, boolean invisible, int leashedToEntity) {
        int entityType = 2;
        double locationX = location.getX();
        double locationY = location.getY();
        double locationZ = location.getZ();
        UUID uuid = UUID.randomUUID();

        sendLog("spawnVirtualArmorstand for " + player.getBukkitPlayer().getName() + " at " + location.toString());

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

        sendLog("spawnVirtualArmorstand");
    }

    public void moveVirtualArmorstand(Player player, int entityId, Vector3 location, double yawRotation){
        Vector vector = location.toBukkitVector();

        new ArmorstandMoveServerPacket(
                protocolManager, entityId, location, yawRotation
        ).send(player);

        sendLog("moveVirtualArmorstand");
    }

    @Override
    public void moveVirtualArmorstand(List<Player> players, int entityId, Vector3 location, double yawRotation) {
        new ArmorstandMoveServerPacket(
                protocolManager, entityId, location, yawRotation
        ).sendAll(players);

        sendLog("moveVirtualArmorstand " + location.toString() + " players:" + players.size());
    }

    public void destroyVirtualEntity(Player player, int entityId){
        new EntityDestroyServerPacket(
                protocolManager, entityId
        ).send(player);

        sendLog("destroyVirtualEntity");
    }

    @Override
    public void destroyVirtualEntity(List<Player> players, int entityId) {
        new EntityDestroyServerPacket(
                protocolManager, entityId
        ).sendAll(players);

        sendLog("destroyVirtualEntity");
    }

    public void teleportVirtualEntity(Player player, int entityId, Vector3 blockLocation){
        new EntityTeleportServerPacket(protocolManager, entityId, blockLocation).send(player);

        sendLog("teleportVirtualEntity");
    }

    @Override
    public void teleportVirtualEntity(List<Player> players, int entityId, Vector3 blockLocation) {
        new EntityTeleportServerPacket(protocolManager, entityId, blockLocation).sendAll(players);

        sendLog("teleportVirtualEntity");
    }

    @Override
    public void sendMountVirtualEntityPacket(List<Player> players, Player mounted, int entityId) {
        new EntityMountServerPacket(protocolManager, entityId, mounted).sendAll(players);

        sendLog("sendMountVirtualEntityPacket");
    }

    @Override
    public void sendClientPositionPacket(Player movedPlayer, Vector3 position) {
        new PlayerPositionServerPacket(protocolManager, position).send(movedPlayer);

//        movedPlayer.sendMessage("Sent server position packet to " + position.toString());

        sendLog("sendClientPositionPacket");
    }
}
