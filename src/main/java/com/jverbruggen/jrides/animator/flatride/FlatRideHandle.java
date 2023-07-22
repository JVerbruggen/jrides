package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.AbstractRideHandle;
import com.jverbruggen.jrides.animator.flatride.seat.SeatComponent;
import com.jverbruggen.jrides.animator.flatride.station.FlatRideStationHandle;
import com.jverbruggen.jrides.animator.flatride.timing.TimingSequence;
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.config.coaster.objects.SoundsConfig;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.event.player.PlayerFinishedRideEvent;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.train.Vehicle;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecord;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

public class FlatRideHandle extends AbstractRideHandle {
    private TimingSequence timingSequence;
    private final FlatRideStationHandle stationHandle;
    private final DispatchTrigger dispatchTrigger;
    private final List<SeatComponent> seatComponents;

    private boolean finished;

    public FlatRideHandle(World world, Ride ride, boolean loaded, FlatRideStationHandle stationHandle, SoundsConfig sounds) {
        super(world, ride, null, loaded, sounds);
        this.stationHandle = stationHandle;
        this.timingSequence = null;
        this.dispatchTrigger = stationHandle.getTriggerContext().getDispatchTrigger();
        this.finished = false;
        this.seatComponents = new ArrayList<>();

        stationHandle.setFlatRideHandle(this);
        stationHandle.getTriggerContext().getRestraintTrigger().getLock().addEventListener(c -> this.onRestraintLockUpdateEventListener(c.isUnlocked()));
    }

    @Override
    public void tick() {
        if(!isLoaded()) return;

        RideController rideController = getRideController();
        if(rideController.isActive())
            rideController.getControlMode().tick();

        vehicleTick();
    }

    private void vehicleTick(){
        boolean dispatchActive = dispatchTrigger.isActive();
        if(finished && !dispatchActive) return;

        checkForDispatch(dispatchActive);

        finished = this.timingSequence.tick();
        this.stationHandle.getVehicle().tick();

        if(finished)
            onRideFinish();
    }

    private void checkForDispatch(boolean dispatchActive){
        if(dispatchActive && this.timingSequence.isIdle()){
            onRideStart();

            Vehicle vehicle = getVehicle();
            getRideController().onVehicleDepart(vehicle, stationHandle);
            vehicle.playDispatchSound();
        }
    }

    private void onRideStart(){
        dispatchTrigger.reset();
        this.timingSequence.restart();
        finished = false;
        stationHandle.getTriggerContext().getVehiclePresentLock().setLocked(true);
    }

    private void onRideFinish(){
        PlayerFinishedRideEvent.sendFinishedRideEvent(getPassengers()
                .stream()
                .map(p -> (JRidesPlayer)p)
                .collect(Collectors.toList()), getRide());

        stationHandle.getTriggerContext().getVehiclePresentLock().setLocked(false);
        stationHandle.getTriggerContext().getRestraintTrigger().getLock().setLocked(true);
        stationHandle.getVehicle().ejectPassengers();
    }

    private void onRestraintLockUpdateEventListener(boolean unlocked){
        seatComponents.forEach(c -> c.setRestraint(unlocked));
    }

    public void setTimingSequence(TimingSequence timingSequence) {
        this.timingSequence = timingSequence;
    }

    public void addRootComponent(FlatRideComponent component){
        stationHandle.getVehicle().addRootComponent(component);
    }

    public List<FlatRideComponent> getRootComponents() {
        return stationHandle.getVehicle().getRootComponents();
    }

    @Override
    public DispatchTrigger getDispatchTrigger() {
        return getFirstTriggerContext().getDispatchTrigger();
    }

    @Override
    public TriggerContext getTriggerContext(@Nonnull String contextOwner) {
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
    public List<RideCounterRecord> getTopRideCounters() {
        return null;
    }

    @Override
    public List<Player> getPassengers() {
        return stationHandle.getVehicle().getPassengers();
    }

    public Vehicle getVehicle(){
        return stationHandle.getVehicle();
    }

    public void addSeatComponent(SeatComponent seatComponent){
        this.seatComponents.add(seatComponent);
    }
}