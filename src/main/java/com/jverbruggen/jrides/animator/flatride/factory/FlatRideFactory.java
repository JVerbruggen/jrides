package com.jverbruggen.jrides.animator.flatride.factory;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.seat.FlatRideSeat;
import com.jverbruggen.jrides.animator.flatride.seat.SeatComponent;
import com.jverbruggen.jrides.animator.flatride.attachment.FixedAttachment;
import com.jverbruggen.jrides.animator.flatride.rotor.Rotor;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.animator.flatride.rotor.RotorSpeed;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.MatrixMath;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.seat.Seat;
import com.jverbruggen.jrides.models.ride.flatride.FlatRide;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class FlatRideFactory {
    private final ViewportManager viewportManager;

    public FlatRideFactory() {
        viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
    }

    public RideHandle createSimpleFlatRide(String rideIdentifier, World world, RideState rideState){
        Vector3 center = new Vector3(-1026.5, 110, -771.5);
        FlatRide flatRide = new FlatRide(rideIdentifier,
                "DisplayName", List.of(""), new ItemStack(Material.GOLD_BLOCK),
                PlayerLocation.fromVector3(center), false);

        Rotor rootRotor = createMainRotor(
                "center",
                center,
                new Quaternion(),
                new RotorSpeed(2.5f));

        FlatRideHandle flatRideHandle = new FlatRideHandle(world, flatRide, true, rootRotor);

        List<FlatRideComponent> turntableRotors = createDistributedAttachedRotors(
                "turntable",
                rootRotor,
                Quaternion.fromYawPitchRoll(0, 90, 0),
                new Vector3(7, 0, 0),
                new RotorSpeed(2.5f),
                4);

        for(FlatRideComponent turntableRotor : turntableRotors){
            List<FlatRideComponent> cupRotors = createDistributedAttachedRotors(
                    "cup",
                    turntableRotor,
                    Quaternion.fromYawPitchRoll(0, 90, 0),
                    new Vector3(3, 0, 0),
                    new RotorSpeed(8),
                    3);

            int seatYawOffset = 180;
            for(FlatRideComponent cupRotor : cupRotors){
                createDistributedSeats(flatRideHandle,
                        cupRotor.getIdentifier(),
                        cupRotor,
                        Quaternion.fromYawPitchRoll(0, 0, 0),
                        new Vector3(1, 0, 0),
                        seatYawOffset,
                        3);
            }
        }

        flatRideHandle.setState(rideState);

        return flatRideHandle;
    }

    private Rotor createAttachedRotor(String identifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, RotorSpeed rotorSpeed){
        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        VirtualEntity virtualEntity = viewportManager.spawnVirtualArmorstand(spawnPosition, new TrainModelItem(new ItemStack(Material.GOLD_BLOCK)));

        List<FlatRideModel> flatRideModels = List.of(new FlatRideModel(virtualEntity, Vector3.zero()));
        Rotor rotor = new Rotor(identifier, flatRideModels, rotorSpeed);
        attachedTo.attach(rotor, offsetRotation, offsetPosition);

        return rotor;
    }

    private SeatComponent createSeat(RideHandle rideHandle, String identifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, int seatYawOffset){
        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        VirtualArmorstand virtualArmorstand = viewportManager.spawnVirtualArmorstand(spawnPosition, new TrainModelItem(new ItemStack(Material.OAK_FENCE)));
        Seat seat = new FlatRideSeat(rideHandle, null, virtualArmorstand, Vector3.zero());

        List<FlatRideModel> flatRideModels = Collections.emptyList();
        Quaternion seatRotation = Quaternion.fromYawPitchRoll(0, seatYawOffset, 0);
        SeatComponent component = new SeatComponent(identifier, flatRideModels, seat, seatRotation);
        seat.setParentSeatHost(component);

        attachedTo.attach(component, offsetRotation, offsetPosition);

        return component;
    }

    private List<FlatRideComponent> createDistributedAttachedRotors(String identifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, RotorSpeed rotorSpeed, int amount){
        return createDistributedComponent(
                identifier,
                offsetRotation,
                amount,
                (rotorQuaternion, rotorIdentifier) -> createAttachedRotor(
                        rotorIdentifier, attachedTo, rotorQuaternion, offsetPosition, rotorSpeed.clone()));
    }

    private List<FlatRideComponent> createDistributedSeats(RideHandle rideHandle, String identifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, int seatYawOffset, int amount){
        return createDistributedComponent(
                identifier,
                offsetRotation,
                amount,
                (seatQuaternion, seatIdentifier) -> createSeat(
                        rideHandle, seatIdentifier, attachedTo, seatQuaternion, offsetPosition, seatYawOffset));
    }

    private List<FlatRideComponent> createDistributedComponent(String identifier, Quaternion offsetRotation, int amount, BiFunction<Quaternion, String, FlatRideComponent> creationFunction){
        List<FlatRideComponent> rotors = new ArrayList<>();
        Quaternion workingQuaternion = offsetRotation.clone();

        for(int i = 0; i < amount; i++){
            rotors.add(creationFunction.apply(workingQuaternion.clone(), identifier + "_" + (i+1)));

            workingQuaternion.rotateY(360f/amount);
        }
        return rotors;
    }

    private Rotor createMainRotor(String identifier, Vector3 position, Quaternion rotation, RotorSpeed rotorSpeed){
        VirtualEntity virtualEntity = viewportManager.spawnVirtualArmorstand(position, new TrainModelItem(new ItemStack(Material.DIAMOND_BLOCK)));

        Rotor rotor = new Rotor(identifier, List.of(new FlatRideModel(virtualEntity, Vector3.zero())), rotorSpeed);
        rotor.setAttachedTo(new FixedAttachment(rotor, position, rotation));
        return rotor;
    }
}
