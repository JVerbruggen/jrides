package com.jverbruggen.jrides.models.ride.section.reference;

import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.Map;

public abstract class SectionReference {
    public static Section findByIdentifier(String sectionIdentifier, Map<SectionReference, Section> sectionMap){
        return sectionMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().getSectionIdentifier().equalsIgnoreCase(sectionIdentifier))
                .findFirst().orElseThrow().getValue();
    }

    public abstract void setPreviousSectionIdentifier(String previousSectionIdentifier);

    public abstract String getPreviousSectionIdentifier();

    public abstract String getSectionIdentifier();

    public abstract String getNextSectionIdentifier();

    public abstract String getParentTrackIdentifier();

    public abstract Section makeSection();
}
