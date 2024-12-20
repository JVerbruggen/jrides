/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.models.entity;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimationSupport;
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.event.player.PlayerTeleportByJRidesEvent;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.PlayerLocation;
import com.jverbruggen.jrides.models.ride.seat.Seat;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;
import com.jverbruggen.jrides.nms.NMSHandler;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player implements MessageAgent, JRidesPlayer {
    private final org.bukkit.entity.Player bukkitPlayer;
    private SeatedOnContext seatedOnContext;
    private SmoothAnimationSupport smoothAnimationSupport;
    private final LanguageFile languageFile;
    private RideController operating;
    private final List<VirtualEntity> viewing;
    private final RideCounterRecordCollection rideCounters;

    public Player(org.bukkit.entity.Player bukkitPlayer, RideCounterRecordCollection rideCounters) {
        this.bukkitPlayer = bukkitPlayer;
        this.seatedOnContext = null;
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
        if(message.equals("")) return;
        bukkitPlayer.sendMessage(message);
    }

    public void sendActionbarMessage(String message){
        bukkitPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public void sendTitle(String title, String subtitle, int stay){
        bukkitPlayer.sendTitle(title, subtitle, 10, stay, 10);
    }

    public org.bukkit.entity.Player getBukkitPlayer(){
        return bukkitPlayer;
    }

    public UUID getUniqueId(){
        return bukkitPlayer.getUniqueId();
    }

    public Seat getSeatedOn(){
        if(!isSeated()) return null;
        return this.seatedOnContext.getSeat();
    }

    public void setSeatedOnContext(SeatedOnContext seatedOnContext) {
        this.seatedOnContext = seatedOnContext;
    }

    public SeatedOnContext getSeatedOnContext() {
        return seatedOnContext;
    }

    public boolean isSeated(){
        return this.seatedOnContext != null;
    }

    public void setPositionWithoutTeleport(Vector3 position){
//        JRidesPlugin.getPacketSender().sendClientPositionPacket(this, position);
        ServiceProvider.getSingleton(NMSHandler.class).setPlayerLocationNoTeleport(this, position);
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
            languageFile.sendMessage(bukkitPlayer, LanguageFileField.ERROR_SMOOTH_COASTERS_DISABLED, FeedbackType.WARNING);
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
        // Check if player is already operating ride
        if(rideController != null && rideController.getOperator() == this && operating == rideController) return true;

        // If already operating something..
        if(operating != null){
            // stop operating it
            languageFile.sendMessage(this, LanguageFileField.NOTIFICATION_RIDE_CONTROL_INACTIVE,
                    (b) -> b.add(LanguageFileTag.rideDisplayName, operating.getRide().getDisplayName()));
            operating.setOperator(null);
            operating = null;
        }

        if(rideController == null) return true;

        if(!hasPermission(Permissions.CABIN_OPERATE)){
            languageFile.sendMessage(this, LanguageFileField.ERROR_OPERATING_NO_PERMISSION);
            return false;
        }

        // Try to operate the selected ride
        boolean set = rideController.setOperator(this);
        if(!set){
            languageFile.sendMessage(this, LanguageFileField.ERROR_OPERATING_CABIN_OCCUPIED);
            bukkitPlayer.closeInventory();
            return false;
        }

        // All is ok, setting operating field for player
        operating = rideController;

        languageFile.sendMessage(this, LanguageFileField.NOTIFICATION_RIDE_CONTROL_ACTIVE,
                builder -> builder.add(LanguageFileTag.rideDisplayName, operating.getRide().getDisplayName()));
        return true;
    }

    public void clearOperating(){
        operating = null;
    }

    public RideController getOperating(){
        return operating;
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

    public void teleport(PlayerLocation location){
        teleport(location, false);
    }

    public void teleport(PlayerLocation location, boolean hard) {
        PlayerTeleportByJRidesEvent.sendEvent(this, location, hard);
    }

    public void teleport(Vector3 location, double yaw){
        PlayerLocation playerLocation = PlayerLocation.fromVector3(location);
        playerLocation.setYaw(yaw);

        teleport(playerLocation, false);
    }

    public boolean hasPermission(String permission){
        return bukkitPlayer.hasPermission(permission);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public Player getPlayer(PlayerManager playerManager) {
        return this;
    }

    public void playSound(Sound sound){
        if(sound == null) return;
        bukkitPlayer.playSound(bukkitPlayer.getLocation(), sound, 1, 1);
    }

    public void setFly(boolean enabled){
        setFly(enabled, enabled);
    }

    public void setFly(boolean enabled, boolean allowFlight){
        bukkitPlayer.setAllowFlight(allowFlight);
        bukkitPlayer.setFlying(enabled);
    }
}

