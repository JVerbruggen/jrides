package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.basic.StaticStructureComponent;
import com.jverbruggen.jrides.animator.flatride.linearactuator.LinearActuator;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.animator.flatride.rotor.Rotor;
import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxis;
import com.jverbruggen.jrides.animator.flatride.seat.FlatRideSeat;
import com.jverbruggen.jrides.animator.flatride.seat.SeatComponent;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.LinearActuatorConfig;
import com.jverbruggen.jrides.config.flatride.structure.actuator.RotorPlayerControlConfig;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.MatrixMath;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
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
    void tick();
    boolean equalsToIdentifier(String identifier);
    @Nullable PlayerControl getPlayerControl();

    static FlatRideComponent findInHaystack(List<FlatRideComponent> components, String needle){
        return components.stream()
                .filter(c -> c.equalsToIdentifier(needle))
                .findFirst().orElseThrow(() -> new RuntimeException("Could not find parent '" + needle + "' for rotor"));
    }

    static List<FlatRideComponent> findAllMatching(List<FlatRideComponent> components, String needle){
        return components.stream()
                .filter(c -> c.equalsToIdentifier(needle))
                .collect(Collectors.toList());
    }

    private static List<FlatRideComponent> createDistributedComponent(String identifier, Quaternion offsetRotation, int amount, BiFunction<Quaternion, String, FlatRideComponent> creationFunction){
        if(amount == 1){
            return List.of(creationFunction.apply(offsetRotation.clone(), identifier + "_1"));
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

    static List<FlatRideComponent> createDistributedAttachedRotors(String identifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, FlatRideComponentSpeed flatRideComponentSpeed, RotorPlayerControlConfig controlConfig, RotorAxis rotorAxis, List<ModelConfig> flatRideModelsConfig, int amount){
        return createDistributedComponent(
                identifier,
                offsetRotation,
                amount,
                (rotorQuaternion, rotorIdentifier) -> FlatRideComponent.createAttachedRotor(
                        rotorIdentifier, identifier, attachedTo, rotorQuaternion, offsetPosition, flatRideComponentSpeed.clone(), controlConfig, rotorAxis, flatRideModelsConfig));
    }

    static List<FlatRideComponent> createDistributedLinearActuators(FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, LinearActuatorConfig linearActuatorConfig, int amount) {
        return createDistributedComponent(
                linearActuatorConfig.getIdentifier(),
                offsetRotation,
                amount,
                (linearActuatorQuaternion, linearActuatorIdentifier) -> FlatRideComponent.createLinearActuator(
                        linearActuatorIdentifier, linearActuatorConfig.getIdentifier(), attachedTo,
                        linearActuatorQuaternion, offsetPosition, linearActuatorConfig.getFlatRideComponentSpeed().clone(),
                        linearActuatorConfig.getSize(), linearActuatorConfig.getPhase(), linearActuatorConfig.getFlatRideModels()));
    }

    static Rotor createAttachedRotor(String identifier, String groupIdentifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, FlatRideComponentSpeed flatRideComponentSpeed, RotorPlayerControlConfig controlConfig, RotorAxis rotorAxis, List<ModelConfig> flatRideModelsConfig){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        List<FlatRideModel> flatRideModels = flatRideModelsConfig.stream()
                .map(config -> config.toFlatRideModel(spawnPosition, viewportManager))
                .collect(Collectors.toList());

        Rotor rotor = new Rotor(identifier, groupIdentifier, false, flatRideModels, flatRideComponentSpeed, rotorAxis);
        rotor.createPlayerControl(controlConfig);
        attachedTo.attach(rotor, offsetRotation, offsetPosition);

        return rotor;
    }

    static LinearActuator createLinearActuator(String identifier, String groupIdentifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, FlatRideComponentSpeed flatRideComponentSpeed, float size, Supplier<Integer> phase, List<ModelConfig> flatRideModelsConfig){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        List<FlatRideModel> flatRideModels = flatRideModelsConfig.stream()
                .map(config -> config.toFlatRideModel(spawnPosition, viewportManager))
                .collect(Collectors.toList());

        LinearActuator linearActuator = new LinearActuator(identifier, groupIdentifier, false, flatRideModels, flatRideComponentSpeed, size, phase.get().shortValue());
        attachedTo.attach(linearActuator, offsetRotation, offsetPosition);

        return linearActuator;
    }


    static SeatComponent createSeat(FlatRideHandle flatRideHandle, String identifier, String groupIdentifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, int seatYawOffset, List<ModelConfig> flatRideModelsConfig){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        List<FlatRideModel> flatRideModels = flatRideModelsConfig.stream()
                .map(config -> config.toFlatRideModel(spawnPosition, viewportManager))
                .collect(Collectors.toList());

        VirtualEntity seatEntity = viewportManager.spawnSeatEntity(spawnPosition, 0, null);

        FlatRideSeat seat = new FlatRideSeat(flatRideHandle, null, seatEntity, Vector3.zero());

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

    static StaticStructureComponent createStaticStructure(String identifier, String groupIdentifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, List<ModelConfig> flatRideModelsConfig){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        List<FlatRideModel> flatRideModels = flatRideModelsConfig.stream()
                .map(config -> config.toFlatRideModel(spawnPosition, viewportManager))
                .collect(Collectors.toList());

        StaticStructureComponent component = new StaticStructureComponent(identifier, groupIdentifier, false, flatRideModels);
        attachedTo.attach(component, offsetRotation, offsetPosition);

        return component;
    }

}
