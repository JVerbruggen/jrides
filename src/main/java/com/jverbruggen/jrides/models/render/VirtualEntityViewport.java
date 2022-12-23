package com.jverbruggen.jrides.models.render;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class VirtualEntityViewport implements Viewport{
    protected List<VirtualEntity> virtualEntities;
    protected List<Player> viewers;

    public VirtualEntityViewport() {
        this.virtualEntities = new ArrayList<>();
        this.viewers = new ArrayList<>();
    }

    @Override
    public List<VirtualEntity> getEntities() {
        return virtualEntities;
    }

    @Override
    public List<Player> getViewers() {
        return viewers;
    }
}
