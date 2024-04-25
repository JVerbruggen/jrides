package com.jverbruggen.jrides.state.viewport;

import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.packets.object.VirtualArmorstandConfiguration;
import org.bukkit.entity.EntityType;

import java.util.List;

public interface ViewportManager {
    void updateVisuals(Player player);
    void updateVisuals(Player player, Vector3 playerLocation);
    void updateForEntity(VirtualEntity virtualEntity);
    VirtualEntity spawnModelEntity(Vector3 location, TrainModelItem headModel);
    VirtualEntity spawnModelEntity(Vector3 location, TrainModelItem headModel, String customName);
    VirtualEntity spawnSeatEntity(Vector3 location, double yawRotation, TrainModelItem model);
    VirtualEntity spawnVirtualEntity(Vector3 location, EntityType entityType);
    VirtualEntity spawnVirtualEntity(Vector3 location, EntityType entityType, double yawRotation);
    VirtualArmorstand spawnVirtualArmorstand(Vector3 location);
    VirtualArmorstand spawnVirtualArmorstand(Vector3 location, double yawRotation);
    VirtualArmorstand spawnVirtualArmorstand(Vector3 location, TrainModelItem headModel, VirtualArmorstandConfiguration configuration);
    VirtualArmorstand spawnVirtualArmorstand(Vector3 location, double yawRotation, TrainModelItem headModel, VirtualArmorstandConfiguration configuration);
    void despawnAll();
    VirtualEntity getEntity(int entityId);
    int getRenderChunkSize();
    int getRenderDistance();

    void removeEntities(TrainHandle trainHandle);
    void removeEntities(List<TrainHandle> trainHandles);
}
