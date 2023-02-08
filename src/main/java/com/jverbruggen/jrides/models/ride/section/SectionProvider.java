package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
import com.jverbruggen.jrides.models.properties.TrackEnd;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.exception.SectionNotFoundException;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SectionProvider {
    public @NonNull Section getSectionFor(Train train, Section currentSection, Frame fromFrame, Frame toFrame){
        if(currentSection.isInSection(toFrame)) return currentSection;

        if(fromFrame.getTrack() != toFrame.getTrack()){
            return getSectionOnDifferentTrack(train, currentSection, toFrame);
        }

        Section subsequentNextSection = currentSection.next(train);
        Section subsequentPreviousSection = currentSection.previous(train);
        if(subsequentNextSection != null){
            if(subsequentNextSection.isInSection(toFrame)) {
//                Bukkit.broadcastMessage("isnext!");
                return subsequentNextSection;
            }
            if(currentSection.isInSection(fromFrame) && train.getDirection() == TrackEnd.END){
//                Bukkit.broadcastMessage("isnext! - special");

                int overshotFrameAmount = toFrame.getValue() - currentSection.getEndFrame().getValue();
                int newFrameValue = subsequentNextSection.getStartFrame().getValue() + overshotFrameAmount;
                train.getHeadOfTrainFrame().updateTo(new SimpleFrame(newFrameValue));
            }
        }else if(subsequentPreviousSection != null){
            if(subsequentPreviousSection.isInSection(toFrame)){ // Unchecked!
//                Bukkit.broadcastMessage("isprev!");
                return subsequentPreviousSection;
            }else if(currentSection.isInSection(fromFrame) && train.getDirection() == TrackEnd.START){
//                Bukkit.broadcastMessage("isprev! - special");

                int overshotFrameAmount = toFrame.getValue() - currentSection.getStartFrame().getValue();
                int newFrameValue = subsequentPreviousSection.getEndFrame().getValue() + overshotFrameAmount;
                train.getHeadOfTrainFrame().updateTo(new SimpleFrame(newFrameValue));
            }
        }

        Section found = findSectionBySearchingNext(train, toFrame, currentSection);
        if(found == null) throw new SectionNotFoundException(train);

        return found;
    }

    private Section findSectionBySearchingNext(Train train, Frame frame, Section firstSection) {
        List<Section> checked = new ArrayList<>();

        Section found = null;
        Section checking = firstSection;
        while(checking != null && found == null){
            if(checking.isInSection(frame)){
                found = checking;
            }else{
                checked.add(checking);
                checking = checking.next(train);

                if(checked.contains(checking))
                    checking = null;
            }
        }

        return found;
    }

    private Section findSectionInBulk(Frame frame){
        List<Section> sections = frame.getTrack().getSections();

        Section found = null;
        int i = 0;
        while(found == null && i < sections.size()){
            Section compare = sections.get(i);

            if(compare.isInSection(frame)) found = compare;

            i++;
        }

        return found;
    }

    public Section getSectionOnDifferentTrack(Train train, Section currentSection, Frame toFrame){
        Track newTrack = toFrame.getTrack();
        List<Section> newTrackSections = newTrack.getSections();

        // If rolling forwards
        Section logicalNextSection = currentSection.next(train);
        Section firstSectionNewTrack = newTrackSections.get(0);
        if(logicalNextSection.equals(firstSectionNewTrack)){
            return logicalNextSection;
        }

        // If rolling backwards
        Section logicalPreviousSection = currentSection.previous(train);
        Section lastSectionNewTrack = newTrackSections.get(newTrackSections.size()-1);
        if(logicalPreviousSection.equals(lastSectionNewTrack)){
            return logicalPreviousSection;
        }

        throw new RuntimeException("Unknown situation to handle section on different track");
    }
}
