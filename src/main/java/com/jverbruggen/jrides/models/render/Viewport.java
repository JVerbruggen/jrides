package com.jverbruggen.jrides.models.render;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.List;

public interface Viewport {
    List<VirtualEntity> getEntities();
    List<Player> getViewers();
    boolean isInViewport(Vector3 location);
    void addViewer(Player player);
    void removeViewer(Player player);
    void updateFor(Player player);
    boolean hasViewer(Player player);
    void addEntity(VirtualEntity virtualEntity);
    void removeEntity(VirtualEntity virtualEntity);
    void updateEntityViewers(VirtualEntity virtualEntity);
    boolean hasEntity(VirtualEntity virtualEntity);
}
