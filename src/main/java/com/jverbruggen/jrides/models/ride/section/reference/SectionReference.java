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

package com.jverbruggen.jrides.models.ride.section.reference;

import com.jverbruggen.jrides.models.ride.section.Section;

import java.util.List;
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

    public abstract List<String> getConflictSectionStrings();

    @Override
    public String toString() {
        return "<SRef to " + getSectionIdentifier() + ">";
    }
}
