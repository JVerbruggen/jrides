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

package com.jverbruggen.jrides.state.ride.flatride;

import com.jverbruggen.jrides.animator.flatride.BlenderExportPositionRecord;
import com.jverbruggen.jrides.models.math.VectorQuaternionState;

import java.util.ArrayList;
import java.util.List;

public class Animation {
    private final String target;
    private final List<BlenderExportPositionRecord> frames;

    public Animation(String target) {
        this.target = target;
        this.frames = new ArrayList<>();
    }

    public String getTarget() {
        return target;
    }

    public void addPosition(BlenderExportPositionRecord rawPosition) {
        frames.add(rawPosition);
    }

    public List<BlenderExportPositionRecord> getFrames() {
        return frames;
    }

    public VectorQuaternionState getInitialPose(){
        return frames.get(0).toVectorQuaternionState();
    }
}
