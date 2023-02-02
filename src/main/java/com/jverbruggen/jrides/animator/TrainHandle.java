package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.effect.EffectTriggerCollection;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.properties.TrainEnd;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SectionProvider;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.Set;

public class TrainHandle {
    private Train train;
    private Track track;
    private Speed speedBPS;
    private TrackBehaviour trackBehaviour;
    private final SectionProvider sectionProvider;
    private CoasterHandle coasterHandle;

    private boolean hasEffects;
    private EffectTriggerHandle nextEffect;

    public TrainHandle(SectionProvider sectionProvider, Train train, Track track) {
        this.sectionProvider = sectionProvider;
        this.train = train;
        this.track = track;
        this.trackBehaviour = train.getHeadSection().getTrackBehaviour();
        this.speedBPS = new Speed(0);
        this.coasterHandle = null;

        this.nextEffect = null;
        this.hasEffects = false;

        this.train.setHandle(this);
    }

    public void resetEffects(){
        if(!hasEffects) return;

        nextEffect = coasterHandle.getEffectTriggerCollection().first();
    }

    private void sectionLogic(Section fromSection, Section toSection, boolean goingForward, boolean applyNewBehaviour){
        // If the section it is entering is occupied by some train
        if(toSection.isOccupied()){
            // If that train is a different train
            if(!toSection.getOccupiedBy().equals(this.train)){
                // .. crash
                train.setCrashed(true);
                JRidesPlugin.getLogger().warning(LogType.CRASH, "Train " + train + " has crashed!");
                JRidesPlugin.getLogger().warning(LogType.CRASH, train.getHeadSection().toString());
                JRidesPlugin.getLogger().warning(LogType.CRASH, toSection.toString());
                // else if that train is self
            }else{
                if(applyNewBehaviour) trackBehaviour = toSection.getTrackBehaviour();
//                Bukkit.broadcastMessage("From " + fromSection + ", to " + toSection + ", spans:" + fromSection.spansOver(train));
                if(!fromSection.spansOver(train)){
                    fromSection.removeOccupation(train);
                    train.removeCurrentSection(fromSection);
                    fromSection.getTrackBehaviour().trainExitedAtEnd();
                }
            }
        // else if the section is free
        }else{
            // .. occupy it
            TrainEnd trainEnd = goingForward ? TrainEnd.HEAD : TrainEnd.TAIL;
            toSection.addOccupation(train);
            train.addCurrentSection(toSection, trainEnd);
            if(applyNewBehaviour){
                trackBehaviour = toSection.getTrackBehaviour();
            }
        }
    }

    public void tick(){
        if(train.isCrashed()) return;

        // --- Fetch current/old sections (before move operation)
        Section fromHeadSection = train.getHeadSection();
        Section fromTailSection = train.getTailSection();
        Frame fromHeadFrame = train.getHeadOfTrainFrame();
        Frame fromTailFrame = train.getTailOfTrainFrame();

        // --- Calculate movement that should be applied to the train
        TrainMovement result = trackBehaviour.move(speedBPS, this, track);
        if(result == null) return;

        // --- Apply movement: new head frame and speed
        speedBPS = result.getNewSpeed();
        train.getHeadOfTrainFrame().updateTo(result.getNewHeadOfTrainFrame());

        // --- Move carts according to instructions
        Set<Map.Entry<Cart, CartMovement>> cartMovements = result.getCartMovements();
        if(cartMovements != null){
            for(Map.Entry<Cart, CartMovement> cartMovement : cartMovements){
                Cart cart = cartMovement.getKey();
                CartMovement movement = cartMovement.getValue();
                cart.setPosition(movement);
            }
        }

        // --- Set new train location according to new frames
        Vector3 headLocation = track.getLocationFor(result.getNewHeadOfTrainFrame());
        Vector3 middleLocation = result.getNewTrainLocation();
        Vector3 tailLocation = track.getLocationFor(result.getNewTailOfTrainFrame());
        train.setCurrentLocation(headLocation, middleLocation, tailLocation);

        // --- Section occupations
        // ---   Calculate new head section occupation
        Section toHeadSection = sectionProvider.getSectionFor(train, fromHeadSection, fromHeadFrame, train.getHeadOfTrainFrame());
        if(toHeadSection != fromHeadSection){
            sectionLogic(fromHeadSection, toHeadSection, speedBPS.isPositive(), true);
        }

        // ---   Calculate new tail section occupation
        Section toTailSection = sectionProvider.getSectionFor(train, fromTailSection, fromTailFrame, train.getTailOfTrainFrame());
        if(toTailSection != fromTailSection){
            sectionLogic(fromTailSection, toTailSection, speedBPS.isPositive(), false);
        }

        // Check if the next effect should be played yet
        while(nextEffect != null){
            Frame nextEffectFrame = nextEffect.getFrame();
            boolean activateEffect = train.getHeadSection().hasPassed(nextEffectFrame, result.getNewHeadOfTrainFrame());
            if(!activateEffect) break;
            nextEffect.execute(train);
            nextEffect = nextEffect.next();
        }
    }

    public Train getTrain() {
        return train;
    }

    public void setCoasterHandle(CoasterHandle coasterHandle) {
        this.coasterHandle = coasterHandle;

        EffectTriggerCollection effectTriggerCollection = coasterHandle.getEffectTriggerCollection();
        if(effectTriggerCollection != null && effectTriggerCollection.size() > 0)
            hasEffects = true;
    }

    public CoasterHandle getCoasterHandle() {
        return coasterHandle;
    }
}
