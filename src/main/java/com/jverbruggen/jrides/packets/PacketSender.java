package com.jverbruggen.jrides.packets;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandModels;
import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandRotations;
import com.jverbruggen.jrides.models.math.Vector3;

public interface PacketSender {
    void spawnVirtualArmorstand(Player player, int entityId, Vector3 location, double yawRotation,
                                ArmorstandRotations rotations, ArmorstandModels models, boolean invisible,
                                int leashedToEntity);
}
