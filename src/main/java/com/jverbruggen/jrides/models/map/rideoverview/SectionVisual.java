package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.models.math.Vector2;
import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.ArrayList;
import java.util.List;

public class SectionVisual {
    private Section section;
    private List<Vector2> drawPoints;

    public SectionVisual(Section section) {
        this.section = section;
        this.drawPoints = new ArrayList<>();
    }

    public Section getSection() {
        return section;
    }

    public List<Vector2> getDrawPoints() {
        return drawPoints;
    }

    public void addDrawPoint(Vector2 point){
        drawPoints.add(point);
    }
}
