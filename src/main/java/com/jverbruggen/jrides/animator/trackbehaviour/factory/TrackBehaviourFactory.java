package com.jverbruggen.jrides.animator.trackbehaviour.factory;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.*;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.BlockSectionSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.SectionConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.StationEffectsConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.StationSpecConfig;
import com.jverbruggen.jrides.config.gates.GateOwnerConfigSpec;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.SimpleDispatchLock;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.RestraintTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.EffectTriggerFactory;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.FrameRange;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
import com.jverbruggen.jrides.models.properties.factory.FrameFactory;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.gate.Gate;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class TrackBehaviourFactory {
    private final CartMovementFactory cartMovementFactory;
    private final FrameFactory frameFactory;
    private final EffectTriggerFactory effectTriggerFactory;

    public TrackBehaviourFactory() {
        this.cartMovementFactory = ServiceProvider.getSingleton(CartMovementFactory.class);
        this.frameFactory = ServiceProvider.getSingleton(FrameFactory.class);
        this.effectTriggerFactory = ServiceProvider.getSingleton(EffectTriggerFactory.class);
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

    public TrackBehaviour getStationBehaviour(Frame blockBrakeEngageFrame, CoasterHandle coasterHandle, SectionConfig sectionConfig, GateOwnerConfigSpec gateSpec){
        int stationNr = coasterHandle.getStationHandles().size() + 1;
        String rideIdentifier = coasterHandle.getRide().getIdentifier();
        String stationName = rideIdentifier + "_station_" + stationNr;

        DispatchLockCollection dispatchLockCollection = new DispatchLockCollection("Main locks");

        DispatchLock trainInStationDispatchLock = new SimpleDispatchLock(dispatchLockCollection, "No train present in station", true);
        DispatchLock blockSectionOccupiedDispatchLock = new SimpleDispatchLock(dispatchLockCollection, "Next block section is occupied", true);
        DispatchLock minimumWaitTimeDispatchLock = new SimpleDispatchLock(dispatchLockCollection, "Waiting time has not passed yet", true);
        DispatchLock restraintLock = new SimpleDispatchLock(dispatchLockCollection, "Restraints are not closed", true);

        List<Gate> gates = new ArrayList<>();
//        List<GateConfig> gateConfigs = gateSpec.getGateSpecConfigEntry().getGates();
//        for(int i = 0; i < gateConfigs.size(); i++){
//            GateConfig gateConfig = gateConfigs.get(i);
//            String gateName = stationName + "_gate_" + i;
//            Vector3 location = gateConfig.getLocation();
//            gates.add(new FenceGate(gateName, new DispatchLock(dispatchLockCollection, "Gate " + gateName + " is open"), location));
//        }

        TriggerContext triggerContext = new TriggerContext(
                dispatchLockCollection,
                new DispatchTrigger(dispatchLockCollection),
                null,
                new RestraintTrigger(restraintLock));

        StationSpecConfig stationSpecConfig = sectionConfig.getStationSectionSpec();
        int minimumWaitingTime = stationSpecConfig.getMinimumWaitIntervalSeconds();
        int maximumWaitingTime = stationSpecConfig.getMaximumWaitIntervalSeconds();

        StationEffectsConfig stationEffectsConfig = stationSpecConfig.getStationEffectsConfig();
        List<EffectTrigger> entryEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(rideIdentifier, stationEffectsConfig.getEntryEffects());
        List<EffectTrigger> exitEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(rideIdentifier, stationEffectsConfig.getExitEffects());

        MinMaxWaitingTimer waitingTimer = new MinMaxWaitingTimer(minimumWaitingTime, maximumWaitingTime, minimumWaitTimeDispatchLock);

        StationHandle stationHandle = new StationHandle(coasterHandle, stationName, triggerContext, gates, waitingTimer,
                entryEffectTriggers, exitEffectTriggers);

        return new StationTrackBehaviour(coasterHandle, cartMovementFactory, blockBrakeEngageFrame, true, triggerContext,
                stationHandle, trainInStationDispatchLock, blockSectionOccupiedDispatchLock, restraintLock);
    }

    public TrackBehaviour getTrackBehaviourFor(CoasterHandle coasterHandle, CoasterConfig coasterConfig, SectionConfig sectionConfig, int totalFrames){
        String type = sectionConfig.getType();
        TrackConfig trackConfig = coasterConfig.getTrack();

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
            String stationIdentifier = stationSectionSpecConfig.getIdentifier();
            GateOwnerConfigSpec gateSpec = coasterConfig.getGates().getGateOwnerSpec(stationIdentifier);

            double engagePercentage = stationSectionSpecConfig.getEngage();
            int globalOffset = trackConfig.getOffset();
            Frame lowerRange = new SimpleFrame(sectionConfig.getLowerRange() + globalOffset);
            Frame upperRange = new SimpleFrame(sectionConfig.getUpperRange() + globalOffset);

            Frame blockBrakeEngageFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);
            return getStationBehaviour(blockBrakeEngageFrame, coasterHandle, sectionConfig, gateSpec);
        }

        throw new RuntimeException("Unknown section type " + type);
    }
}
