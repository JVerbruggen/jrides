package com.jverbruggen.jrides.animator.trackbehaviour.factory;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.*;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.BlockSectionSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.SectionConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.StationEffectsConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.StationSpecConfig;
import com.jverbruggen.jrides.config.gates.GateConfig;
import com.jverbruggen.jrides.config.gates.GateOwnerConfigSpec;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.SimpleDispatchLock;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.GateTrigger;
import com.jverbruggen.jrides.control.trigger.RestraintTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.EffectTriggerFactory;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.FrameRange;
import com.jverbruggen.jrides.models.properties.MinMaxWaitingTimer;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.gate.FenceGate;
import com.jverbruggen.jrides.models.ride.gate.Gate;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class TrackBehaviourFactory {
    private final CartMovementFactory cartMovementFactory;
    private final EffectTriggerFactory effectTriggerFactory;
    private final LanguageFile languageFile;

    public TrackBehaviourFactory() {
        this.cartMovementFactory = ServiceProvider.getSingleton(CartMovementFactory.class);
        this.effectTriggerFactory = ServiceProvider.getSingleton(EffectTriggerFactory.class);
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
    }

    public TrackBehaviour getTrackBehaviour(double gravityConstant, double dragConstant){
        return new FreeMovementTrackBehaviour(cartMovementFactory, gravityConstant, dragConstant);
    }

    public TrackBehaviour getBlockBrakeBehaviour(Frame blockBrakeEngageFrame, boolean canSpawn, double driveSpeed){
        return new BlockBrakeTrackBehaviour(cartMovementFactory, blockBrakeEngageFrame, canSpawn, driveSpeed);
    }

    public TrackBehaviour getStationBehaviour(Frame blockBrakeEngageFrame, CoasterHandle coasterHandle, SectionConfig sectionConfig, GateOwnerConfigSpec gateSpec){
        int stationNr = coasterHandle.getStationHandles().size() + 1;
        String rideIdentifier = coasterHandle.getRide().getIdentifier();
        String stationName = rideIdentifier + "_station_" + stationNr;
        World world = JRidesPlugin.getWorld();

        DispatchLockCollection dispatchLockCollection = new DispatchLockCollection("Main locks");

        DispatchLock trainInStationDispatchLock = new SimpleDispatchLock(dispatchLockCollection, languageFile.notificationRideNoTrainPresent, true);
        DispatchLock blockSectionOccupiedDispatchLock = new SimpleDispatchLock(dispatchLockCollection, languageFile.notificationRideNextBlockOccupied, true);
        DispatchLock minimumWaitTimeDispatchLock = new SimpleDispatchLock(dispatchLockCollection, languageFile.notificationRideWaitingTime, true);
        DispatchLock restraintLock = new SimpleDispatchLock(dispatchLockCollection, languageFile.notificationRideRestraintsNotClosed, true);
        DispatchLockCollection gatesGenericLock = new DispatchLockCollection(languageFile.notificationRideGatesNotClosed, dispatchLockCollection);

        List<Gate> gates = new ArrayList<>();
        List<GateConfig> gateConfigs = gateSpec.getGateSpecConfigEntry().getGates();
        for(int i = 0; i < gateConfigs.size(); i++){
            GateConfig gateConfig = gateConfigs.get(i);
            String gateName = stationName + "_gate_" + i;
            Vector3 location = gateConfig.getLocation();
            gates.add(new FenceGate(gateName,
                    new SimpleDispatchLock(gatesGenericLock, languageFile.notificationRideGateNotClosed, false),
                    location.toBukkitLocation(world).getBlock()));
        }

        TriggerContext triggerContext = new TriggerContext(
                dispatchLockCollection,
                new DispatchTrigger(dispatchLockCollection),
                new GateTrigger(gatesGenericLock),
                new RestraintTrigger(restraintLock));

        StationSpecConfig stationSpecConfig = sectionConfig.getStationSectionSpec();
        int minimumWaitingTime = stationSpecConfig.getMinimumWaitIntervalSeconds();
        int maximumWaitingTime = stationSpecConfig.getMaximumWaitIntervalSeconds();

        double driveSpeed = stationSpecConfig.getDriveSpeed();

        StationEffectsConfig stationEffectsConfig = stationSpecConfig.getStationEffectsConfig();
        List<EffectTriggerHandle> entryEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(rideIdentifier, stationEffectsConfig.getEntryEffects());
        List<EffectTriggerHandle> exitEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(rideIdentifier, stationEffectsConfig.getExitEffects());

        MinMaxWaitingTimer waitingTimer = new MinMaxWaitingTimer(minimumWaitingTime, maximumWaitingTime, minimumWaitTimeDispatchLock);

        StationHandle stationHandle = new StationHandle(coasterHandle, stationName, triggerContext, gates, waitingTimer,
                entryEffectTriggers, exitEffectTriggers);

        return new StationTrackBehaviour(coasterHandle, cartMovementFactory, blockBrakeEngageFrame, true, triggerContext,
                stationHandle, trainInStationDispatchLock, blockSectionOccupiedDispatchLock, restraintLock, driveSpeed);
    }

    public TrackBehaviour getTrackBehaviourFor(CoasterHandle coasterHandle, CoasterConfig coasterConfig, SectionConfig sectionConfig, int totalFrames){
        String type = sectionConfig.getType();
        TrackConfig trackConfig = coasterConfig.getTrack();

        if(type.equalsIgnoreCase("track")){
            double gravityConstant = coasterConfig.getGravityConstant();
            double dragConstant = coasterConfig.getDragConstant();
            return getTrackBehaviour(gravityConstant, dragConstant);
        }else if(type.equalsIgnoreCase("blocksection")){
            BlockSectionSpecConfig blockSectionSpecConfig = sectionConfig.getBlockSectionSpec();
            double engagePercentage = blockSectionSpecConfig.getEngage();
            int globalOffset = trackConfig.getOffset();
            Frame lowerRange = new SimpleFrame(sectionConfig.getLowerRange() + globalOffset);
            Frame upperRange = new SimpleFrame(sectionConfig.getUpperRange() + globalOffset);
            boolean canSpawn = blockSectionSpecConfig.canSpawn();
            double driveSpeed = blockSectionSpecConfig.getDriveSpeed();

            Frame blockBrakeEngageFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);
            return getBlockBrakeBehaviour(blockBrakeEngageFrame, canSpawn, driveSpeed);
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

        JRidesPlugin.getLogger().severe("Unknown section type " + type);
        return null;
    }
}
