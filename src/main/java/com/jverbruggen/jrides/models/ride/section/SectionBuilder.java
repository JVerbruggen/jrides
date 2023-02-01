package com.jverbruggen.jrides.models.ride.section;

import java.util.ArrayList;
import java.util.List;

public class SectionBuilder {
    private final List<Section> sections;
    private boolean collected;
    private final boolean tieLooseEnds;

    public SectionBuilder(boolean tieLooseEnds) {
        this.sections = new ArrayList<>();
        this.collected = false;
        this.tieLooseEnds = tieLooseEnds;
    }

    private Section getFirstItem(){
        if(sections.size() == 0) return null;
        return sections.get(0);
    }

    private Section getLastItem(){
        if(sections.size() == 0) return null;
        return sections.get(sections.size()-1);
    }

    public SectionBuilder add(Section section){
        Section previousSection = getLastItem();
        if(previousSection != null){
            section.setPrevious(previousSection);
            previousSection.setNext(section);
        }

        sections.add(section);
        return this;
    }

    public List<Section> collect(){
        if(sections.size() == 0) throw new RuntimeException("Cannot collect empty section builder");
        if(collected) throw new RuntimeException("Builder was already collected");
        collected = true;

        if(tieLooseEnds){
            Section firstSection = getFirstItem();
            Section lastSection = getLastItem();

            assert firstSection != null;
            assert lastSection != null;

            firstSection.setPrevious(lastSection);
            lastSection.setNext(firstSection);
        }

        return sections;
    }



}
