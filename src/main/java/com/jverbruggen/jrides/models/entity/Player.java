package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimationSupport;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileFields;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.Seat;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Player implements MessageReceiver {
    private org.bukkit.entity.Player bukkitPlayer;
    private Seat seatedOn;
    private SmoothAnimationSupport smoothAnimationSupport;
    private LanguageFile languageFile;
    private RideController operating;
    private List<VirtualEntity> viewing;
    private RideCounterRecordCollection rideCounters;

    public Player(org.bukkit.entity.Player bukkitPlayer, RideCounterRecordCollection rideCounters) {
        this.bukkitPlayer = bukkitPlayer;
        this.seatedOn = null;
        this.smoothAnimationSupport = SmoothAnimationSupport.UNKNOWN;
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.operating = null;
        this.viewing = new ArrayList<>();
        this.rideCounters = rideCounters;
    }

    public RideCounterRecordCollection getRideCounters() {
        return rideCounters;
    }

    public Vector3 getLocation(){
        Location location = bukkitPlayer.getLocation();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return new Vector3(x, y, z);
    }

    @Override
    public void sendMessage(String message){
        bukkitPlayer.sendMessage(message);
    }

    public void sendActionbarMessage(String message){
        bukkitPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public org.bukkit.entity.Player getBukkitPlayer(){
        return bukkitPlayer;
    }

    public void setSeatedOn(Seat seat){
        this.seatedOn = seat;
    }

    public Seat getSeatedOn(){
        return this.seatedOn;
    }

    public boolean isSeated(){
        return this.seatedOn != null;
    }

    public void setPositionWithoutTeleport(Vector3 position){
        JRidesPlugin.getPacketSender().sendClientPositionPacket(this, position);
    }

    public boolean shouldResetSmoothAnimationSupport(){
        return smoothAnimationSupport.equals(SmoothAnimationSupport.UNKNOWN);
    }

    public boolean hasSmoothAnimationSupport() {
        return smoothAnimationSupport.equals(SmoothAnimationSupport.AVAILABLE);
    }

    public void setSmoothAnimationSupport(boolean smoothAnimationSupport) {
        if(this.smoothAnimationSupport != SmoothAnimationSupport.UNKNOWN) return; // Cannot reset smooth animation without rejoin

        this.smoothAnimationSupport = (smoothAnimationSupport ? SmoothAnimationSupport.AVAILABLE : SmoothAnimationSupport.UNAVAILABLE);
        if(!smoothAnimationSupport){
            languageFile.sendMessage(bukkitPlayer, LanguageFileFields.ERROR_SMOOTH_COASTERS_DISABLED, FeedbackType.WARNING);
        }
    }

    public void clearSmoothAnimationRotation(){
        if(!hasSmoothAnimationSupport()) return;
        JRidesPlugin.getSmoothAnimation().clearRotation(this);
    }

    public void setSmoothAnimationRotation(Quaternion orientation){
        if(!hasSmoothAnimationSupport()) return;
        JRidesPlugin.getSmoothAnimation().setRotation(this, orientation);
    }

    public String getName() {
        return bukkitPlayer.getName();
    }

    public String getIdentifier(){
        return bukkitPlayer.getUniqueId().toString();
    }

    public boolean equals(Player other) {
        if(other == null) return false;
        return this.getBukkitPlayer().getUniqueId().equals(other.getBukkitPlayer().getUniqueId());
    }

    public boolean setOperating(RideController rideController) {
        if(rideController != null && rideController.getOperator() == this && operating == rideController) return true;

        if(operating != null){
            operating.setOperator(null);
            operating = null;
        }

        if(rideController == null) return true;

        boolean set = rideController.setOperator(this);
        if(!set){
            languageFile.sendMessage(this, LanguageFileFields.ERROR_OPERATING_CABIN_OCCUPIED, FeedbackType.CONFLICT);
            return false;
        }

        operating = rideController;
        return true;
    }

    public void clearOperating(){
        operating = null;
    }

    public void addViewing(VirtualEntity virtualEntity){
        if(isViewing(virtualEntity)) return;
        viewing.add(virtualEntity);
    }

    public void removeViewing(VirtualEntity virtualEntity){
        viewing.remove(virtualEntity);
    }

    public boolean isViewing(VirtualEntity virtualEntity){
        return viewing.contains(virtualEntity);
    }

    public List<VirtualEntity> getViewing() {
        return viewing;
    }

    public void teleport(PlayerLocation location) {
        World world = bukkitPlayer.getWorld();
        bukkitPlayer.teleport(location.toBukkitLocation(world));
    }

    public void teleport(Vector3 location){
        World world = bukkitPlayer.getWorld();
        bukkitPlayer.teleport(location.toBukkitLocation(world));
    }

    public void teleport(Vector3 location, double yaw){
        World world = bukkitPlayer.getWorld();
        bukkitPlayer.teleport(location.toBukkitLocation(world, yaw));
    }
}

