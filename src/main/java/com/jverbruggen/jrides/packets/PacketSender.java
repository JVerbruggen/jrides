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

package com.jverbruggen.jrides.packets;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.object.VirtualArmorstandConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;

import java.util.List;

public interface PacketSender {
    String getIdentifier();
    void spawnVirtualEntity(Player player, int entityId, Vector3 location, double yawRotation,
                            EntityType entityType, boolean invisible, int leashedToEntity);
    void spawnVirtualArmorstand(Player player, int entityId, Vector3 location, double yawRotation,
                                VirtualArmorstandConfiguration configuration);
    void spawnVirtualArmorstand(List<Player> players, int entityId, Vector3 location, double yawRotation,
                                VirtualArmorstandConfiguration configuration);
    void spawnVirtualFallingBlock(Player player, int entityId, Vector3 location);
    void sendRotationPacket(Player player, int entityId, int rotationType, Vector3 rotation);
    void sendRotationPacket(List<Player> players, int entityId, int rotationType, Vector3 rotation);
    void sendApplyModelPacket(Player player, int entityId, EnumWrappers.ItemSlot itemSlot, TrainModelItem model);
    void sendApplyModelPacket(List<Player> players, int entityId, EnumWrappers.ItemSlot itemSlot, TrainModelItem model);
    void sendAttachLeashPacket(Player player, int entityId, int leashToEntityId);
    void moveVirtualArmorstand(Player player, int entityId, Vector3 location, double yawRotation);
    void moveVirtualArmorstand(List<Player> players, int entityId, Vector3 location, double yawRotation);
    void moveVirtualEntity(Player player, int entityId, Vector3 location, double yawRotation);
    void moveVirtualEntity(List<Player> players, int entityId, Vector3 location, double yawRotation);
    void destroyVirtualEntity(Player player, int entityId);
    void destroyVirtualEntity(List<Player> players, int entityId);
    void teleportVirtualEntity(Player player, int entityId, Vector3 blockLocation);
    void teleportVirtualEntity(List<Player> players, int entityId, Vector3 blockLocation);
    void sendMountVirtualEntityPacket(List<Player> players, Player mounted, int entityId);
    void sendClientPositionPacket(Player movedPlayer, Vector3 position);
    void sendApplyItemDisplayModelPacket(Player player, int entityId, ItemDisplay.ItemDisplayTransform itemDisplayTransform, TrainModelItem model);
    void sendApplyItemDisplayModelPacket(List<Player> players, int entityId, ItemDisplay.ItemDisplayTransform itemDisplayTransform, TrainModelItem model);
    void sendItemDisplayMetaDataPacket(Player player, int entityId, int PositionRotationInterpolationDuration);
    void sendItemDisplayRotationPacket(List<Player> players, int entityId, Quaternion orientation, int positionRotationInterpolationDuration);
    Vector3 getArmorstandModelCompensationVector();
    double toPacketYaw(double normalYaw);
}
