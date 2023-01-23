package com.jverbruggen.jrides.models.ride.coaster;

public class SimpleSection implements Section {
    private int endFrame;

    public SimpleSection(int endFrame) {
        this.endFrame = endFrame;
    }

    @Override
    public int getEndFrame() {
        return this.endFrame;
    }
}
