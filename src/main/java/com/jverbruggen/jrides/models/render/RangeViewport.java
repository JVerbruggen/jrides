package com.jverbruggen.jrides.models.render;

import com.jverbruggen.jrides.models.math.Vector3;

public class RangeViewport extends VirtualEntityViewport implements Viewport {
    private Vector3 middle;
    private double range;

    public RangeViewport(int maxRenderDistance, Vector3 middle, double range) {
        super(maxRenderDistance);
        this.middle = middle;
        this.range = range;
    }

    @Override
    public boolean isInViewport(Vector3 location) {
        double rangeSquared = range*range;
        double distanceSquared = middle.distanceSquared(location);
        return distanceSquared <= rangeSquared;
    }
}
