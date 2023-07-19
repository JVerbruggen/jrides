package com.jverbruggen.jrides.effect.platform;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.Bukkit;

public class EntityFromToMovementEffectTrigger extends BaseTrainEffectTrigger implements EntityMovementTrigger {
    private final VirtualEntity virtualEntity;
    private Runnable onFinishRunnable;

    private final Vector3 locationFrom;
    private final Vector3 locationTo;
    private final Quaternion rotationFrom;
    private final Quaternion rotationTo;
    private final int animationTimeTicks;
    private final int delayTicks;

    private Vector3 targetLocation;
    private int animationTickState;
    private int delayTickState;

    private boolean started;
    private int bukkitTimerTracker;
    private boolean finished;

    public EntityFromToMovementEffectTrigger(VirtualEntity virtualEntity, Vector3 locationFrom, Vector3 locationTo, Quaternion rotationFrom, Quaternion rotationTo, int animationTimeTicks, int delayTicks) {
        this.virtualEntity = virtualEntity;
        this.onFinishRunnable = null;

        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.rotationFrom = rotationFrom;
        this.rotationTo = rotationTo;
        this.animationTimeTicks = animationTimeTicks;
        this.delayTicks = delayTicks;

        this.targetLocation = null;
        this.started = false;
        this.finished = false;
        this.bukkitTimerTracker = -1;
    }

    public int getAnimationTimeTicks() {
        return animationTimeTicks;
    }

    protected void tick(){
        if(delayTickState < delayTicks){
            delayTickState++;
            return;
        }

        if(animationTickState >= getAnimationTimeTicks()){
            stop();
            return;
        }

        lerpPosition();
        lerpRotation();

        animationTickState++;
    }

    private void lerpRotation(){
        if(rotationFrom == null || rotationTo == null) return;

        double lerpIncrement = 1d/(double)getAnimationTimeTicks();
        double theta = lerpIncrement * animationTickState;
        virtualEntity.setRotation(Quaternion.lerp(rotationFrom, rotationTo, theta));
    }

    private void lerpPosition(){
        if(locationFrom == null || locationTo == null) return;

        Vector3 currentLocation = virtualEntity.getLocation();
        Vector3 delta = Vector3.subtract(targetLocation, currentLocation);
        double multiplication = 1.0 / (getAnimationTimeTicks()-animationTickState);
        Vector3 newLocation = Vector3.add(Vector3.multiply(delta, multiplication), currentLocation);
        virtualEntity.setLocation(newLocation);
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
        delayTickState = 0;

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
