package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.event.player.PlayerSitDownEvent;
import com.jverbruggen.jrides.event.player.PlayerStandUpEvent;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.state.ride.SoftEjector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class CoasterSeat implements Seat {
    private Player passenger;
    private final VirtualArmorstand virtualArmorstand;
    private final Vector3 offset;
    private boolean restraintLocked;
    private Cart parentCart;

    public CoasterSeat(VirtualArmorstand virtualArmorstand, Vector3 offset) {
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
        if(passenger != null){ // Overtaking seat
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
            player.setSeatedOn(this);
            parentCart.getParentTrain().onPlayerEnter(player);
            PlayerSitDownEvent.send(passenger, parentCart.getParentTrain().getHandle().getCoasterHandle().getRide());

            // Potentially rider wants to inspect frames
            org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();
            ItemStack itemInHand = bukkitPlayer.getInventory().getItemInMainHand();
            if(bukkitPlayer.hasPermission(Permissions.STATUS_INSPECTION)
                    && itemInHand.getItemMeta().getDisplayName().stripTrailing().equalsIgnoreCase("jrides:frame-inspect")){
                getParentCart().getParentTrain().addPositionMessageListener(player);
                player.sendMessage("Now inspecting frames");
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
                passenger.teleport(ejectLocation);
            }
            return true;
        }else{
            SoftEjector.addTimer(passenger);
            JRidesPlugin.getLanguageFile().sendMessage(passenger, JRidesPlugin.getLanguageFile().notificationShiftExitConfirmation);
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
}
