package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.ride.coaster.Cart;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.SectionProvider;

import java.util.Map;

public class TrainHandle {
    private Train train;
    private Vector3 currentLocation;
    private int currentMassMiddleFrame;
    private Track track;
    private Speed speedBPS;
    private TrackBehaviour trackBehaviour;
    private final SectionProvider sectionProvider;

    public TrainHandle(SectionProvider sectionProvider, Train train, Track track, int stationIndexOffset) {
        this.sectionProvider = sectionProvider;
        this.train = train;
        this.currentMassMiddleFrame = stationIndexOffset;
        this.track = track;
        this.currentLocation = track.getRawPositions().get(currentMassMiddleFrame).toVector3();
        this.trackBehaviour = train.getCurrentSection().getTrackBehaviour();
        this.speedBPS = new Speed(0);
    }

    public void tick(){
        Section section = sectionProvider.getSectionFor(train, currentMassMiddleFrame);
        if(section != null){
            train.getCurrentSection().getTrackBehaviour().trainExitedAtEnd();
            train.setCurrentSection(section);
            this.trackBehaviour = section.getTrackBehaviour();
        }

        TrainMovement result = trackBehaviour.move(currentMassMiddleFrame, speedBPS, currentLocation, train.getCarts(), track);
        speedBPS = result.getNewSpeed();
        currentMassMiddleFrame = result.getNewMassMiddleFrame();

        for(Map.Entry<Cart, CartMovement> cartMovement : result.getCartMovements()){
            Cart cart = cartMovement.getKey();
            CartMovement movement = cartMovement.getValue();
            cart.setPosition(movement);
        }

        currentLocation = result.getNewTrainLocation();
    }
}
