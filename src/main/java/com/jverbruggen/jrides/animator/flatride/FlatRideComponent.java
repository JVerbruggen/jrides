package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.animator.flatride.rotor.Rotor;
import com.jverbruggen.jrides.animator.flatride.rotor.RotorSpeed;
import com.jverbruggen.jrides.animator.flatride.seat.FlatRideSeat;
import com.jverbruggen.jrides.animator.flatride.seat.SeatComponent;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.MatrixMath;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.seat.Seat;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
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
    void tick();
    boolean equalsToIdentifier(String identifier);

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
        List<FlatRideComponent> rotors = new ArrayList<>();
        Quaternion workingQuaternion = offsetRotation.clone();

        for(int i = 0; i < amount; i++){
            rotors.add(creationFunction.apply(workingQuaternion.clone(), identifier + "_" + (i+1)));

            workingQuaternion.rotateY(360f/amount);
        }
        return rotors;
    }

    static List<FlatRideComponent> createDistributedSeats(RideHandle rideHandle, String identifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, int seatYawOffset, int amount){
        return createDistributedComponent(
                identifier,
                offsetRotation,
                amount,
                (seatQuaternion, seatIdentifier) -> createSeat(
                        rideHandle, seatIdentifier, identifier, attachedTo, seatQuaternion, offsetPosition, seatYawOffset));
    }

    static List<FlatRideComponent> createDistributedAttachedRotors(String identifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, RotorSpeed rotorSpeed, List<ModelConfig> flatRideModelsConfig, int amount){
        return createDistributedComponent(
                identifier,
                offsetRotation,
                amount,
                (rotorQuaternion, rotorIdentifier) -> FlatRideComponent.createAttachedRotor(
                        rotorIdentifier, identifier, attachedTo, rotorQuaternion, offsetPosition, rotorSpeed.clone(), flatRideModelsConfig));
    }

    static Rotor createAttachedRotor(String identifier, String groupIdentifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, RotorSpeed rotorSpeed, List<ModelConfig> flatRideModelsConfig){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        List<FlatRideModel> flatRideModels = flatRideModelsConfig.stream()
                .map(config -> config.toFlatRideModel(spawnPosition, viewportManager))
                .collect(Collectors.toList());

        Rotor rotor = new Rotor(identifier, groupIdentifier, false, flatRideModels, rotorSpeed);
        attachedTo.attach(rotor, offsetRotation, offsetPosition);

        return rotor;
    }

    static SeatComponent createSeat(RideHandle rideHandle, String identifier, String groupIdentifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, int seatYawOffset){
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        VirtualArmorstand virtualArmorstand = viewportManager.spawnVirtualArmorstand(spawnPosition, new TrainModelItem(new ItemStack(Material.OAK_FENCE)));
        Seat seat = new FlatRideSeat(rideHandle, null, virtualArmorstand, Vector3.zero());

        List<FlatRideModel> flatRideModels = Collections.emptyList();
        Quaternion seatRotation = Quaternion.fromYawPitchRoll(0, seatYawOffset, 0);
        SeatComponent component = new SeatComponent(identifier, groupIdentifier, false, flatRideModels, seat, seatRotation);
        seat.setParentSeatHost(component);

        attachedTo.attach(component, offsetRotation, offsetPosition);

        return component;
    }

}
