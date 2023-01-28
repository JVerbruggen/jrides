package com.jverbruggen.jrides.effect.platform;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.models.entity.armorstand.VirtualArmorstand;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import org.bukkit.Bukkit;

public class ArmorstandMovementEffectTrigger implements EffectTrigger {
    private final String identifier;
    private final VirtualArmorstand armorstand;

    private final Vector3 locationFrom;
    private final Vector3 locationTo;
    private final int animationTimeTicks;

    private Vector3 targetLocation;
    private int animationTickState;

    private boolean started;
    private int bukkitTimerTracker;
    private boolean finished;

    public ArmorstandMovementEffectTrigger(String identifier, VirtualArmorstand armorstand, Vector3 locationFrom, Vector3 locationTo, int animationTimeTicks) {
        this.identifier = identifier;
        this.armorstand = armorstand;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.animationTimeTicks = animationTimeTicks;

        this.targetLocation = null;
        this.started = false;
        this.finished = false;
        this.bukkitTimerTracker = -1;
    }

    public String getIdentifier() {
        return identifier;
    }

    public VirtualArmorstand getArmorstand() {
        return armorstand;
    }

    public Vector3 getLocationFrom() {
        return locationFrom;
    }

    public Vector3 getLocationTo() {
        return locationTo;
    }

    public int getAnimationTimeTicks() {
        return animationTimeTicks;
    }

    protected void tick(){
        if(animationTickState >= getAnimationTimeTicks()){
            stop();
            return;
        }

        Vector3 currentLocation = armorstand.getLocation();
        Vector3 delta = Vector3.subtract(targetLocation, currentLocation);

        double multiplication = 1.0 / (getAnimationTimeTicks()-animationTickState);
        Vector3 newLocation = Vector3.add(Vector3.multiply(delta, multiplication), currentLocation);
        armorstand.setLocation(newLocation, 0);

        animationTickState++;
    }

    @Override
    public void execute(Train train) {
        if(started) return;

        targetLocation = locationTo;
        start();
    }

    @Override
    public void executeReversed(Train train) {
        if(started) return;

        targetLocation = locationFrom;
        start();
    }

    protected void start(){
        if(started) return;
        this.finished = false;
        this.started = true;
        animationTickState = 0;

        bukkitTimerTracker = Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 1L, 1L).getTaskId();
    }

    protected void stop(){
        if(bukkitTimerTracker == -1) return;
        this.started = false;
        this.finished = true;

        Bukkit.getScheduler().cancelTask(bukkitTimerTracker);
        bukkitTimerTracker = -1;
    }

    @Override
    public boolean finishedPlaying() {
        return finished;
    }
}
