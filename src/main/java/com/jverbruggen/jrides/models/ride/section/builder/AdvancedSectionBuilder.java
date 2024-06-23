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

package com.jverbruggen.jrides.models.ride.section.builder;

import com.jverbruggen.jrides.models.ride.coaster.transfer.Transfer;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;
import org.bukkit.Bukkit;

import java.util.*;
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

        List<SectionReference> allSectionReferences = new ArrayList<>(sectionReferences.values());

        // --- Initialize section objects
        Map<SectionReference, Section> sections = new HashMap<>();
        for(SectionReference sectionReference : allSectionReferences){
            sections.put(sectionReference, sectionReference.makeSection());
        }

        // --- Populate conflicting sections
        for(Map.Entry<SectionReference, Section> entry : sections.entrySet()){
            SectionReference sectionReference = entry.getKey();
            Section section = entry.getValue();

            List<String> conflictSectionStrings = sectionReference.getConflictSectionStrings();
            if(conflictSectionStrings == null) continue;

            List<Section> conflictingSections = sections.values().stream()
                    .filter(s -> conflictSectionStrings.contains(s.getName()))
                    .collect(Collectors.toList());

            section.setConflictSections(conflictingSections);
//            conflictingSections.forEach(other -> Bukkit.broadcastMessage(section.getName() + " conflicts with " + other));
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
