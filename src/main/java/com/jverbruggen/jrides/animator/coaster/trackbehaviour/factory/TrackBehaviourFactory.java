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

package com.jverbruggen.jrides.animator.coaster.trackbehaviour.factory;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.FreeMovementTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.LaunchTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.station.StationTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.drive.*;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.transfer.TrainDisplacerTransferTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.point.SwitchBehaviour;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.base.PointSectionConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.base.RangedSectionConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.point.SwitchSectionSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.ranged.*;
import com.jverbruggen.jrides.config.coaster.objects.section.ranged.transfer.TransferSectionPositionSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.ranged.transfer.TransferSectionSpecConfig;
import com.jverbruggen.jrides.config.gates.GateOwnerConfigSpec;
import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.control.SimpleDispatchLock;
import com.jverbruggen.jrides.control.trigger.*;
import com.jverbruggen.jrides.effect.EffectTriggerFactory;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.FrameRange;
import com.jverbruggen.jrides.models.properties.frame.SimpleFrame;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.RideType;
import com.jverbruggen.jrides.models.ride.coaster.trackswitch.SwitchPosition;
import com.jverbruggen.jrides.models.ride.coaster.transfer.Transfer;
import com.jverbruggen.jrides.models.ride.coaster.transfer.TransferPosition;
import com.jverbruggen.jrides.models.ride.gate.Gate;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrackBehaviourFactory {
    private final CartMovementFactory cartMovementFactory;
    private final EffectTriggerFactory effectTriggerFactory;
    private final LanguageFile languageFile;
    private final ViewportManager viewportManager;

    public TrackBehaviourFactory() {
        this.cartMovementFactory = ServiceProvider.getSingleton(CartMovementFactory.class);
        this.effectTriggerFactory = ServiceProvider.getSingleton(EffectTriggerFactory.class);
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
    }

    public TrackBehaviour getTrackBehaviour(double gravityConstant, double dragConstant){
        return new FreeMovementTrackBehaviour(cartMovementFactory, gravityConstant, dragConstant);
    }

    public TrackBehaviour getBlockBrakeBehaviour(RangedSectionConfig rangedSectionConfig, int totalFrames){
        BlockSectionSpecConfig blockSectionSpecConfig = rangedSectionConfig.getBlockSectionSpec();
        double engagePercentage = blockSectionSpecConfig.getEngage();
        Frame lowerRange = new SimpleFrame(rangedSectionConfig.getLowerRange());
        Frame upperRange = new SimpleFrame(rangedSectionConfig.getUpperRange());
        boolean canSpawn = blockSectionSpecConfig.canSpawn();
        double driveSpeed = blockSectionSpecConfig.getDriveSpeed();
        double acceleration = blockSectionSpecConfig.getAcceleration();
        double deceleration = blockSectionSpecConfig.getDeceleration();
        int minWaitTicks = blockSectionSpecConfig.getMinWaitTicks();

        Frame blockBrakeEngageFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);

        return new BlockBrakeTrackBehaviour(cartMovementFactory, blockBrakeEngageFrame, canSpawn, driveSpeed, deceleration, acceleration, minWaitTicks);
    }

    public TrackBehaviour getLaunchBehaviour(CoasterHandle coasterHandle, Frame engageFrame, boolean canSpawn, double driveSpeed, double acceleration, double deceleration, boolean isForwardsLaunch, int waitTicks, double launchAcceleration, double launchSpeed, double launchSpeedBackward, List<String> launchEffectStrings){
        String rideIdentifier = coasterHandle.getRide().getIdentifier();
        List<TrainEffectTriggerHandle> launchEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(RideType.COASTER, rideIdentifier, launchEffectStrings);

        return new LaunchTrackBehaviour(cartMovementFactory, canSpawn, driveSpeed, deceleration, acceleration, isForwardsLaunch, waitTicks, engageFrame, launchAcceleration, launchSpeed, launchSpeedBackward, launchEffectTriggers);
    }

    public TrackBehaviour getStationBehaviour(Frame blockBrakeEngageFrame, CoasterHandle coasterHandle, RangedSectionConfig rangedSectionConfig, GateOwnerConfigSpec gateSpec){
        String rideIdentifier = coasterHandle.getRide().getIdentifier();
        String shortStationName = rangedSectionConfig.getIdentifier();
        String stationName = rideIdentifier + "_station_" + shortStationName;
        World world = JRidesPlugin.getWorld();

        DispatchLockCollection dispatchLockCollection = new DispatchLockCollection("Main locks");

        DispatchLock trainInStationDispatchLock = new SimpleDispatchLock(dispatchLockCollection,
                languageFile.get(LanguageFileField.NOTIFICATION_RIDE_NO_TRAIN_PRESENT), true);
        DispatchLock blockSectionOccupiedDispatchLock = new SimpleDispatchLock(dispatchLockCollection,
                languageFile.get(LanguageFileField.NOTIFICATION_RIDE_NEXT_BLOCK_OCCUPIED), true);
        DispatchLock minimumWaitTimeDispatchLock = new SimpleDispatchLock(dispatchLockCollection,
                languageFile.get(LanguageFileField.NOTIFICATION_RIDE_WAITING_TIME), true);
        DispatchLock restraintLock = new SimpleDispatchLock(dispatchLockCollection,
                languageFile.get(LanguageFileField.NOTIFICATION_RIDE_RESTRAINTS_NOT_CLOSED), true);
        DispatchLockCollection gatesGenericLock = new DispatchLockCollection(
                languageFile.get(LanguageFileField.NOTIFICATION_RIDE_GATES_NOT_CLOSED), dispatchLockCollection);

        List<Gate> gates = gateSpec != null
                ? gateSpec.createGates(stationName, world, gatesGenericLock)
                : new ArrayList<>();

        TriggerContext triggerContext = new TriggerContext(
                dispatchLockCollection,
                trainInStationDispatchLock,
                new SimpleDispatchTrigger(dispatchLockCollection),
                new GateTrigger(gatesGenericLock),
                new RestraintTrigger(restraintLock));

        CoasterStationConfig coasterStationConfig = rangedSectionConfig.getStationSectionSpec();
        double driveSpeed = coasterStationConfig.getDriveSpeed();
        double acceleration = coasterStationConfig.getAcceleration();
        double deceleration = coasterStationConfig.getDeceleration();

        CoasterStationHandle stationHandle = coasterStationConfig.createStationHandle(
                stationName, shortStationName, triggerContext, coasterHandle,
                gates, minimumWaitTimeDispatchLock);

        return new StationTrackBehaviour(coasterHandle, cartMovementFactory, blockBrakeEngageFrame, coasterStationConfig.canSpawn(), triggerContext,
                stationHandle, trainInStationDispatchLock, blockSectionOccupiedDispatchLock, restraintLock, driveSpeed, acceleration, deceleration,
                coasterStationConfig.isForwardsDispatch(), coasterStationConfig.getPassThroughCount());
    }

    private TrackBehaviour getSwitchBehaviour(SwitchSectionSpecConfig switchSectionSpecConfig) {
        List<SwitchPosition> destinations = switchSectionSpecConfig.getDestinations()
                .stream()
                .map(SwitchPosition::new)
                .collect(Collectors.toList());
        SwitchPosition origin = new SwitchPosition(switchSectionSpecConfig.getOrigin());

        return new SwitchBehaviour(cartMovementFactory, destinations, origin);
    }

    public TrackBehaviour getTrackBehaviourFor(CoasterHandle coasterHandle, CoasterConfig coasterConfig, RangedSectionConfig rangedSectionConfig, int totalFrames){
        String type = rangedSectionConfig.getType();
        String identifier = rangedSectionConfig.getIdentifier();

        if(type.equalsIgnoreCase("track")){
            double gravityConstant = coasterConfig.getGravityConstant();
            double dragConstant = coasterConfig.getDragConstant();
            return getTrackBehaviour(gravityConstant, dragConstant);
        }else if(type.equalsIgnoreCase("blocksection")){
            return getBlockBrakeBehaviour(rangedSectionConfig, totalFrames);
        }else if(type.equalsIgnoreCase("station")){
            CoasterStationConfig stationSectionSpecConfig = rangedSectionConfig.getStationSectionSpec();
            GateOwnerConfigSpec gateSpec = coasterConfig.getGates().getGateOwnerSpec(identifier).orElse(null);

            double engagePercentage = stationSectionSpecConfig.getEngage();
            Frame lowerRange = new SimpleFrame(rangedSectionConfig.getLowerRange());
            Frame upperRange = new SimpleFrame(rangedSectionConfig.getUpperRange());

            Frame blockBrakeEngageFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);
            return getStationBehaviour(blockBrakeEngageFrame, coasterHandle, rangedSectionConfig, gateSpec);
        }else if(type.equalsIgnoreCase("brake")){
            BrakeSectionSpecConfig brakeSectionSpecConfig = rangedSectionConfig.getBrakeSectionSpec();
            double driveSpeed = brakeSectionSpecConfig.getDriveSpeed();
            double deceleration = brakeSectionSpecConfig.getDeceleration();
            return new BrakeAndDriveTrackBehaviour(cartMovementFactory, false, driveSpeed, deceleration, deceleration);
        }else if(type.equalsIgnoreCase("trim")){
            TrimBrakeSectionSpecConfig trimBrakeSectionSpecConfig = rangedSectionConfig.getTrimBrakeSectionSpecConfig();
            double dragConstantWithTrim = trimBrakeSectionSpecConfig.getTrimResistanceConstant() * coasterConfig.getDragConstant();
            return new TrimBrakeTrackBehaviour(cartMovementFactory, coasterConfig.getGravityConstant(), dragConstantWithTrim);
        }else if(type.equalsIgnoreCase("drive")){
            DriveSectionSpecConfig driveSectionSpecConfig = rangedSectionConfig.getDriveSectionSpec();
            boolean ignoreDirection = driveSectionSpecConfig.isIgnoreDirection();
            double driveSpeed = driveSectionSpecConfig.getDriveSpeed();
            double acceleration = driveSectionSpecConfig.getAcceleration();
            double deceleration = driveSectionSpecConfig.getDeceleration();
            return new BrakeAndDriveTrackBehaviour(cartMovementFactory, ignoreDirection, driveSpeed, deceleration, acceleration);
        }else if(type.equalsIgnoreCase("proximityDrive")){
            ProximityDriveSectionSpecConfig driveSectionSpecConfig = rangedSectionConfig.getProximityDriveSectionSpec();
            boolean canSpawn = driveSectionSpecConfig.canSpawn();
            double driveSpeed = driveSectionSpecConfig.getDriveSpeed();
            double acceleration = driveSectionSpecConfig.getAcceleration();
            double deceleration = driveSectionSpecConfig.getDeceleration();
            int minTrainDistance = driveSectionSpecConfig.getMinTrainDistance();

            Frame lowerRange = new SimpleFrame(rangedSectionConfig.getLowerRange());
            Frame upperRange = new SimpleFrame(rangedSectionConfig.getUpperRange());
            Frame stopFrame = Frame.getDistanceFromUpperFrame(lowerRange, upperRange, 10);

            return new ProximityDriveTrackBehaviour(cartMovementFactory, canSpawn, driveSpeed, deceleration, acceleration, minTrainDistance, stopFrame);
        }else if(type.equalsIgnoreCase("driveAndRelease")){
            DriveAndReleaseSectionSpecConfig driveAndReleaseSectionSpecConfig = rangedSectionConfig.getDriveAndReleaseSectionSpecConfig();
            double driveSpeed = driveAndReleaseSectionSpecConfig.getDriveSpeed();
            double acceleration = driveAndReleaseSectionSpecConfig.getAcceleration();
            double deceleration = driveAndReleaseSectionSpecConfig.getDeceleration();

            double engagePercentage = driveAndReleaseSectionSpecConfig.getEngage();
            Frame lowerRange = new SimpleFrame(rangedSectionConfig.getLowerRange());
            Frame upperRange = new SimpleFrame(rangedSectionConfig.getUpperRange());

            Frame stopFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);

            int minWaitTicks = driveAndReleaseSectionSpecConfig.getWaitTicks();

            return new DriveAndReleaseTrackBehaviour(cartMovementFactory, driveSpeed, deceleration, acceleration,
                    stopFrame, minWaitTicks, coasterConfig.getGravityConstant(), coasterConfig.getDragConstant());
        }else if(type.equalsIgnoreCase("driveStopDrive")){
            DriveStopDriveSectionSpecConfig driveStopDriveSectionSpecConfig = rangedSectionConfig.getDriveStopDriveSectionSpecConfig();
            double driveSpeedIn = driveStopDriveSectionSpecConfig.getDriveSpeedIn();
            double driveSpeedOut = driveStopDriveSectionSpecConfig.getDriveSpeedOut();
            double acceleration = driveStopDriveSectionSpecConfig.getAcceleration();
            double deceleration = driveStopDriveSectionSpecConfig.getDeceleration();

            double engagePercentage = driveStopDriveSectionSpecConfig.getEngage();
            Frame lowerRange = new SimpleFrame(rangedSectionConfig.getLowerRange());
            Frame upperRange = new SimpleFrame(rangedSectionConfig.getUpperRange());

            Frame stopFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);

            int minWaitTicks = driveStopDriveSectionSpecConfig.getWaitTicks();

            return new DriveStopDriveTrackBehaviour(cartMovementFactory, minWaitTicks, stopFrame, deceleration, acceleration,
                    driveSpeedIn, driveSpeedOut);
        }else if(type.equalsIgnoreCase("launch")){
            LaunchSectionSpecConfig launchSectionSpecConfig = rangedSectionConfig.getLaunchSectionSpecConfig();
            double engagePercentage = launchSectionSpecConfig.getEngage();
            Frame lowerRange = new SimpleFrame(rangedSectionConfig.getLowerRange());
            Frame upperRange = new SimpleFrame(rangedSectionConfig.getUpperRange());
            Frame engageFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);

            boolean canSpawn = launchSectionSpecConfig.canSpawn();
            double driveSpeed = launchSectionSpecConfig.getDriveSpeed();
            double acceleration = launchSectionSpecConfig.getAcceleration();
            double deceleration = launchSectionSpecConfig.getDeceleration();
            boolean isForwardsLaunch = launchSectionSpecConfig.isForwardsLaunch();
            int waitTicks = launchSectionSpecConfig.getWaitTicks();
            double launchAcceleration = launchSectionSpecConfig.getLaunchAcceleration();
            double launchSpeed = launchSectionSpecConfig.getLaunchSpeed();
            double launchSpeedBackward = launchSectionSpecConfig.getLaunchSpeedBackward();
            List<String> launchEffectsString = launchSectionSpecConfig.getLaunchEffectsConfig().getLaunchEffects();

            return getLaunchBehaviour(coasterHandle, engageFrame, canSpawn, driveSpeed, acceleration, deceleration, isForwardsLaunch, waitTicks, launchAcceleration, launchSpeed, launchSpeedBackward, launchEffectsString);
        }else if(type.equalsIgnoreCase("transfer")){
            TransferSectionSpecConfig transferSectionSpecConfig = rangedSectionConfig.getTransferSectionSpec();

            Vector3 origin = transferSectionSpecConfig.getOrigin();
            double engagePercentage = transferSectionSpecConfig.getEngage();
            Frame lowerRange = new SimpleFrame(rangedSectionConfig.getLowerRange());
            Frame upperRange = new SimpleFrame(rangedSectionConfig.getUpperRange());
            Frame engageFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);
            Vector3 modelOffsetPosition = transferSectionSpecConfig.getModelOffsetPosition();
            Vector3 modelOffsetRotation = transferSectionSpecConfig.getModelOffsetRotation();
            double enterDriveSpeed = transferSectionSpecConfig.getEnterDriveSpeed();
            double exitDriveSpeed = transferSectionSpecConfig.getExitDriveSpeed();
            double acceleration = transferSectionSpecConfig.getAcceleration();
            double deceleration = transferSectionSpecConfig.getDeceleration();

            List<TransferPosition> transferPositions = new ArrayList<>();
            for(TransferSectionPositionSpecConfig transferSectionPositionSpecConfig : transferSectionSpecConfig.getPositions()){
                String sectionAtStartReference = transferSectionPositionSpecConfig.getSectionAtStart();
                String sectionAtEndReference = transferSectionPositionSpecConfig.getSectionAtEnd();
                boolean sectionAtStartForwards = transferSectionPositionSpecConfig.isSectionAtStartForwards();
                boolean sectionAtEndForwards = transferSectionPositionSpecConfig.isSectionAtEndForwards();
                boolean sectionAtStartConnectsToStart = transferSectionPositionSpecConfig.isSectionAtStartConnectsToStart();
                boolean sectionAtEndConnectsToStart = transferSectionPositionSpecConfig.isSectionAtEndConnectsToStart();
                Vector3 position = Vector3.add(origin, transferSectionPositionSpecConfig.getPosition());
                Vector3 rotation = transferSectionPositionSpecConfig.getRotation();
                int moveTicks = transferSectionPositionSpecConfig.getMoveTicks();

                Quaternion orientation = Quaternion.fromAnglesVector(rotation);
                transferPositions.add(new TransferPosition(position, orientation, moveTicks, sectionAtStartReference, sectionAtEndReference, sectionAtStartForwards, sectionAtEndForwards, sectionAtStartConnectsToStart, sectionAtEndConnectsToStart));
            }

            TransferPosition originTransferPosition = transferPositions.get(0);

            Quaternion modelOffsetOrientation = originTransferPosition.getOrientation().clone();
            modelOffsetOrientation.rotateYawPitchRoll(modelOffsetRotation);

            VirtualEntity virtualEntity = viewportManager.spawnModelEntity(
                    Vector3.add(originTransferPosition.getLocation(), modelOffsetPosition),
                    new TrainModelItem(ItemStackFactory.getCoasterStack(Material.DIAMOND_HOE, 2, true)));
            virtualEntity.setRotation(modelOffsetOrientation);

            Transfer transfer = new Transfer(transferPositions, virtualEntity, origin, modelOffsetPosition, modelOffsetRotation);
            coasterHandle.addTransfer(transfer);
            coasterHandle.addUnlockable(identifier, transfer);

            return new TrainDisplacerTransferTrackBehaviour(cartMovementFactory, enterDriveSpeed, deceleration, acceleration, exitDriveSpeed, engageFrame, transfer);
        }

        JRidesPlugin.getLogger().severe("Unknown section type " + type);
        return null;
    }

    public TrackBehaviour getTrackBehaviourFor(CoasterHandle coasterHandle, CoasterConfig coasterConfig, PointSectionConfig pointSectionConfig) {
        String type = pointSectionConfig.getType();

        if(type.equalsIgnoreCase("switch")){
            return getSwitchBehaviour(pointSectionConfig.getSwitchSectionSpecConfig());
        }

        throw new RuntimeException("Unknown point section type " + type);
    }
}
