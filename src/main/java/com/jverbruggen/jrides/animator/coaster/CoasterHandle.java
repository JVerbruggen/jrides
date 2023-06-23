package com.jverbruggen.jrides.animator.coaster;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.AbstractRideHandle;
import com.jverbruggen.jrides.animator.coaster.tool.ParticleTrackVisualisationTool;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.trigger.TriggerContext;
import com.jverbruggen.jrides.effect.EffectTriggerCollection;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.CoasterStationHandle;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.StationHandle;
import com.jverbruggen.jrides.models.ride.coaster.transfer.Transfer;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import org.bukkit.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoasterHandle extends AbstractRideHandle {
    private Track track;
    private ParticleTrackVisualisationTool visualisationTool;
    private List<CoasterStationHandle> stationHandles;
    private List<TrainHandle> trains;
    private List<Transfer> transfers;
    private EffectTriggerCollection effectTriggerCollection;
    private final String dispatchSound;
    private final String restraintOpenSound;
    private final String restraintCloseSound;
    private final String windSound;
    private int rideOverviewMapId;

    public CoasterHandle(Ride ride, World world, String dispatchSound, String restraintOpenSound,
                         String restraintCloseSound, String windSound, int rideOverviewMapId, boolean loaded) {
        super(world, ride, null, loaded);

        this.dispatchSound = dispatchSound;
        this.restraintOpenSound = restraintOpenSound;
        this.restraintCloseSound = restraintCloseSound;
        this.windSound = windSound;
        this.trains = new ArrayList<>();
        this.stationHandles = new ArrayList<>();
        this.transfers = new ArrayList<>();
        this.visualisationTool = null;
        this.track = null;
        this.effectTriggerCollection = null;
        this.rideOverviewMapId = rideOverviewMapId;
    }

    public int getRideOverviewMapId(){
        return this.rideOverviewMapId;
    }

    public void setRideOverviewMapId(int rideOverviewMapId) {
        if(rideOverviewMapId<0) throw new RuntimeException("Cannot set ride overview map id to a negative value");
        this.rideOverviewMapId = rideOverviewMapId;
    }

    public void setTrains(List<TrainHandle> trains) {
        this.trains = trains;
        trains.forEach(t -> t.setCoasterHandle(this));
        this.visualisationTool = ParticleTrackVisualisationTool.fromTrack(getWorld(), track, 20, trains);
    }

    public List<TrainHandle> getTrains() {
        return trains;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public ParticleTrackVisualisationTool getVisualisationTool() {
        return visualisationTool;
    }

    @Override
    public DispatchTrigger getDispatchTrigger() {
        return getStationHandle(null).getTriggerContext().getDispatchTrigger();
    }

    @Override
    public TriggerContext getTriggerContext(@Nonnull String contextOwner) {
        CoasterStationHandle stationHandle = getStationHandle(contextOwner);
        if(stationHandle == null) return null;
        return stationHandle.getTriggerContext();
    }

    @Override
    public TriggerContext getFirstTriggerContext() {
        return getStationHandles().get(0).getTriggerContext();
    }

    @Override
    public PlayerLocation getEjectLocation() {
        return stationHandles.stream()
                .filter(s -> s.getEjectLocation() != null)
                .map(StationHandle::getEjectLocation)
                .findFirst().orElse(null);
    }

    @Override
    public List<StationHandle> getStationHandles() {
        return stationHandles.stream().map(h->(StationHandle)h).collect(Collectors.toList());
    }

    public void addStationHandle(CoasterStationHandle stationHandle) {
        stationHandles.add(stationHandle);
    }

    public List<CoasterStationHandle> getCoasterStationHandles() {
        return stationHandles;
    }

    public CoasterStationHandle getStationHandle(String shortName){
        if(getStationHandles().size() == 0){
            JRidesPlugin.getLogger().severe("Looked for station with short name " + shortName + " but size was 0");
            return null;
        }

        return stationHandles.stream()
                .filter(s -> s.getShortName().equalsIgnoreCase(shortName))
                .findFirst()
                .orElseThrow(() -> {
                    String options = stationHandles.stream().map(StationHandle::getShortName).collect(Collectors.joining(", "));
                    return new RuntimeException("Station short name " + shortName + " did not exist. Existing stations: " + options);
                });
    }

    public CoasterStationHandle getStationHandle(int index){
        if(getStationHandles().size() <= index) return null;
        return getCoasterStationHandles().get(index);
    }

    public void tick(){
        if(!isLoaded()) return;

        RideController rideController = getRideController();
        if(rideController.isActive())
            rideController.getControlMode().tick();

        for(TrainHandle trainHandle : trains){
            trainHandle.tick();
        }
        for(Transfer transfer : transfers){
            transfer.tick();
        }
    }

    public EffectTriggerCollection getEffectTriggerCollection() {
        return effectTriggerCollection;
    }

    public void setEffectTriggerCollection(EffectTriggerCollection effectTriggerCollection) {
        this.effectTriggerCollection = effectTriggerCollection;
    }

    public String getDispatchSound() {
        return dispatchSound;
    }

    public String getRestraintOpenSound() {
        return restraintOpenSound;
    }

    public String getRestraintCloseSound() {
        return restraintCloseSound;
    }

    public String getWindSound() {
        return windSound;
    }

    public void addTransfer(Transfer transfer){
        transfers.add(transfer);
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public Track getTrack(){
        return track;
    }

    @Override
    public List<Player> getPassengers() {
        return getTrains().stream()
                .flatMap(trainHandle -> trainHandle.getTrain().getPassengers().stream())
                .toList();
    }

    @Override
    public boolean isOpen() {
        return isLoaded() && getState().getOpenState().isOpen();
    }

    @Override
    public boolean canFullyClose() {
        return getPassengers().size() == 0 && getRideController().getOperator() == null;
    }

}