package com.jverbruggen.jrides.models.ride.section;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.TrainHandle;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.properties.TrainEnd;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.SimpleFrame;
import com.jverbruggen.jrides.models.properties.TrackEnd;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.exception.SectionNotFoundException;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class SectionProvider {
    public void addFramesWithSectionLogic(TrainHandle trainHandle, Frame toFrame, int value){
        addFramesWithSectionLogic(trainHandle, toFrame, value, false, null, "", false);
    }

    public void addFramesWithSectionLogic(TrainHandle trainHandle, Frame toFrame, int value, boolean updateSectionOccupations, TrainEnd trainEnd, String debugName, boolean applyNewBehaviour){
        Frame fromFrame = toFrame.capture();
        toFrame.setValue(value);

        Section fromSection = fromFrame.getSection();
        if(fromSection.isInSection(toFrame)) return;

        Train train = trainHandle.getTrain();
        boolean positiveDrivingDirection = train.isPositiveDrivingDirection();
        Section toNewSection = getSectionFor(train, fromSection, fromFrame, toFrame);

        if(updateSectionOccupations){
            JRidesPlugin.getLogger().info(LogType.SECTIONS, debugName + ": " + trainEnd + "(" + positiveDrivingDirection);
            JRidesPlugin.getLogger().info(LogType.SECTIONS, "to" + debugName + ": " + fromSection + " => " + toNewSection);
            sectionOccupationLogic(trainHandle, fromSection, toNewSection, trainEnd, applyNewBehaviour);
        }

        toFrame.setSection(toNewSection);
        toFrame.setInvertedFrameAddition(!positiveDrivingDirection);
    }

    public void sectionOccupationLogic(TrainHandle trainHandle, Section fromSection, Section toSection, TrainEnd onTrainEnd, boolean applyNewBehaviour){
        Train train = trainHandle.getTrain();

        // If the section it is entering is occupied by some train
        if(toSection.isOccupied()){
            // If that train is a different train
            if(!toSection.getOccupiedBy().equals(train)){
                // .. crash
                train.setCrashed(true);
                JRidesPlugin.getLogger().warning(LogType.CRASH, "Train " + train + " has crashed!");
                JRidesPlugin.getLogger().warning(LogType.CRASH, train.getHeadSection().toString());
                JRidesPlugin.getLogger().warning(LogType.CRASH, toSection.toString());
                // else if that train is self
            }else{
                JRidesPlugin.getLogger().info(LogType.SECTIONS, "sectionLogic - Occupied");
                if(applyNewBehaviour) trainHandle.setTrackBehaviour(toSection.getTrackBehaviour());
                if(!fromSection.spansOver(train)){
                    JRidesPlugin.getLogger().info(LogType.SECTIONS, "sectionLogic - Not spans over");
                    fromSection.removeOccupation(train);
                    train.removeCurrentSection(fromSection);
                    fromSection.getTrackBehaviour().trainExitedAtEnd();
                }else{
                    JRidesPlugin.getLogger().info(LogType.SECTIONS, "sectionLogic - Yes spans over");
                }
            }
            // else if the section is free
        }else{
            // .. occupy it
            toSection.addOccupation(train);
            train.addCurrentSection(toSection, onTrainEnd);
            if(applyNewBehaviour){
                trainHandle.setTrackBehaviour(toSection.getTrackBehaviour());

                //TODO: Check where it is entering from (assuming always from start now)
                train.setDrivingDirection(true);
            }
        }
    }

    public @NonNull Section getSectionFor(Train train, Section currentSection, Frame fromFrame, Frame toFrame){
        if(currentSection.isInSection(toFrame)) return currentSection;

        if(fromFrame.getTrack() != toFrame.getTrack()){
            return getSectionOnDifferentTrack(train, currentSection, toFrame);
        }

        Section subsequentNextSection = currentSection.next(train);
        Section subsequentPreviousSection = currentSection.previous(train);
        if(subsequentNextSection != null){
            if(subsequentNextSection.isInSection(toFrame)) {
                JRidesPlugin.getLogger().info(LogType.SECTIONS, "isnext!");
                return subsequentNextSection;
            }
            if(currentSection.isInSection(fromFrame) && train.getDirection() == TrackEnd.END){
                int overshotFrameAmount = getOvershotFrameAmount(train, currentSection, toFrame);
                int newFrameValue = subsequentNextSection.getStartFrame().getValue() + overshotFrameAmount;
                JRidesPlugin.getLogger().info(LogType.SECTIONS,
                        "isnext! special - to: " + toFrame.getValue() + " over: " + overshotFrameAmount + ", new: " + newFrameValue);

                toFrame.setValue(newFrameValue);
                return subsequentNextSection;
            }
        }else if(subsequentPreviousSection != null){
            if(subsequentPreviousSection.isInSection(toFrame)){ // Unchecked!
                JRidesPlugin.getLogger().info(LogType.SECTIONS, "isprev!");
                return subsequentPreviousSection;
            }else if(currentSection.isInSection(fromFrame) && train.getDirection() == TrackEnd.START){
                int overshotFrameAmount = getOvershotFrameAmount(train, currentSection, toFrame);
                int newFrameValue = subsequentPreviousSection.getEndFrame().getValue() + overshotFrameAmount;
                JRidesPlugin.getLogger().info(LogType.SECTIONS,
                        "isprev! special - to: " + toFrame.getValue() + " over: " + overshotFrameAmount + ", new: " + newFrameValue);

                toFrame.setValue(newFrameValue);
                return subsequentPreviousSection;
            }
        }

        Section found = findSectionBySearchingNext(train, toFrame, currentSection);
        if(found == null){
            train.setCrashed(true);
            JRidesPlugin.getLogger().info(LogType.SECTIONS,
                    "error: " + currentSection + " to: " + toFrame);
            throw new SectionNotFoundException(train);
        }else{
            JRidesPlugin.getLogger().info(LogType.SECTIONS,
                    "Next section found when searched!");
        }

        return found;
    }

    private int getOvershotFrameAmount(Train train, Section currentSection, Frame toFrame){
        if(train.isPositiveDrivingDirection())
            return toFrame.getValue() - currentSection.getEndFrame().getValue();
        else
            return currentSection.getStartFrame().getValue() - toFrame.getValue();
    }

    public Section findSectionBySearchingNext(Train train, Frame frame, Section firstSection) {
        return findSectionBySearchingRelative(train, frame, firstSection, Section::next);
    }

    public Section findSectionBySearchingPrevious(Train train, Frame frame, Section firstSection) {
        return findSectionBySearchingRelative(train, frame, firstSection, Section::previous);
    }

    private Section findSectionBySearchingRelative(Train train, Frame frame, Section firstSection, BiFunction<Section, Train, Section> relativeFunction){
        List<Section> checked = new ArrayList<>();

        Section found = null;
        Section checking = firstSection;
        while(checking != null && found == null){
            if(checking.isInSection(frame)){
                found = checking;
            }else{
                checked.add(checking);
                checking = relativeFunction.apply(checking, train);

                if(checked.contains(checking))
                    checking = null;
            }
        }

        return found;
    }

    public Section findSectionInBulk(Frame frame, List<Section> sections){
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
