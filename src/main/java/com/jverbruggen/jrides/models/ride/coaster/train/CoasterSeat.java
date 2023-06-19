package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.event.player.PlayerSitDownEvent;
import com.jverbruggen.jrides.event.player.PlayerStandUpEvent;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.state.ride.SoftEjector;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class CoasterSeat implements Seat {
    private final RideHandle parentRideHandle;
    private Player passenger;
    private final VirtualArmorstand virtualArmorstand;
    private final Vector3 offset;
    private boolean restraintLocked;
    private Cart parentCart;

    public CoasterSeat(RideHandle parentRideHandle, VirtualArmorstand virtualArmorstand, Vector3 offset) {
        this.parentRideHandle = parentRideHandle;
        this.passenger = null;
        this.virtualArmorstand = virtualArmorstand;
        this.offset = offset;
        this.restraintLocked = true;
        this.parentCart = null;
    }

    @Override
    public Player getPassenger() {
        return passenger;
    }

    @Override
    public void setPassenger(Player player) {
        if(passenger != null){ // Overtaking seat or player = null
            passenger.setSeatedOn(null);
            virtualArmorstand.setPassenger(null);
            passenger.clearSmoothAnimationRotation();
            parentCart.getParentTrain().onPlayerExit(passenger);
            PlayerStandUpEvent.send(passenger, parentCart.getParentTrain().getHandle().getCoasterHandle().getRide());

            getParentCart().getParentTrain().removePositionMessageListener(passenger);
        }

        passenger = player;
        virtualArmorstand.setPassenger(player);
        if(player != null){
            if(!player.hasPermission(Permissions.RIDE_ENTER)){
                JRidesPlugin.getLanguageFile().sendMessage(player, LanguageFileField.ERROR_GENERAL_NO_PERMISSION_MESSAGE);
                return;
            }

            player.setSeatedOn(this);
            parentCart.getParentTrain().onPlayerEnter(player);
            PlayerSitDownEvent.send(passenger, parentCart.getParentTrain().getHandle().getCoasterHandle().getRide());

            // Potentially rider wants to inspect frames
            org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();
            ItemStack itemInHand = bukkitPlayer.getInventory().getItemInMainHand();
            if(bukkitPlayer.hasPermission(Permissions.ELEVATED_STATUS_INSPECTION)
                    && itemInHand.getItemMeta() != null
                    && itemInHand.getItemMeta().getDisplayName().stripTrailing().equalsIgnoreCase("jrides:frame-inspect")){
                getParentCart().getParentTrain().addPositionMessageListener(player);
                player.sendMessage(ChatColor.GRAY + "(debug) Now inspecting frames");
            }
        }
    }

    @Override
    public boolean hasPassenger() {
        return passenger != null;
    }

    @Override
    public boolean ejectPassengerSoft(boolean teleport) {
        if(!hasPassenger()) return false;
        Player passenger = getPassenger();

        if(SoftEjector.hasTimer(passenger)){
            SoftEjector.removeTimer(passenger);
            setPassenger(null);
            if(teleport){
                PlayerLocation ejectLocation = getParentCart().getParentTrain().getHandle().getCoasterHandle().getEjectLocation();
                passenger.teleport(ejectLocation, true);
            }
            return true;
        }else{
            SoftEjector.addTimer(passenger);
            JRidesPlugin.getLanguageFile().sendMessage(passenger, LanguageFileField.NOTIFICATION_SHIFT_EXIT_CONFIRMATION);
            return false;
        }
    }

    @Override
    public Vector3 getOffset() {
        return offset;
    }

    @Override
    public void setLocation(Vector3 location, Quaternion orientation) {
        virtualArmorstand.setLocation(location, orientation);

        if(hasPassenger()){
            Quaternion smoothAnimationRotation = orientation.clone();
            smoothAnimationRotation.rotateY(90);
            passenger.setSmoothAnimationRotation(smoothAnimationRotation);
        }
    }

    @Override
    public VirtualEntity getEntity() {
        return virtualArmorstand;
    }

    @Override
    public void setRestraint(boolean locked) {
        restraintLocked = locked;
    }

    @Override
    public boolean restraintsActive() {
        return restraintLocked;
    }

    @Override
    public void setParentCart(Cart cart) {
        this.parentCart = cart;
    }

    @Override
    public Cart getParentCart() {
        return this.parentCart;
    }

    public static Vector3 getHeightCompensation(){
        return new Vector3(0, 1.5, 0);
    }

    @Override
    public RideHandle getParentRideHandle() {
        return parentRideHandle;
    }
}
