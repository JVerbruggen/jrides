/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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
