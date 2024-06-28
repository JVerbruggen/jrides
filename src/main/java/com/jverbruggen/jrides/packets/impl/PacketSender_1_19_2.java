/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.packets.impl;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandRotations;
import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandModels;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.object.VirtualArmorstandConfiguration;
import com.jverbruggen.jrides.packets.packet.v1_19.*;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.UUID;

public class PacketSender_1_19_2 implements PacketSender {
    private static final Vector3 ARMORSTAND_MODEL_COMPENSATION_1_19_2 = new Vector3(0, 1.8, 0);

    protected final ProtocolManager protocolManager;
    protected final JRidesLogger logger;
    protected final boolean debugMode;

    public PacketSender_1_19_2(boolean debugMode) {
        this.logger = ServiceProvider.getSingleton(JRidesLogger.class);
        this.protocolManager = ServiceProvider.getSingleton(ProtocolManager.class);
        this.debugMode = debugMode;
    }

    protected void sendDebugLog(String msg){
        if(debugMode)
            logger.warning(msg);
    }

    private void sendLog(String msg){
        logger.info(LogType.PACKET, msg);
    }

    public void sendRotationPacket(Player player, int entityId, int rotationType, Vector3 rotation){
        sendDebugLog("sendRotationPacket (single)");

        new ArmorstandRotationServerPacket(
                protocolManager, entityId, rotationType, rotation
        ).send(player);
    }

    @Override
    public void sendRotationPacket(List<Player> players, int entityId, int rotationType, Vector3 rotation) {
        sendDebugLog("sendRotationPacket (multiple)");

        new ArmorstandRotationServerPacket(
                protocolManager, entityId, rotationType, rotation
        ).sendAll(players);
    }

    public void sendApplyModelPacket(Player player, int entityId, EnumWrappers.ItemSlot itemSlot, TrainModelItem model){
        if(model == null) return;
        sendDebugLog("sendApplyModelPacket (single)");

        new EntityEquipmentServerPacket(
                protocolManager, entityId, itemSlot, model
        ).send(player);
    }

    @Override
    public void sendApplyModelPacket(List<Player> players, int entityId, EnumWrappers.ItemSlot itemSlot, TrainModelItem model) {
        if(model == null) return;
        sendDebugLog("sendApplyModelPacket (multiple");

        new EntityEquipmentServerPacket(
                protocolManager, entityId, itemSlot, model
        ).sendAll(players);
    }

    public void sendAttachLeashPacket(Player player, int entityId, int leashToEntityId){
        if(leashToEntityId == -1) return;
        sendDebugLog("sendAttachLeashPacket (single)");

        new AttachEntityServerPacket(
                protocolManager, entityId, leashToEntityId
        ).send(player);
    }

    @Override
    public String getIdentifier() {
        return "1.19.2";
    }

    @Override
    public void spawnVirtualEntity(Player player, int entityId, Vector3 location, double yawRotation, EntityType entityType, boolean invisible, int leashedToEntity) {
        sendDebugLog("spawnVirtualEntity (single)");

        double locationX = location.getX();
        double locationY = location.getY();
        double locationZ = location.getZ();
        UUID uuid = UUID.randomUUID();

        new SpawnBukkitEntityServerPacket(
                protocolManager, entityId, entityType, locationX, locationY, locationZ, yawRotation, uuid
        ).send(player);

        sendAttachLeashPacket(player, entityId, leashedToEntity);
    }

    @Override
    public void spawnVirtualArmorstand(Player player, int entityId, Vector3 location, double yawRotation, VirtualArmorstandConfiguration configuration) {
        sendDebugLog("spawnVirtualArmorstand (single)");

        double locationX = location.getX();
        double locationY = location.getY();
        double locationZ = location.getZ();
        UUID uuid = UUID.randomUUID();

        boolean invisible = configuration.invisible();
        ArmorstandModels models = configuration.models();
        ArmorstandRotations rotations = configuration.rotations();
        int leashedToEntity = configuration.leashedToEntity();
        String customName = configuration.customName();

        new SpawnArmorstandServerPacket(
                protocolManager, entityId, locationX, locationY, locationZ, yawRotation, uuid
        ).send(player);

        sendEntityMetaDataPacket(player, entityId, invisible, customName);

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
    public void spawnVirtualArmorstand(List<Player> players, int entityId, Vector3 location, double yawRotation, VirtualArmorstandConfiguration configuration) {
        sendDebugLog("spawnVirtualArmorstand (multiple)");

        for(Player player : players){
            spawnVirtualArmorstand(player, entityId, location, yawRotation, configuration);
        }
    }

    @Override
    public void spawnVirtualFallingBlock(Player player, int entityId, Vector3 location) {
        sendDebugLog("spawnVirtualFallingBlock (single)");

        throw new RuntimeException("Not implemented");
    }

    public void moveVirtualArmorstand(Player player, int entityId, Vector3 location, double yawRotation){
        sendDebugLog("moveVirtualArmorstand (single)");

        new ArmorstandMoveServerPacket(
                protocolManager, entityId, location, yawRotation
        ).send(player);
    }

    @Override
    public void moveVirtualArmorstand(List<Player> players, int entityId, Vector3 location, double yawRotation) {
        sendDebugLog("moveVirtualArmorstand (multiple)");

        new ArmorstandMoveServerPacket(
                protocolManager, entityId, location, yawRotation
        ).sendAll(players);
    }

    @Override
    public void moveVirtualEntity(Player player, int entityId, Vector3 location, double yawRotation) {
        sendDebugLog("moveVirtualEntity (single)");

        new EntityMovePacket(
                protocolManager, entityId, location, yawRotation
        ).send(player);
    }

    @Override
    public void moveVirtualEntity(List<Player> players, int entityId, Vector3 location, double yawRotation) {
        sendDebugLog("moveVirtualEntity (multiple)");

        new EntityMovePacket(
                protocolManager, entityId, location, yawRotation
        ).sendAll(players);
    }

    public void destroyVirtualEntity(Player player, int entityId){
        sendDebugLog("destroyVirtualEntity (single)");

        new EntityDestroyServerPacket(
                protocolManager, entityId
        ).send(player);
    }

    @Override
    public void destroyVirtualEntity(List<Player> players, int entityId) {
        sendDebugLog("destroyVirtualEntity (multiple)");

        new EntityDestroyServerPacket(
                protocolManager, entityId
        ).sendAll(players);
    }

    public void teleportVirtualEntity(Player player, int entityId, Vector3 blockLocation){
        sendDebugLog("teleportVirtualEntity (single)");

        new EntityTeleportServerPacket(protocolManager, entityId, blockLocation).send(player);
    }

    @Override
    public void teleportVirtualEntity(List<Player> players, int entityId, Vector3 blockLocation) {
        sendDebugLog("teleportVirtualEntity (multiple)");

        new EntityTeleportServerPacket(protocolManager, entityId, blockLocation).sendAll(players);
    }

    @Override
    public void sendMountVirtualEntityPacket(List<Player> players, Player mounted, int entityId) {
        sendDebugLog("sendMountVirtualEntityPacket (multiple)");

        new EntityMountServerPacket(protocolManager, entityId, mounted).sendAll(players);
    }

    @Override
    public void sendClientPositionPacket(Player movedPlayer, Vector3 position) {
        sendDebugLog("sendClientPositionPacket (single)");

        new PlayerPositionServerPacket(protocolManager, position).send(movedPlayer);
    }

    @Override
    public Vector3 getArmorstandModelCompensationVector() {
        return ARMORSTAND_MODEL_COMPENSATION_1_19_2.clone();
    }

    public void sendEntityMetaDataPacket(Player player, int entityId, boolean invisible, String customName){
        sendDebugLog("sendEntityMetaDataPacket (single)");

        new EntityMetadataServerPacket(
                protocolManager, entityId, invisible, customName
        ).send(player);
    }

    @Override
    public double toPacketYaw(double normalYaw) {
        return normalYaw*256/360;
    }
}
