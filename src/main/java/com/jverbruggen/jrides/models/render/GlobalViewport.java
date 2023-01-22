package com.jverbruggen.jrides.models.render;

import com.jverbruggen.jrides.models.math.Vector3;

public class GlobalViewport extends VirtualEntityViewport implements Viewport {
    @Override
    public boolean isInViewport(Vector3 location) {
        return true;
    }
}
