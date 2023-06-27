package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.AbstractRideHandle;
import com.jverbruggen.jrides.animator.flatride.station.FlatRideStationHandle;
import com.jverbruggen.jrides.animator.flatride.timing.TimingSequence;
import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FlatRideHandle extends AbstractRideHandle {
    private final List<FlatRideComponent> rootComponents;
    private TimingSequence timingSequence;
    private final FlatRideStationHandle stationHandle;
    private final DispatchTrigger dispatchTrigger;

    public FlatRideHandle(World world, Ride ride, boolean loaded, FlatRideStationHandle stationHandle, SoundsConfig sounds) {
        super(world, ride, null, loaded, sounds);
        this.stationHandle = stationHandle;
        this.timingSequence = null;
        this.rootComponents = new ArrayList<>();
        this.dispatchTrigger = stationHandle.getTriggerContext().getDispatchTrigger();

        stationHandle.setFlatRideHandle(this);
    }

    @Override
    public void tick() {
        if(dispatchTrigger.isActive()
                && this.timingSequence.isIdle()){
            dispatchTrigger.reset();
            this.timingSequence.restart();
        }


        boolean finished = this.timingSequence.tick();
        this.rootComponents.forEach(FlatRideComponent::tick);

        if(finished)
            stationHandle.getStationaryVehicle().ejectPassengers();
    }

    public void setTimingSequence(TimingSequence timingSequence) {
        this.timingSequence = timingSequence;
    }

    public void addRootComponent(FlatRideComponent component){
        rootComponents.add(component);
    }

    public List<FlatRideComponent> getRootComponents() {
        return rootComponents;
    }

    @Override
    public DispatchTrigger getDispatchTrigger() {
        return getFirstTriggerContext().getDispatchTrigger();
    }

    @Override
    public TriggerContext getTriggerContext(@NotNull String contextOwner) {
        return getFirstTriggerContext();
    }

    @Override
    public TriggerContext getFirstTriggerContext() {
        return stationHandle.getTriggerContext();
    }

    @Override
    public PlayerLocation getEjectLocation() {
        return stationHandle.getEjectLocation();
    }

    @Override
    public List<StationHandle> getStationHandles() {
        return List.of(stationHandle);
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

    public Vehicle getVehicle(){
        return stationHandle.getVehicle();
    }
}