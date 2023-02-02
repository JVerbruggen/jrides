package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.models.properties.Frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdvancedSectionBuilder {
    private final Map<String, SectionReference> sectionReferences;
    private boolean calculated;
    private Map<SectionReference, Section> result;

    public AdvancedSectionBuilder() {
        this.sectionReferences = new HashMap<>();
        this.calculated = false;
        this.result = null;
    }

    public AdvancedSectionBuilder add(SectionReference sectionReference){
        String sectionIdentifier = sectionReference.getSectionIdentifier();
        String nextSectionIdentifier = sectionReference.getNextSectionIdentifier();

        if(sectionReferences.containsKey(nextSectionIdentifier)){
            SectionReference nextSectionReference = sectionReferences.get(nextSectionIdentifier);
            nextSectionReference.setPreviousSectionIdentifier(sectionIdentifier);
        }

        for(Map.Entry<String, SectionReference> existingSection : sectionReferences.entrySet()){
            SectionReference existingSectionReference = existingSection.getValue();
            if(existingSectionReference.getNextSectionIdentifier().equalsIgnoreCase(sectionIdentifier)){
                sectionReference.setPreviousSectionIdentifier(existingSectionReference.getSectionIdentifier());
            }
        }

        sectionReferences.put(sectionIdentifier, sectionReference);
        return this;
    }

    private Map.Entry<SectionReference, Section> findSection(Map<SectionReference, Section> sections, String sectionName){
        Map.Entry<SectionReference, Section> found = null;
        for(Map.Entry<SectionReference, Section> entry : sections.entrySet()){
            if(entry.getKey().getSectionIdentifier().equalsIgnoreCase(sectionName)){
                found = entry;
                break;
            }
        }
        if(found == null)
            throw new RuntimeException("Section " + sectionName + " not found in map");

        return found;
    }

    public void calculate(){
        if(calculated)
            throw new RuntimeException("Already calculated");

        // --- Initialize section objects
        Map<SectionReference, Section> sections = new HashMap<>();
        for(SectionReference sectionReference : sectionReferences.values()){
            Frame startFrame = sectionReference.getStartFrame();
            Frame endFrame = sectionReference.getEndFrame();
            TrackBehaviour trackBehaviour = sectionReference.getTrackBehaviour();

            Section section = new SimpleSection(startFrame, endFrame, trackBehaviour);
            sections.put(sectionReference, section);
        }

        // --- Link section ends together
        for(Map.Entry<SectionReference, Section> entry : sections.entrySet()){
            SectionReference sectionReference = entry.getKey();
            Section section = entry.getValue();

            Section nextSection = findSection(sections, sectionReference.getNextSectionIdentifier()).getValue();
//            Section previousSection = findSection(sections, sectionReference.getPreviousSectionIdentifier()).getValue();

            section.setNext(nextSection);
            nextSection.setPrevious(section);
        }

        this.result = sections;
        this.calculated = true;
    }

    public List<Section> collectFor(String track){
        if(!this.calculated) throw new RuntimeException("Calculate the builder before collecting");

        return result.entrySet().stream()
                .filter(entry -> entry.getKey().getParentTrackIdentifier().equalsIgnoreCase(track))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }



}
