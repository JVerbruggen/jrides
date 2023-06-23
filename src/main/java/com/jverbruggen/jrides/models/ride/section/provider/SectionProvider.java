package com.jverbruggen.jrides.models.ride.section.provider;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.TrainHandle;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.properties.TrainEnd;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.SimpleFrame;
import com.jverbruggen.jrides.models.properties.TrackEnd;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.exception.SectionNotFoundException;
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
        Section toNewSection = getSectionFor(train, fromSection, fromFrame, toFrame, applyNewBehaviour);

        if(updateSectionOccupations){
            JRidesPlugin.getLogger().info(LogType.SECTIONS, debugName + ": " + trainEnd + "(" + positiveDrivingDirection);
            JRidesPlugin.getLogger().info(LogType.SECTIONS, "to " + debugName + ": " + fromSection + " => " + toNewSection);
            sectionOccupationLogic(trainHandle, fromSection, toNewSection, trainEnd, applyNewBehaviour);
        }

        toFrame.setSection(toNewSection);
        toFrame.setInvertedFrameAddition(!train.isFacingForwards());
    }

    public void sectionOccupationLogic(TrainHandle trainHandle, Section fromSection, Section toSection, TrainEnd onTrainEnd, boolean applyNewBehaviour){
        final Train train = trainHandle.getTrain();

        // If the section it is entering is occupied by some train
        if(toSection.isOccupied()){
            // If that train is a different train
            if(!toSection.getOccupiedBy().equals(train)){
                // .. crash
                train.setCrashed(true);
                sendTrainCrashMessage(train, toSection, null);
                // else if that train is self
            }else{
                JRidesPlugin.getLogger().info(LogType.SECTIONS, "sectionLogic - Occupied");
                if(applyNewBehaviour) trainHandle.setTrackBehaviour(toSection.getTrackBehaviour());
                if(!fromSection.spansOver(train)){
                    JRidesPlugin.getLogger().info(LogType.SECTIONS, "sectionLogic - Not spans over");
                    fromSection.removeOccupation(train);
                    train.removeCurrentSection(fromSection);
                    fromSection.getTrackBehaviour().trainExitedAtEnd();

                    if(fromSection.canBlock()) fromSection.clearEntireBlockReservation(train);
                }else{
                    JRidesPlugin.getLogger().info(LogType.SECTIONS, "sectionLogic - Yes spans over");
                }
            }
            // else if the section is free
        }else{
            // .. occupy it
            if(toSection.getReservedBy() != train)
                throw new RuntimeException("Logic error: Section " + toSection.getName() + " was not reserved in section occupation logic!");

            toSection.addOccupation(train);
            train.addCurrentSection(toSection, onTrainEnd);
            if(applyNewBehaviour){
                trainHandle.setTrackBehaviour(toSection.getTrackBehaviour());

                //TODO: Check where it is entering from (assuming always from start now)
                train.setDrivingDirection(true);
            }
        }
    }

    private void sendTrainCrashMessage(Train train, Section onSection, Frame onFrame) {
        JRidesLogger logger = JRidesPlugin.getLogger();
        logger.warning(LogType.CRASH, "Train " + train + " has crashed!");
        logger.warning(LogType.CRASH, "| head-fr: " + train.getHeadOfTrainFrame());
        logger.warning(LogType.CRASH, "| midl-fr: " + train.getMiddleOfTrainFrame());
        logger.warning(LogType.CRASH, "| tail-fr: " + train.getTailOfTrainFrame());
        logger.warning(LogType.CRASH, "| head-sc: " + train.getHeadSection());
        logger.warning(LogType.CRASH, "| tail-sc: " + train.getTailSection());
        logger.warning(LogType.CRASH, "crash-sc:" + onSection.toString());
        logger.warning(LogType.CRASH, "crash-fr:" + onFrame);
        for(CoasterCart cart : train.getCarts()){
            logger.warning(LogType.CRASH, "| cart-fr: " + cart.getFrame());
        }
    }

    public @NonNull Section getSectionFor(Train train, Section fromSection, Frame fromFrame, Frame toFrame, boolean canProcess){
        if(fromSection.isInSection(toFrame)) return fromSection;

        if(fromFrame.getTrack() != toFrame.getTrack()){
            return getSectionOnDifferentTrack(train, fromSection, toFrame, canProcess);
        }

        final Section subsequentNextSection = fromSection.next(train, canProcess);
        final Section subsequentPreviousSection = fromSection.previous(train);
        if(subsequentNextSection != null){
            Section result = processNextSection(subsequentNextSection, train, fromSection, fromFrame, toFrame, false);
            if(result != null) return result;
        }else if(subsequentPreviousSection != null){
            Section result = processPreviousSection(subsequentPreviousSection, train, fromSection, fromFrame, toFrame);
            if(result != null) return result;
        }

        final BiFunction<Section, Train, Section> relativeFunction = train.isPositiveDrivingDirection()
                ? Section::next
                : Section::previous;
        Section found = findSectionBySearchingRelative(train, toFrame, fromSection, relativeFunction);

//        if(found == null){
//            Collection<Section> neighbors = fromSection.allPreviousSections(train);
//            Bukkit.broadcastMessage("Checking");
//            for(Section neighbor : neighbors){
//                Bukkit.broadcastMessage("Checking: " + neighbor);
//                if(neighbor.isInSection(toFrame)){
//                    JRidesPlugin.getLogger().info(LogType.SECTIONS,"Found section in neighbors");
//                    Section result = processNextSection(neighbor, train, fromSection, fromFrame, toFrame, true);
//                    if(result != null) return result;
//                }
//            }
//        }

        if(found == null){
            train.setCrashed(true);
            JRidesPlugin.getLogger().info(LogType.CRASH,
                    "Section not found!: " + fromSection + " from " + fromFrame + " to: " + toFrame);
            sendTrainCrashMessage(train, fromSection, fromFrame);
            throw new SectionNotFoundException(train);
        }else{
            JRidesPlugin.getLogger().info(LogType.SECTIONS,
                    "Section found when searched!");
        }

        return found;
    }

    private Section processNextSection(Section subsequentNextSection, Train train, Section fromSection, Frame fromFrame, Frame toFrame, boolean forceJump){
        if(subsequentNextSection.isInSection(toFrame) && !forceJump) {
            JRidesPlugin.getLogger().info(LogType.SECTIONS, "isnext!");
            return subsequentNextSection;
        } else if((fromSection.isInSection(fromFrame) && train.getDirection() == TrackEnd.END)
                || fromSection.shouldJumpAtEnd()){
            Frame nextStartFrame = subsequentNextSection.getStartFrame();
            int overshotFrameAmount = getOvershotFrameAmount(train, fromSection, toFrame);
            int newFrameValue = nextStartFrame.getValue() + overshotFrameAmount;
            JRidesPlugin.getLogger().info(LogType.SECTIONS,
                    "isnext! jumping - to: " + toFrame.getValue() + " over: " + overshotFrameAmount + ", new: " + newFrameValue);

            toFrame.updateTo(new SimpleFrame(newFrameValue, nextStartFrame.getTrack(), nextStartFrame.getSection()));
            return subsequentNextSection;
        }
        return null;
    }

    private Section processPreviousSection(Section subsequentPreviousSection, Train train, Section fromSection, Frame fromFrame, Frame toFrame){
        if(subsequentPreviousSection.isInSection(toFrame)){
            JRidesPlugin.getLogger().info(LogType.SECTIONS, "isprev!");
            return subsequentPreviousSection;
        } else if((fromSection.isInSection(fromFrame) && train.getDirection() == TrackEnd.START)
                || fromSection.shouldJumpAtStart()){
            Frame previousEndFrame = subsequentPreviousSection.getEndFrame();
            int overshotFrameAmount = getOvershotFrameAmount(train, fromSection, toFrame);
            int newFrameValue = previousEndFrame.getValue() + overshotFrameAmount;
            JRidesPlugin.getLogger().info(LogType.SECTIONS,
                    "isprev! jumping - to: " + toFrame.getValue() + " over: " + overshotFrameAmount + ", new: " + newFrameValue);

            toFrame.updateTo(new SimpleFrame(newFrameValue, previousEndFrame.getTrack(), previousEndFrame.getSection()));
            return subsequentPreviousSection;
        }
        return null;
    }

    private int getOvershotFrameAmount(Train train, Section currentSection, Frame toFrame){
        if(train.isPositiveDrivingDirection() && !toFrame.isInvertedFrameAddition()) {
            return toFrame.getValue() - currentSection.getEndFrame().getValue();
        }else{
            return currentSection.getStartFrame().getValue() - toFrame.getValue();
        }
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

            if(compare.isInRawFrameRange(frame)) found = compare;

            i++;
        }

        return found;
    }

    public Section getSectionOnDifferentTrack(Train train, Section currentSection, Frame toFrame, boolean canProcess){
        Track newTrack = toFrame.getTrack();
        List<Section> newTrackSections = newTrack.getSections();

        // If rolling forwards
        Section logicalNextSection = currentSection.next(train, canProcess);
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
