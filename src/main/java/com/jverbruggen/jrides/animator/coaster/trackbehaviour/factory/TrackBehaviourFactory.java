package com.jverbruggen.jrides.animator.coaster.trackbehaviour.factory;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.FreeMovementTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.LaunchTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.StationTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.brake.BlockBrakeTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.brake.BrakeAndDriveTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.transfer.TrainDisplacerTransferTrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.point.SwitchBehaviour;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.TrackConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.base.PointSectionConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.base.RangedSectionConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.point.SwitchSectionSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.ranged.*;
import com.jverbruggen.jrides.config.coaster.objects.section.ranged.transfer.TransferSectionPositionSpecConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.ranged.transfer.TransferSectionSpecConfig;
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
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.ArmorStandPose;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.*;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.FrameRange;
import com.jverbruggen.jrides.models.properties.frame.SimpleFrame;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.coaster.trackswitch.SwitchPosition;
import com.jverbruggen.jrides.models.ride.coaster.transfer.Transfer;
import com.jverbruggen.jrides.models.ride.coaster.transfer.TransferPosition;
import com.jverbruggen.jrides.models.ride.gate.FenceGate;
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

    public TrackBehaviour getLaunchBehaviour(CoasterHandle coasterHandle, Frame engageFrame, double driveSpeed, double acceleration, double deceleration, int waitTicks, double launchAcceleration, double launchMaxSpeed, List<String> launchEffectStrings){
        String rideIdentifier = coasterHandle.getRide().getIdentifier();
        List<TrainEffectTriggerHandle> launchEffectTriggers = effectTriggerFactory.getFramelessEffectTriggers(rideIdentifier, launchEffectStrings);

        return new LaunchTrackBehaviour(cartMovementFactory, driveSpeed, deceleration, acceleration, waitTicks, engageFrame, launchAcceleration, launchMaxSpeed, launchEffectTriggers);
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
                new DispatchTrigger(dispatchLockCollection),
                new GateTrigger(gatesGenericLock),
                new RestraintTrigger(restraintLock));

        CoasterStationConfig coasterStationConfig = rangedSectionConfig.getStationSectionSpec();
        double driveSpeed = coasterStationConfig.getDriveSpeed();

        CoasterStationHandle stationHandle = coasterStationConfig.createStationHandle(
                stationName, shortStationName, triggerContext, coasterHandle,
                gates, minimumWaitTimeDispatchLock);

        triggerContext.setParentStation(stationHandle);

        return new StationTrackBehaviour(coasterHandle, cartMovementFactory, blockBrakeEngageFrame, true, triggerContext,
                stationHandle, trainInStationDispatchLock, blockSectionOccupiedDispatchLock, restraintLock, driveSpeed);
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
        TrackConfig trackConfig = coasterConfig.getTrack();
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
            return new BrakeAndDriveTrackBehaviour(cartMovementFactory, driveSpeed, deceleration, deceleration);
        }else if(type.equalsIgnoreCase("drive")){
            DriveSectionSpecConfig driveSectionSpecConfig = rangedSectionConfig.getDriveSectionSpec();
            double driveSpeed = driveSectionSpecConfig.getDriveSpeed();
            double acceleration = driveSectionSpecConfig.getAcceleration();
            double deceleration = driveSectionSpecConfig.getDeceleration();
            return new BrakeAndDriveTrackBehaviour(cartMovementFactory, driveSpeed, deceleration, acceleration);
        }else if(type.equalsIgnoreCase("launch")){
            LaunchSectionSpecConfig launchSectionSpecConfig = rangedSectionConfig.getLaunchSectionSpecConfig();
            double engagePercentage = launchSectionSpecConfig.getEngage();
            Frame lowerRange = new SimpleFrame(rangedSectionConfig.getLowerRange());
            Frame upperRange = new SimpleFrame(rangedSectionConfig.getUpperRange());
            Frame engageFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);

            double driveSpeed = launchSectionSpecConfig.getDriveSpeed();
            double acceleration = launchSectionSpecConfig.getAcceleration();
            double deceleration = launchSectionSpecConfig.getDeceleration();
            int waitTicks = launchSectionSpecConfig.getWaitTicks();
            double launchAcceleration = launchSectionSpecConfig.getLaunchAcceleration();
            double launchMaxSpeed = launchSectionSpecConfig.getLaunchMaxSpeed();
            List<String> launchEffectsString = launchSectionSpecConfig.getLaunchEffectsConfig().getLaunchEffects();

            return getLaunchBehaviour(coasterHandle, engageFrame, driveSpeed, acceleration, deceleration, waitTicks, launchAcceleration, launchMaxSpeed, launchEffectsString);
        }else if(type.equalsIgnoreCase("transfer")){
            TransferSectionSpecConfig transferSectionSpecConfig = rangedSectionConfig.getTransferSectionSpec();

            Vector3 origin = transferSectionSpecConfig.getOrigin();
            double engagePercentage = transferSectionSpecConfig.getEngage();
            Frame lowerRange = new SimpleFrame(rangedSectionConfig.getLowerRange());
            Frame upperRange = new SimpleFrame(rangedSectionConfig.getUpperRange());
            Frame engageFrame = new FrameRange(lowerRange, upperRange, totalFrames).getInBetween(engagePercentage);
            Vector3 modelOffsetPosition = transferSectionSpecConfig.getModelOffsetPosition();
            Vector3 modelOffsetRotation = transferSectionSpecConfig.getModelOffsetRotation();

            List<TransferPosition> transferPositions = new ArrayList<>();
            for(TransferSectionPositionSpecConfig transferSectionPositionSpecConfig : transferSectionSpecConfig.getPositions()){
                String sectionAtStartReference = transferSectionPositionSpecConfig.getSectionAtStart();
                String sectionAtEndReference = transferSectionPositionSpecConfig.getSectionAtEnd();
                Vector3 position = Vector3.add(origin, transferSectionPositionSpecConfig.getPosition());
                Vector3 rotation = transferSectionPositionSpecConfig.getRotation();
                int moveTicks = transferSectionPositionSpecConfig.getMoveTicks();

                Quaternion orientation = Quaternion.fromAnglesVector(rotation);
                transferPositions.add(new TransferPosition(position, orientation, moveTicks, sectionAtStartReference, sectionAtEndReference));
            }

            TransferPosition originTransferPosition = transferPositions.get(0);

            Quaternion modelOffsetOrientation = originTransferPosition.getOrientation().clone();
            modelOffsetOrientation.rotateYawPitchRoll(modelOffsetRotation);

            VirtualArmorstand virtualArmorstand = viewportManager.spawnVirtualArmorstand(
                    Vector3.add(originTransferPosition.getLocation(), modelOffsetPosition),
                    new TrainModelItem(ItemStackFactory.getCoasterStack(Material.DIAMOND_HOE, 2, true)));
            virtualArmorstand.setHeadpose(ArmorStandPose.getArmorStandPose(modelOffsetOrientation));

            Transfer transfer = new Transfer(transferPositions, virtualArmorstand, origin, modelOffsetPosition, modelOffsetRotation);
            coasterHandle.addTransfer(transfer);

            return new TrainDisplacerTransferTrackBehaviour(cartMovementFactory, 1.0, 0.1, 0.1, engageFrame, transfer);
        }

        JRidesPlugin.getLogger().severe("Unknown section type " + type);
        return null;
    }

    public TrackBehaviour getTrackBehaviourFor(CoasterHandle coasterHandle, CoasterConfig coasterConfig, PointSectionConfig pointSectionConfig) {
        String type = pointSectionConfig.getType();
        TrackConfig trackConfig = coasterConfig.getTrack();
        String identifier = pointSectionConfig.getIdentifier();

        if(type.equalsIgnoreCase("switch")){
            return getSwitchBehaviour(pointSectionConfig.getSwitchSectionSpecConfig());
        }

        throw new RuntimeException("Not implemented"); // TODO: do
    }
}
