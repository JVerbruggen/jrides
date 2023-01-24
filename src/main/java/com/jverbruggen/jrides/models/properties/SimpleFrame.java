package com.jverbruggen.jrides.models.properties;

public class SimpleFrame implements Frame {
    private int frame;

    public SimpleFrame(int frame) {
        this.frame = frame;
    }

    @Override
    public int getValue() {
        return frame;
    }

    @Override
    public void setValue(int frame) {
        this.frame = frame;
    }

    @Override
    public Frame add(int frames){
        this.frame += frames;
        return this;
    }

    @Override
    public Frame clone(){
        return new SimpleFrame(frame);
    }

    @Override
    public void updateTo(Frame other){
        this.frame = other.getValue();
    }

    @Override
    public String toString() {
        return "<Frame: " + getValue() + ">";
    }
}
