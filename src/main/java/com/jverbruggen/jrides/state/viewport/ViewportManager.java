package com.jverbruggen.jrides.state.viewport;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;

public interface ViewportManager {
    void updateVisuals(Player player);
    void updateForEntity(VirtualEntity virtualEntity);
    VirtualArmorstand spawnVirtualArmorstand(Vector3 location);
    VirtualArmorstand spawnVirtualArmorstand(Vector3 location, double yawRotation);
    VirtualArmorstand spawnVirtualArmorstand(Vector3 location, TrainModelItem headModel);
    VirtualArmorstand spawnVirtualArmorstand(Vector3 location, double yawRotation, TrainModelItem headModel);
    void despawnAll();
    VirtualEntity getEntity(int entityId);
    int getRenderChunkSize();
    int getRenderDistance();
}
