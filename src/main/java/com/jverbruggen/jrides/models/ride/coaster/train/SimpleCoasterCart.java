package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.event.player.PlayerSitDownEvent;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.seat.Seat;
import com.jverbruggen.jrides.models.ride.factory.SeatFactory;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleCoasterCart implements CoasterCart {
    private final String name;
    private final List<CoasterSeat> seats;
    private final VirtualEntity modelEntity;
    private final Vector3 trackOffset;
    private final Quaternion trackRotationOffset;
    private final Frame frame;
    private final int wheelDistance;
    private Train parentTrain;

    private boolean hasEffects;
    private TrainEffectTriggerHandle nextEffect;

    private Quaternion currentOrientation;
    private Vector3 orientationOffset;

    public SimpleCoasterCart(String name, List<CoasterSeat> seats, VirtualEntity modelEntity, Vector3 trackOffset, Quaternion trackRotationOffset, Frame frame, int wheelDistance) {
        this.name = name;
        this.seats = seats;
        this.modelEntity = modelEntity;
        this.trackOffset = trackOffset;
        this.trackRotationOffset = trackRotationOffset;
        this.frame = frame;
        this.wheelDistance = wheelDistance;
        this.parentTrain = null;
        this.currentOrientation = new Quaternion(0,0,0,0);
        this.orientationOffset = null;

        this.nextEffect = null;
        this.hasEffects = false;

        seats.forEach(s -> s.setParentSeatHost(this));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Seat> getSeats() {
        return new ArrayList<>(seats);
    }

    public List<CoasterSeat> getCoasterSeats(){
        return seats;
    }

    @Override
    public List<Player> getPassengers() {
        return seats.stream()
                .filter(Seat::hasPassenger)
                .map(Seat::getPassenger)
                .collect(Collectors.toList());
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public int getWheelDistance() {
        return wheelDistance;
    }

    @Override
    public Vector3 getTrackOffset() {
        return trackOffset;
    }

    @Override
    public Vector3 getPosition() {
        return modelEntity.getLocation();
    }

    /**
     * Get total orientation that is used in display
     * @return
     */
    @Override
    public Quaternion getOrientation() {
        return currentOrientation;
    }

    @Override
    public Quaternion getRotationOffset() {
        return trackRotationOffset;
    }

    @Override
    public void updateCustomOrientationOffset(Vector3 orientationOffset){
        this.orientationOffset = orientationOffset;
        updateOrientation(calculateOrientationWithOffset(currentOrientation));
    }

    private Quaternion calculateOrientationWithOffset(Quaternion original){
        Quaternion orientationWithOffset = original.clone();
        if(this.orientationOffset != null)
            orientationWithOffset.rotateYawPitchRoll(this.orientationOffset);

        return orientationWithOffset;
    }

    private void updateOrientation(Quaternion orientation){
        currentOrientation = orientation;
        modelEntity.setRotation(currentOrientation);
    }

    @Override
    public void setPosition(Vector3 position, Quaternion orientation) {
        updateOrientation(calculateOrientationWithOffset(orientation));
        setPosition(position);
    }

    @Override
    public void setPosition(Vector3 position) {
        modelEntity.setLocation(position);
        SeatFactory.moveCoasterSeats(seats, position, currentOrientation);
    }

    @Override
    public void setPosition(CartMovement cartMovement) {
        setPosition(cartMovement.getLocation(), cartMovement.getOrientation());
    }

    @Override
    public void setRestraint(boolean locked) {
        for(Seat seat : getCoasterSeats()){
            seat.setRestraint(locked);
        }
    }

    @Override
    public boolean getRestraintState() {
        return getCoasterSeats().stream().allMatch(Seat::restraintsActive);
    }

    @Override
    public PlayerLocation getEjectLocation() {
        return getParentTrain().getHandle().getCoasterHandle().getEjectLocation();
    }

    @Override
    public RideHandle getRideHandle() {
        return getParentTrain().getHandle().getCoasterHandle();
    }

    @Override
    public void onPlayerEnter(Player player) {
        getParentTrain().onPlayerEnter(player);
        PlayerSitDownEvent.send(player, getParentTrain().getHandle().getCoasterHandle().getRide());

        // Potentially rider wants to inspect frames
        org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();
        ItemStack itemInHand = bukkitPlayer.getInventory().getItemInMainHand();
        if(bukkitPlayer.hasPermission(Permissions.ELEVATED_STATUS_INSPECTION)
                && itemInHand.getItemMeta() != null
                && itemInHand.getItemMeta().getDisplayName().stripTrailing().equalsIgnoreCase("jrides:frame-inspect")){
            getParentTrain().addPositionMessageListener(player);
            player.sendMessage(ChatColor.GRAY + "(debug) Now inspecting frames");
        }
    }

    @Override
    public void onPlayerExit(Player player) {
        getParentTrain().onPlayerExit(player);
        getParentTrain().removePositionMessageListener(player);
    }

    @Override
    public void setParentTrain(Train train) {
        parentTrain = train;
    }

    @Override
    public Train getParentTrain() {
        return parentTrain;
    }

    @Override
    public void ejectPassengers() {
        seats.forEach(s -> {
            Player passenger = s.getPassenger();
            if(passenger != null){
                s.setPassenger(null);
                PlayerLocation ejectLocation = (parentTrain.isStationary())
                        ? parentTrain.getStationaryAt().getEjectLocation()
                        : getParentTrain().getHandle().getCoasterHandle().getEjectLocation();
                if(ejectLocation != null)
                    passenger.teleport(ejectLocation, true);
            }
        });
    }

    @Override
    public boolean shouldFaceForwards() {
        return !frame.isInvertedFrameAddition();
    }

    @Override
    public void setInvertedFrameAddition(boolean inverted) {
        frame.setInvertedFrameAddition(inverted);
    }


    @Override
    public void setNextEffect(TrainEffectTriggerHandle nextEffect) {
        this.nextEffect = nextEffect;
        this.hasEffects = true;
    }

    @Override
    public void playEffects() {

    }

    @Override
    public void despawn() {
        modelEntity.despawn();
    }
}
