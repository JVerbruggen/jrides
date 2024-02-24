package com.jverbruggen.jrides.state.ride.flatride;

import com.jverbruggen.jrides.animator.flatride.BlenderExportPositionRecord;

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
}
