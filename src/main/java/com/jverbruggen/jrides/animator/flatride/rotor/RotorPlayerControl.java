package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.flatride.PlayerControl;
import com.jverbruggen.jrides.models.ride.seat.InstructionType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class RotorPlayerControl implements PlayerControl {
    private Rotor rotor;
    private final float lowerSpeed;
    private final float upperSpeed;
    private final float acceleration;

    private float currentSpeed;
    private float pendingAcceleration;

    private List<Player> controlling;

    public RotorPlayerControl(float lowerSpeed, float upperSpeed, float acceleration) {
        this.rotor = null;
        this.lowerSpeed = lowerSpeed;
        this.upperSpeed = upperSpeed;
        this.acceleration = acceleration;
        this.currentSpeed = 0f;
        this.pendingAcceleration = 0f;
        this.controlling = null;
    }

    public void setRotor(Rotor rotor) {
        this.rotor = rotor;
        this.currentSpeed = this.rotor.getFlatRideComponentSpeed().getSpeed();
    }

    @Override
    public void processInstructionAsync(InstructionType instruction) {
        if(!rotor.allowsControl()) return;

        if(instruction == InstructionType.A){
            pendingAcceleration = -acceleration;
        }else if(instruction == InstructionType.D){
            pendingAcceleration = acceleration;
        }
    }

    @Override
    public void apply() {
        float acceleration = this.pendingAcceleration; // Synchronization?

        currentSpeed = rotor.getFlatRideComponentSpeed().getSpeed();
        float oldSpeed = currentSpeed;
        currentSpeed += acceleration;
        if(currentSpeed > upperSpeed){
            currentSpeed = upperSpeed;
            acceleration = upperSpeed - oldSpeed;
        }
        else if(currentSpeed < lowerSpeed){
            currentSpeed = lowerSpeed;
            acceleration = lowerSpeed - oldSpeed;
        }

        rotor.getFlatRideComponentSpeed().accelerate(acceleration);
        this.pendingAcceleration = 0;
    }

    @Override
    public void reset() {
        this.pendingAcceleration = 0;
        this.currentSpeed = rotor.getFlatRideComponentSpeed().getSpeed();
    }

    @Override
    public void addControlling(Player player) {
        if(controlling == null) controlling = new ArrayList<>();
        controlling.add(player);
    }

    @Override
    public void removeControlling(Player player) {
        if(controlling == null) return;
        controlling.remove(player);
        if(controlling.size() == 0) controlling = null;
    }

    @Override
    public void sendStartNotification() {
        if(controlling == null) return;

        controlling.forEach(
                p -> p.sendTitle(ChatColor.RED + "Control the cup!", ChatColor.GOLD + "Press A or D to control the cup", 60));
    }

    @Override
    public void sendStopNotification() {

    }
}
