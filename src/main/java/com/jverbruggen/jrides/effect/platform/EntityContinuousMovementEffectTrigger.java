package com.jverbruggen.jrides.effect.platform;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.Bukkit;

public class EntityContinuousMovementEffectTrigger extends BaseTrainEffectTrigger implements EntityMovementTrigger {
    private final VirtualEntity virtualEntity;
    private Runnable onFinishRunnable;

    private final Vector3 initialLocation;
    private final Quaternion initialRotation;
    private final boolean resetOnStart;

    private final Vector3 locationDelta;
    private final Quaternion rotationDelta;
    private final int animationTimeTicks;

    private int animationTickState;

    private boolean started;
    private int bukkitTimerTracker;
    private boolean finished;

    public EntityContinuousMovementEffectTrigger(VirtualEntity virtualEntity, Vector3 initialLocation, Quaternion initialRotation, boolean resetOnStart, Vector3 locationDelta, Quaternion rotationDelta, int animationTimeTicks) {
        this.virtualEntity = virtualEntity;
        this.onFinishRunnable = null;
        this.locationDelta = locationDelta;
        this.rotationDelta = rotationDelta;
        this.animationTimeTicks = animationTimeTicks;

        this.initialLocation = initialLocation;
        this.initialRotation = initialRotation;
        this.resetOnStart = resetOnStart;

        this.started = false;
        this.finished = false;
        this.bukkitTimerTracker = -1;
    }

    public VirtualEntity getVirtualEntity() {
        return virtualEntity;
    }

    public int getAnimationTimeTicks() {
        return animationTimeTicks;
    }

    protected void tick(){
        if(animationTickState >= getAnimationTimeTicks()){
            stop();
            return;
        }

        addPosition();
        addRotation();

        animationTickState++;
    }

    private void addRotation(){
        if(rotationDelta == null) return;

        virtualEntity.setRotation(Quaternion.multiply(virtualEntity.getRotation(), rotationDelta));
    }

    private void addPosition(){
        if(locationDelta == null) return;

        virtualEntity.setLocation(Vector3.add(locationDelta, virtualEntity.getLocation()));
    }

    @Override
    public void execute(Train train) {
        if(started) return;

        start();
    }

    @Override
    public void executeReversed(Train train) {
        execute(train);
    }

    protected void start(){
        if(started) return;
        this.finished = false;
        this.started = true;
        animationTickState = 0;

        if(resetOnStart){
            virtualEntity.setLocation(initialLocation);
            virtualEntity.setRotation(initialRotation);
        }

        bukkitTimerTracker = Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 1L, 1L).getTaskId();
    }

    protected void stop(){
        if(bukkitTimerTracker == -1) return;
        this.started = false;
        this.finished = true;
        if(onFinishRunnable != null) onFinishRunnable.run();

        Bukkit.getScheduler().cancelTask(bukkitTimerTracker);
        bukkitTimerTracker = -1;
    }

    @Override
    public boolean finishedPlaying() {
        return finished;
    }

    @Override
    public void onFinish(Runnable runnable) {
        onFinishRunnable = runnable;
    }
}
