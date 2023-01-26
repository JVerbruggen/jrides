package com.jverbruggen.jrides.animator.trackbehaviour.factory;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.*;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.BlockSectionSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.SectionConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.StationSpecConfig;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.FrameRange;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
import com.jverbruggen.jrides.models.properties.factory.FrameFactory;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.models.ride.StationHandle;

public class TrackBehaviourFactory {
    private final CartMovementFactory cartMovementFactory;
    private final FrameFactory frameFactory;

    public TrackBehaviourFactory(CartMovementFactory cartMovementFactory, FrameFactory frameFactory) {
        this.cartMovementFactory = cartMovementFactory;
        this.frameFactory = frameFactory;
    }

    public TrackBehaviour getBrakeBehaviour(int stopTime){
        return new FullStopAndGoTrackBehaviour(cartMovementFactory, stopTime);
    }

    public TrackBehaviour getTrackBehaviour(){
        return new FreeMovementTrackBehaviour(cartMovementFactory);
    }

    public TrackBehaviour getBlockBrakeBehaviour(Frame blockBrakeEngageFrame, boolean canSpawn){
        return new BlockBrakeTrackBehaviour(cartMovementFactory, blockBrakeEngageFrame, canSpawn);
    }

    public TrackBehaviour getStationBehaviour(Frame blockBrakeEngageFrame, CoasterHandle coasterHandle){
        int stationNr = coasterHandle.getStationHandles().size() + 1;

        DispatchLock trainInStationDispatchLock = new DispatchLock("Train present in station");
        DispatchLock blockSectionOccupiedDispatchLock = new DispatchLock("Next block section is occupied");

        DispatchLockCollection dispatchLockCollection = new DispatchLockCollection();
        dispatchLockCollection.addDispatchLock(trainInStationDispatchLock);
        dispatchLockCollection.addDispatchLock(blockSectionOccupiedDispatchLock);

        StationHandle stationHandle = new StationHandle(coasterHandle.getRide().getIdentifier() + "_station_" + stationNr, dispatchLockCollection);
        coasterHandle.addStationHandle(stationHandle);
        DispatchTrigger dispatchTrigger = stationHandle.getDispatchTrigger();

        return new StationTrackBehaviour(coasterHandle, cartMovementFactory, blockBrakeEngageFrame, true, dispatchTrigger,
                stationHandle, trainInStationDispatchLock, blockSectionOccupiedDispatchLock);
    }

    public TrackBehaviour getTrackBehaviourFor(CoasterHandle coasterHandle, TrackConfig trackConfig, SectionConfig sectionConfig, int totalFrames){
        String type = sectionConfig.getType();

        if(type.equalsIgnoreCase("track")){
            return getTrackBehaviour();
        }else if(type.equalsIgnoreCase("blocksection")){
            BlockSectionSpecConfig blockSectionSpecConfig = sectionConfig.getBlockSectionSpec();
            double engagePercentage = blockSectionSpecConfig.getEngage();
            int globalOffset = trackConfig.getOffset();
            Frame lowerRange = new SimpleFrame(sectionConfig.getLowerRange() + globalOffset);
            Frame upperRange = new SimpleFrame(sectionConfig.getUpperRange() + globalOffset);
            boolean canSpawn = blockSectionSpecConfig.canSpawn();

            Frame blockBrakeEngageFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);
            return getBlockBrakeBehaviour(blockBrakeEngageFrame, canSpawn);
        }else if(type.equalsIgnoreCase("station")){
            StationSpecConfig stationSectionSpecConfig = sectionConfig.getStationSectionSpec();
            double engagePercentage = stationSectionSpecConfig.getEngage();
            int globalOffset = trackConfig.getOffset();
            Frame lowerRange = new SimpleFrame(sectionConfig.getLowerRange() + globalOffset);
            Frame upperRange = new SimpleFrame(sectionConfig.getUpperRange() + globalOffset);

            Frame blockBrakeEngageFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);
            return getStationBehaviour(blockBrakeEngageFrame, coasterHandle);
        }

        throw new RuntimeException("Unknown section type " + type);
    }
}
