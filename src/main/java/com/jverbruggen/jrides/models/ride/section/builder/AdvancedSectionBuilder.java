package com.jverbruggen.jrides.models.ride.section.builder;

import com.jverbruggen.jrides.models.ride.coaster.transfer.Transfer;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;
import org.bukkit.Bukkit;

import java.util.Collection;
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
            String existingNextSectionIdentifier = existingSectionReference.getNextSectionIdentifier();
            if(existingNextSectionIdentifier != null
                    && existingNextSectionIdentifier.equalsIgnoreCase(sectionIdentifier)){
                sectionReference.setPreviousSectionIdentifier(existingSectionReference.getSectionIdentifier());
            }
        }

        sectionReferences.put(sectionIdentifier, sectionReference);
        return this;
    }

    private Map.Entry<SectionReference, Section> findSection(Map<SectionReference, Section> sections, String sectionIdentifier){
        if(sectionIdentifier == null) return null;

        Map.Entry<SectionReference, Section> found = null;
        for(Map.Entry<SectionReference, Section> entry : sections.entrySet()){
            if(entry.getKey().getSectionIdentifier().equalsIgnoreCase(sectionIdentifier)){
                found = entry;
                break;
            }
        }
        if(found == null)
            throw new RuntimeException("Section " + sectionIdentifier + " not found in map");

        return found;
    }

    public void calculate(){
        if(calculated)
            throw new RuntimeException("Already calculated");

        // --- Initialize section objects
        Map<SectionReference, Section> sections = new HashMap<>();
        for(SectionReference sectionReference : sectionReferences.values()){
            sections.put(sectionReference, sectionReference.makeSection());
        }

        // --- Link section ends together
        for(Map.Entry<SectionReference, Section> entry : sections.entrySet()){
            SectionReference sectionReference = entry.getKey();
            Section section = entry.getValue();

            Map.Entry<SectionReference, Section> foundNextSectionEntry = findSection(sections, sectionReference.getNextSectionIdentifier());
            Section nextSection = null;
            if(foundNextSectionEntry != null)
                nextSection = foundNextSectionEntry.getValue();

            section.setNext(nextSection);
            if(nextSection != null)
                nextSection.setPrevious(section);
        }

        this.result = sections;
        this.calculated = true;
    }

    public void populateTransfers(List<Transfer> transfers){
        for(Transfer transfer : transfers){
            transfer.populateTransferPositionSections(result);
        }
    }

    public void populateBehaviours(){
        for(Section section : result.values()){
            section.getTrackBehaviour().populateSectionReferences(result);
        }
    }

    public List<Section> collectFor(String track){
        if(!this.calculated) throw new RuntimeException("Calculate the builder before collecting");

        return result.entrySet().stream()
                .filter(entry -> entry.getKey().getParentTrackIdentifier().equalsIgnoreCase(track))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

}
