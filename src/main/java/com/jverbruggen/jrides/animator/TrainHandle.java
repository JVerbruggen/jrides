package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Cart;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SectionProvider;
import org.bukkit.Bukkit;

import java.util.Map;

public class TrainHandle {
    private Train train;
    private Vector3 currentLocation;
    private Frame currentMassMiddleFrame;
    private Track track;
    private Speed speedBPS;
    private TrackBehaviour trackBehaviour;
    private final SectionProvider sectionProvider;

    public TrainHandle(SectionProvider sectionProvider, Train train, Track track, Frame startOffsetFrame) {
        this.sectionProvider = sectionProvider;
        this.train = train;
        this.currentMassMiddleFrame = startOffsetFrame;
        this.track = track;
        this.currentLocation = track.getRawPositions().get(currentMassMiddleFrame.getValue()).toVector3();
        this.trackBehaviour = train.getCurrentSection().getTrackBehaviour();
        this.speedBPS = new Speed(0);
    }

    public void tick(){
        if(train.isCrashed()) return;

        Bukkit.broadcastMessage("Tick " + train.getName() + " on " + train.getHeadOfTrainFrame());

        Section newSection = sectionProvider.getSectionFor(train, train.getHeadOfTrainFrame());
        if(newSection != null){
            if(newSection.isOccupied()){
                train.setCrashed(true);
                Bukkit.broadcastMessage("Train " + train.getName() + " has crashed!");
                Bukkit.broadcastMessage(train.getCurrentSection().toString());
                Bukkit.broadcastMessage(newSection.toString());
                return;
            }

            Section currentSection = train.getCurrentSection();
            currentSection.setOccupation(null);
            currentSection.getTrackBehaviour().trainExitedAtEnd();

            newSection.setOccupation(train);
            train.setCurrentSection(newSection);
            trackBehaviour = newSection.getTrackBehaviour();
        }

        TrainMovement result = trackBehaviour.move(currentMassMiddleFrame, speedBPS, currentLocation, train.getCarts(), track);
        speedBPS = result.getNewSpeed();
        currentMassMiddleFrame.updateTo(result.getNewMassMiddleFrame());

        for(Map.Entry<Cart, CartMovement> cartMovement : result.getCartMovements()){
            Cart cart = cartMovement.getKey();
            CartMovement movement = cartMovement.getValue();
            cart.setPosition(movement);
        }

        currentLocation = result.getNewTrainLocation();
    }
}
