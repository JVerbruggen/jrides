package com.jverbruggen.jrides.animator.flatride.factory;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.rotor.FixedAttachment;
import com.jverbruggen.jrides.animator.flatride.rotor.Rotor;
import com.jverbruggen.jrides.animator.flatride.rotor.RotorModel;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.MatrixMath;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.flatride.FlatRide;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FlatRideFactory {
    private final ViewportManager viewportManager;

    public FlatRideFactory() {
        viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
    }

    public RideHandle createSimpleFlatRide(String rideIdentifier, World world, RideState rideState){
        Vector3 center = new Vector3(-1026.5, 110, -771.5);
        Rotor rootRotor = createMainRotor("center", center, new Quaternion());

        List<Rotor> turntableRotors = createDistributedAttachedRotors(
                "turntable",
                rootRotor,
                Quaternion.fromYawPitchRoll(0, 90, 0),
                new Vector3(7, 0, 0),
                4);

        for(Rotor turntableRotor : turntableRotors){
            createDistributedAttachedRotors(
                    "cup",
                    turntableRotor,
                    Quaternion.fromYawPitchRoll(0, 90, 0),
                    new Vector3(2, 0, 0),
                    3);
        }


        FlatRide flatRide = new FlatRide(rideIdentifier,
                "DisplayName", List.of(""), new ItemStack(Material.GOLD_BLOCK),
                PlayerLocation.fromVector3(center), false);

        boolean loaded = true;

        FlatRideHandle flatRideHandle = new FlatRideHandle(world, flatRide, loaded, rootRotor);
        flatRideHandle.setState(rideState);

        return flatRideHandle;
    }

    private Rotor createAttachedRotor(String identifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition){
        Vector3 spawnPosition = MatrixMath.rotateTranslate(
                attachedTo.getPosition(),
                attachedTo.getRotation(),
                offsetPosition,
                offsetRotation).toVector3();

        VirtualEntity virtualEntity = viewportManager.spawnVirtualArmorstand(spawnPosition, new TrainModelItem(new ItemStack(Material.GOLD_BLOCK)));

        List<RotorModel> rotorModels = List.of(new RotorModel(virtualEntity, Vector3.zero()));
        Rotor rotor = new Rotor(identifier, rotorModels);
        attachedTo.attach(rotor, offsetRotation, offsetPosition);

        return rotor;
    }

    private List<Rotor> createDistributedAttachedRotors(String identifier, FlatRideComponent attachedTo, Quaternion offsetRotation, Vector3 offsetPosition, int amount){
        List<Rotor> rotors = new ArrayList<>();
        Quaternion workingQuaternion = offsetRotation.clone();

        for(int i = 0; i < amount; i++){
            String rotorIdentifier = identifier + "_" + (i+1);
            Rotor rotor = createAttachedRotor(rotorIdentifier, attachedTo, workingQuaternion.clone(), offsetPosition);
            rotors.add(rotor);

            workingQuaternion.rotateY(360f/amount);
        }
        return rotors;
    }

    private Rotor createMainRotor(String identifier, Vector3 position, Quaternion rotation){
        VirtualEntity virtualEntity = viewportManager.spawnVirtualArmorstand(position, new TrainModelItem(new ItemStack(Material.DIAMOND_BLOCK)));

        Rotor rotor = new Rotor(identifier, List.of(new RotorModel(virtualEntity, Vector3.zero())));
        rotor.setAttachedTo(new FixedAttachment(rotor, position, rotation));
        return rotor;
    }
}
