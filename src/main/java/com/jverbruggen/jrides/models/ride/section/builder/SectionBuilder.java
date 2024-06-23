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

import com.jverbruggen.jrides.models.ride.section.Section;

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
