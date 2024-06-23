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

package com.jverbruggen.jrides.models.properties.frame;

import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

public interface Frame {
    int getValue();
    String getValueString();

    void setValue(int frame);

    Track getTrack();
    void setTrack(Track track);

    Section getSection();
    void setSection(Section section);

    Frame add(int frames);
    Frame clone();
    Frame capture();

    void setInvertedFrameAddition(boolean inverted);
    boolean isInvertedFrameAddition();
    void updateTo(Frame other);

    static int getCyclicFrameValue(int nonCyclicFrame, int totalFrames){
        return Math.floorMod(nonCyclicFrame, totalFrames);
    }

    static boolean isBetweenFrames(Frame lowerFrame, Frame upperFrame, Frame compareFrame){
        int lowerFrameValue = lowerFrame.getValue();
        int upperFrameValue = upperFrame.getValue();
        int compareFrameValue = compareFrame.getValue();

        boolean inNormalSection = (lowerFrameValue < upperFrameValue)
                && (lowerFrameValue <= compareFrameValue && compareFrameValue <= upperFrameValue);
        boolean inCycledSection = (upperFrameValue < lowerFrameValue)
                && (lowerFrameValue <= compareFrameValue || compareFrameValue <= upperFrameValue);

        return inNormalSection || inCycledSection;
    }

    static Frame getDistanceFromUpperFrame(Frame lowerFrame, Frame upperFrame, int distanceFromUpperFrame){
        if(lowerFrame.getValue() >= upperFrame.getValue())
            return upperFrame.clone();

        Frame calculatedDistanceFromUpperFrame = upperFrame.clone().add(-distanceFromUpperFrame);
        if(calculatedDistanceFromUpperFrame.getValue() < lowerFrame.getValue())
            return lowerFrame.clone();

        return calculatedDistanceFromUpperFrame;
    }
}
