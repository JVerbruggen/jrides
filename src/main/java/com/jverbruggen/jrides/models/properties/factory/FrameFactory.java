package com.jverbruggen.jrides.models.properties.factory;

import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.SimpleFrame;

public class FrameFactory {


    public Frame createFrameBetween(Frame a, Frame b, double percentage){
        assert percentage <= 1 && percentage >= 0;

        int aValue = a.getValue();
        int bValue = b.getValue();
        int delta = bValue - aValue;
        int offset = (int) (delta * percentage);

        return new SimpleFrame(aValue + offset);
    }
}
