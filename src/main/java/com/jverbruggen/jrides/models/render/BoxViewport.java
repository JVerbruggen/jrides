package com.jverbruggen.jrides.models.render;

import com.jverbruggen.jrides.models.math.Vector3;

public class BoxViewport extends VirtualEntityViewport implements Viewport {
    protected Vector3 lowerCorner;
    protected Vector3 upperCorner;

    public BoxViewport(Vector3 lowerCorner, Vector3 upperCorner) {
        super();
        this.lowerCorner = lowerCorner;
        this.upperCorner = upperCorner;
    }

    @Override
    public boolean isInViewport(Vector3 location) {
        return false;
    }
}
