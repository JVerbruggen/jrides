package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.AbstractRideHandle;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FlatRideHandle extends AbstractRideHandle {
    private final List<FlatRideComponent> rootComponents;

    public FlatRideHandle(World world, Ride ride, boolean loaded) {
        super(world, ride, null, loaded);

        this.rootComponents = new ArrayList<>();
    }

    @Override
    public void tick() {
        this.rootComponents.forEach(FlatRideComponent::tick);
    }

    public void addRootComponent(FlatRideComponent component){
        rootComponents.add(component);
    }

    public List<FlatRideComponent> getRootComponents() {
        return rootComponents;
    }

    @Override
    public DispatchTrigger getDispatchTrigger() {
        return null;
    }

    @Override
    public TriggerContext getTriggerContext(@NotNull String contextOwner) {
        return null;
    }

    @Override
    public TriggerContext getFirstTriggerContext() {
        return null;
    }

    @Override
    public PlayerLocation getEjectLocation() {
        return null;
    }

    @Override
    public List<StationHandle> getStationHandles() {
        return null;
    }

    @Override
    public List<RideCounterRecordCollection> getTopRideCounters() {
        return null;
    }

    @Override
    public List<Player> getPassengers() {
        return null;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void open(Player authority) {

    }

    @Override
    public void close(Player authority) {

    }

    @Override
    public boolean canFullyClose() {
        return true;
    }
}