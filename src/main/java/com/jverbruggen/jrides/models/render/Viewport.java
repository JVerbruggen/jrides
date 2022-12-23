package com.jverbruggen.jrides.models.render;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.List;

public interface Viewport {
    List<VirtualEntity> getEntities();
    List<Player> getViewers();
    boolean isInViewport(Vector3 location);
}
