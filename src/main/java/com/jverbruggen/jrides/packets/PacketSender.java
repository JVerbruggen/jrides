package com.jverbruggen.jrides.packets;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandModels;
import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandRotations;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.entity.EntityType;

import java.util.List;

public interface PacketSender {
    String getIdentifier();
    void spawnVirtualEntity(Player player, int entityId, Vector3 location, double yawRotation,
                            EntityType entityType, boolean invisible, int leashedToEntity);
    void spawnVirtualArmorstand(Player player, int entityId, Vector3 location, double yawRotation,
                                ArmorstandRotations rotations, ArmorstandModels models, boolean invisible,
                                int leashedToEntity);
    void spawnVirtualArmorstand(List<Player> players, int entityId, Vector3 location, double yawRotation,
                                ArmorstandRotations rotations, ArmorstandModels models, boolean invisible,
                                int leashedToEntity);
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
}
