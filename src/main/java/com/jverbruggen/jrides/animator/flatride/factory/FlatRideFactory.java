package com.jverbruggen.jrides.animator.flatride.factory;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.attachment.FixedAttachment;
import com.jverbruggen.jrides.animator.flatride.rotor.Rotor;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemConfig;
import com.jverbruggen.jrides.config.flatride.FlatRideConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfig;
import com.jverbruggen.jrides.config.flatride.structure.StructureConfigItem;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
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
        FlatRide flatRide = new FlatRide(rideIdentifier,
                "DisplayName", List.of(""), new ItemStack(Material.GOLD_BLOCK),
                PlayerLocation.fromVector3(center), false);

        FlatRideHandle flatRideHandle = new FlatRideHandle(world, flatRide, true);

        Rotor rootRotor = createMainRotor(
                "center",
                center,
                new Quaternion(),
                new FlatRideComponentSpeed(2.5f));
        flatRideHandle.addRootComponent(rootRotor);

        List<FlatRideComponent> turntableRotors = FlatRideComponent.createDistributedAttachedRotors(
                "turntable",
                rootRotor,
                Quaternion.fromYawPitchRoll(0, 90, 0),
                new Vector3(7, 0, 0),
                new FlatRideComponentSpeed(2.5f),
                List.of(new ModelConfig(new ItemConfig(Material.GOLD_BLOCK), Vector3.zero(), new Quaternion())),
                4);

        for(FlatRideComponent turntableRotor : turntableRotors){
            List<FlatRideComponent> cupRotors = FlatRideComponent.createDistributedAttachedRotors(
                    "cup",
                    turntableRotor,
                    Quaternion.fromYawPitchRoll(0, 90, 0),
                    new Vector3(3, 0, 0),
                    new FlatRideComponentSpeed(8),
                    List.of(new ModelConfig(new ItemConfig(Material.REDSTONE_BLOCK), Vector3.zero(), new Quaternion())),
                    3);

            int seatYawOffset = 180;
            for(FlatRideComponent cupRotor : cupRotors){
                FlatRideComponent.createDistributedSeats(flatRideHandle,
                        cupRotor.getIdentifier(),
                        cupRotor,
                        Quaternion.fromYawPitchRoll(0, 0, 0),
                        new Vector3(1, 0, 0),
                        seatYawOffset,
                        List.of(),
                        3);
            }
        }

        flatRideHandle.setState(rideState);

        return flatRideHandle;
    }

    public RideHandle createFromConfig(String rideIdentifier, World world, RideState rideState, FlatRideConfig flatRideConfig){
        String displayName = flatRideConfig.getDisplayName();
        List<String> displayDescription = flatRideConfig.getDisplayDescription();
        ItemStack displayItem = flatRideConfig.getDisplayItem().createItemStack();
        PlayerLocation warpLocation = flatRideConfig.getWarpLocation();
        boolean canExitDuringRide = flatRideConfig.canExitDuringRide();

        FlatRide flatRide = new FlatRide(rideIdentifier, displayName, displayDescription, displayItem,
                warpLocation, canExitDuringRide);
        FlatRideHandle flatRideHandle = new FlatRideHandle(world, flatRide, true);

        List<FlatRideComponent> components = new ArrayList<>();

        StructureConfig structureConfig = flatRideConfig.getStructureConfig();
        for(StructureConfigItem item : structureConfig.getItems()){
            item.createAndAddTo(components, flatRideHandle);
        }

        components.stream()
                .filter(FlatRideComponent::isRoot)
                .forEach(flatRideHandle::addRootComponent);

        flatRideHandle.setState(rideState);
        return flatRideHandle;
    }

    private Rotor createMainRotor(String identifier, Vector3 position, Quaternion rotation, FlatRideComponentSpeed flatRideComponentSpeed){
        VirtualEntity virtualEntity = viewportManager.spawnVirtualArmorstand(position, new TrainModelItem(new ItemStack(Material.DIAMOND_BLOCK)));

        Rotor rotor = new Rotor(identifier, identifier, true, List.of(new FlatRideModel(virtualEntity, Vector3.zero(), new Quaternion())), flatRideComponentSpeed);
        rotor.setAttachedTo(new FixedAttachment(rotor, position, rotation));
        return rotor;
    }
}
