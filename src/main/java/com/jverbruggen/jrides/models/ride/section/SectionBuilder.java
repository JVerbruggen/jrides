package com.jverbruggen.jrides.models.ride.section;

import java.util.ArrayList;
import java.util.List;

public class SectionBuilder {
    private List<Section> sections;
    private boolean collected;

    public SectionBuilder() {
        this.sections = new ArrayList<>();
        this.collected = false;
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

        Section firstSection = getFirstItem();
        Section lastSection = getLastItem();

        assert firstSection != null;
        assert lastSection != null;

        firstSection.setPrevious(lastSection);
        lastSection.setNext(firstSection);

        return sections;
    }



}
