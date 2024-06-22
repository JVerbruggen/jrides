package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.basic.StaticStructureComponent;
import com.jverbruggen.jrides.animator.flatride.linearactuator.LinearActuator;
import com.jverbruggen.jrides.animator.flatride.linearactuator.mode.LinearActuatorMode;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.animator.flatride.rotor.Rotor;
import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxis;
import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxisFactory;
import com.jverbruggen.jrides.animator.flatride.rotor.mode.RotorActuatorMode;
import com.jverbruggen.jrides.animator.flatride.seat.FlatRideSeat;
import com.jverbruggen.jrides.animator.flatride.seat.SeatComponent;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LimbConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LinearActuatorConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.RotorPlayerControlConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.joint.RelativeAttachmentJointConfig;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.*;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public interface FlatRideComponent {
    String getIdentifier();
    String getGroupIdentifier();
    boolean isRoot();
    Attachment getAttachedTo();
    void setAttachedTo(Attachment attachment);
    void attach(FlatRideComponent child, Quaternion offsetRotation, Vector3 offsetPosition);
    Quaternion getRotation();
    Vector3 getPosition();
    Matrix4x4 getPositionMatrix();
    void tick();
    boolean equalsToIdentifier(String identifier);
    @Nullable PlayerControl getPlayerControl();

    static List<FlatRideComponent> findAllMatching(List<FlatRideComponent> components, String needle){
        return components.stream()
                .filter(c -> c.equalsToIdentifier(needle))
                .collect(Collectors.toList());
    }

    private static List<FlatRideComponent> createDistributedComponent(String identifier, Quaternion offsetRotation, int amount, BiFunction<Quaternion, String, FlatRideComponent> creationFunction){
        if(amount == 1){
            return List.of(creationFunction.apply(offsetRotation.clone(), identifier));
        }

        List<FlatRideComponent> rotors = new ArrayList<>();
        Quaternion workingQuaternion = offsetRotation.clone();

        for(int i = 0; i < amount; i++){
            rotors.add(creationFunction.apply(workingQuaternion.clone(), identifier + "_" + (i+1)));

            workingQuaternion.rotateY(360f/amount);
        }
        return rotors;
    }

    static List<FlatRideComponent> createDistributedSeats(FlatRideHandle rideHandle, String identifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, int seatYawOffset, List<ModelConfig> flatRideModelsConfig, int amount){
        return createDistributedComponent(
                identifier,
                offsetRotation,
                amount,
                (seatQuaternion, seatIdentifier) -> createSeat(
                        rideHandle, seatIdentifier, identifier, attachedTo, seatQuaternion, offsetPosition, seatYawOffset, flatRideModelsConfig));
    }

    static List<FlatRideComponent> createDistributedAttachedRotors(String identifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, FlatRideComponentSpeed flatRideComponentSpeed, RotorActuatorMode rotorActuatorMode, RotorPlayerControlConfig controlConfig, RelativeAttachmentJointConfig jointConfig, String rotorAxisSpec, List<ModelConfig> flatRideModelsConfig, int amount){
        return createDistributedComponent(
                identifier,
                offsetRotation,
                amount,
                (rotorQuaternion, rotorIdentifier) -> FlatRideComponent.createAttachedRotor(
                        rotorIdentifier, identifier, attachedTo, rotorQuaternion, offsetPosition,
                        flatRideComponentSpeed.clone(), controlConfig, jointConfig, RotorAxisFactory.createAxisFromString(rotorAxisSpec),
                        rotorActuatorMode, flatRideModelsConfig));
    }

    static List<FlatRideComponent> createDistributedLinearActuators(FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, LinearActuatorConfig linearActuatorConfig, int amount) {
        return createDistributedComponent(
                linearActuatorConfig.getIdentifier(),
                offsetRotation,
                amount,
                (linearActuatorQuaternion, linearActuatorIdentifier) -> FlatRideComponent.createLinearActuator(
                        linearActuatorIdentifier, linearActuatorConfig.getIdentifier(), attachedTo,
                        linearActuatorQuaternion, offsetPosition, linearActuatorConfig.getFlatRideComponentSpeed().clone(),
                        linearActuatorConfig.getLinearActuatorTypeConfig().createActuatorMode(),
                        linearActuatorConfig.getFlatRideModels()));
    }

    static List<FlatRideComponent> createLimb(FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, LimbConfig limbConfig, VectorQuaternionState initialPose) {
        return createDistributedComponent(
                limbConfig.getIdentifier(),
                offsetRotation,
                1,
                (limbQuaternion, limbIdentifier) -> FlatRideComponent.createLimb(
                        limbIdentifier, limbConfig.getIdentifier(), attachedTo,
                        limbQuaternion, offsetPosition, limbConfig.getFlatRideModels(), initialPose));
    }

    static Rotor createAttachedRotor(String identifier, String groupIdentifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, FlatRideComponentSpeed flatRideComponentSpeed, RotorPlayerControlConfig controlConfig, RelativeAttachmentJointConfig jointConfig, RotorAxis rotorAxis, RotorActuatorMode actuatorMode, List<ModelConfig> flatRideModelsConfig){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        List<FlatRideModel> flatRideModels = flatRideModelsConfig.stream()
                .map(config -> config.toFlatRideModel(spawnPosition, viewportManager))
                .collect(Collectors.toList());

        Rotor rotor = new Rotor(identifier, groupIdentifier, false, jointConfig, flatRideModels, flatRideComponentSpeed, rotorAxis, actuatorMode);
        rotor.createPlayerControl(controlConfig);
        attachedTo.attach(rotor, offsetRotation, offsetPosition);

        return rotor;
    }

    static LinearActuator createLinearActuator(String identifier, String groupIdentifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, FlatRideComponentSpeed flatRideComponentSpeed, LinearActuatorMode actuatorMode, List<ModelConfig> flatRideModelsConfig){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        List<FlatRideModel> flatRideModels = flatRideModelsConfig.stream()
                .map(config -> config.toFlatRideModel(spawnPosition, viewportManager))
                .collect(Collectors.toList());

        LinearActuator linearActuator = new LinearActuator(identifier, groupIdentifier, false, null, flatRideModels, flatRideComponentSpeed, actuatorMode);
        attachedTo.attach(linearActuator, offsetRotation, offsetPosition);

        return linearActuator;
    }

    static Limb createLimb(String identifier, String groupIdentifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, List<ModelConfig> flatRideModelsConfig, VectorQuaternionState initialPose){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        List<FlatRideModel> flatRideModels = flatRideModelsConfig.stream()
                .map(config -> config.toFlatRideModel(spawnPosition, viewportManager))
                .collect(Collectors.toList());

        Limb limb = new Limb(identifier, groupIdentifier, false, null, flatRideModels, initialPose);
        attachedTo.attach(limb, offsetRotation, offsetPosition);

        return limb;
    }


    static SeatComponent createSeat(FlatRideHandle flatRideHandle, String identifier, String groupIdentifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, int seatYawOffset, List<ModelConfig> flatRideModelsConfig){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        VirtualEntity seatEntity = viewportManager.spawnSeatEntity(spawnPosition, 0, null);
        FlatRideSeat seat = new FlatRideSeat(flatRideHandle, null, seatEntity, Vector3PlusYaw.zero());

        List<FlatRideModel> flatRideModels = flatRideModelsConfig.stream()
                .map(config -> {
                    FlatRideModel flatRideModel = config.toFlatRideModel(spawnPosition, viewportManager);
                    flatRideModel.getEntity().setHostSeat(seat);
                    return flatRideModel;
                })
                .collect(Collectors.toList());


        Quaternion seatRotation = Quaternion.fromYawPitchRoll(0, seatYawOffset, 0);
        SeatComponent component = new SeatComponent(identifier, groupIdentifier, false, flatRideModels, seat, seatRotation, flatRideHandle.getVehicle());
        seat.setParentSeatHost(component);
        flatRideHandle.addSeatComponent(component);

        attachedTo.attach(component, offsetRotation, offsetPosition);

        PlayerControl playerControl = attachedTo.getPlayerControl();
        if(playerControl != null){
            seat.setPlayerControl(playerControl);
        }

        return component;
    }

    static StaticStructureComponent createStaticStructure(String identifier, String groupIdentifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, RelativeAttachmentJointConfig joint, List<ModelConfig> flatRideModelsConfig){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        List<FlatRideModel> flatRideModels = flatRideModelsConfig.stream()
                .map(config -> config.toFlatRideModel(spawnPosition, viewportManager))
                .collect(Collectors.toList());

        StaticStructureComponent component = new StaticStructureComponent(identifier, groupIdentifier, false, joint, flatRideModels);
        attachedTo.attach(component, offsetRotation, offsetPosition);

        return component;
    }

}
