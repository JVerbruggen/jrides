package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.models.math.MathUtil;
import com.jverbruggen.jrides.models.math.Vector2;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.AutoTrackUpdateFrame;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.*;
import java.util.stream.Collectors;

class SectionVectorCombi{
    private final Section section;
    private final Vector3 vector3;

    public SectionVectorCombi(Section section, Vector3 vector3) {
        this.section = section;
        this.vector3 = vector3;
    }

    public Section getSection() {
        return section;
    }

    public Vector3 getVector3() {
        return vector3;
    }
}

public class SectionVisualFactory {
    public List<SectionVisual> createVisuals(CoasterHandle handle){
        List<Section> sections = handle.getTrack().getSections();
        Track track = handle.getTrack();

        // For normalization
        int minX = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        int borderPadding = 10;

        List<SectionVectorCombi> locations3D = new ArrayList<>();
        for(Section section : sections){
            Frame startFrame = section.getStartFrame().clone();
            Frame endFrame = section.getEndFrame().clone();

            int distance = getForwardDistance(startFrame.getTrack(), startFrame, endFrame);
            int interval = 50;
            int amount = distance/interval;

            AutoTrackUpdateFrame walkingFrame = AutoTrackUpdateFrame.of(startFrame);
            for(int i = 0; i < amount; i++){
                walkingFrame.add(interval);

                Vector3 location = section.getLocationFor(walkingFrame);

                locations3D.add(new SectionVectorCombi(section, location));

                // Normalization preparations, getting outer bounds
                int x = location.getBlockX();
                int z = location.getBlockZ();

                if(x < minX) minX = x;
                else if(x > maxX) maxX = x;

                if(z < minZ) minZ = z;
                else if(z > maxZ) maxZ = z;
            }
        }

        minX -= borderPadding;
        minZ -= borderPadding;
        maxX += borderPadding;
        maxZ += borderPadding;

        // Normalize to 2d
        Map<Section, SectionVisual> sectionVisuals = new HashMap<>();
        for(SectionVectorCombi pack : locations3D){
            Vector3 location3D = pack.getVector3();
            Section section = pack.getSection();

            int x = (int) MathUtil.map(location3D.getX(), minX, maxX, 0, 127);
            int z = (int) MathUtil.map(location3D.getZ(), minZ, maxZ, 0, 127);

            SectionVisual visual = sectionVisuals.get(section);
            if(visual == null){
                visual = new SectionVisual(section);
                sectionVisuals.put(section, visual);
            }

            visual.addDrawPoint(new Vector2(x, z));
        }

        // make visuals
        return new ArrayList<>(sectionVisuals.values());
    }

    private int getForwardDistance(Track track, Frame from, Frame toForwards){
        int fromValue = from.getValue();
        int toValue = toForwards.getValue();

        if(toValue > fromValue) return toValue - fromValue;

        int beforeCycleDistance = track.getUpperFrame() - fromValue;
        int afterCycleDistance = toValue - track.getLowerFrame();
        return beforeCycleDistance + afterCycleDistance;
    }
}
