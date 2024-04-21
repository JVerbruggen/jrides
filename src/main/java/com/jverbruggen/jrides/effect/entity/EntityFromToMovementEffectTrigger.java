package com.jverbruggen.jrides.effect.entity;

import com.jverbruggen.jrides.effect.entity.common.DelayedEntityTask;
import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

public class EntityFromToMovementEffectTrigger extends BaseTrainEffectTrigger implements EntityMovementTrigger {
    private final VirtualEntity virtualEntity;

    private final DelayedEntityTask delayedEntityTask;

    private final Vector3 locationFrom;
    private final Vector3 locationTo;
    private final Quaternion rotationFrom;
    private final Quaternion rotationTo;

    private Vector3 targetLocation;

    public EntityFromToMovementEffectTrigger(VirtualEntity virtualEntity, Vector3 locationFrom, Vector3 locationTo, Quaternion rotationFrom, Quaternion rotationTo, int animationTimeTicks, int delayTicks) {
        this.virtualEntity = virtualEntity;

        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.rotationFrom = rotationFrom;
        this.rotationTo = rotationTo;

        this.targetLocation = null;
        this.delayedEntityTask = new DelayedEntityTask(this::runTask, delayTicks, animationTimeTicks);
    }

    public int getAnimationTimeTicks() {
        return delayedEntityTask.getAnimationTicks();
    }

    private void runTask(){
        lerpPosition();
        lerpRotation();
    }

    private void lerpRotation(){
        if(rotationFrom == null || rotationTo == null) return;

        double lerpIncrement = 1d/(double)getAnimationTimeTicks();
        double theta = lerpIncrement * delayedEntityTask.getCurrentAnimationTick();
        virtualEntity.setRotation(Quaternion.lerp(rotationFrom, rotationTo, theta));
    }

    private void lerpPosition(){
        if(locationFrom == null || locationTo == null) return;

        Vector3 currentLocation = virtualEntity.getLocation();
        Vector3 delta = Vector3.subtract(targetLocation, currentLocation);
        double multiplication = 1.0 / (getAnimationTimeTicks() - delayedEntityTask.getCurrentAnimationTick());
        Vector3 newLocation = Vector3.add(Vector3.multiply(delta, multiplication), currentLocation);
        virtualEntity.setLocation(newLocation);
    }

    @Override
    public void execute(Train train) {
        if(delayedEntityTask.isBusy()) return;

        targetLocation = locationTo;
        delayedEntityTask.start();
    }

    @Override
    public void executeReversed(Train train) {
        if(delayedEntityTask.isBusy()) return;

        targetLocation = locationFrom;
        delayedEntityTask.start();
    }

    @Override
    public boolean finishedPlaying() {
        return delayedEntityTask.isFinished();
    }

    @Override
    public void onFinish(Runnable runnable) {
        delayedEntityTask.addOnFinishRunnable(runnable);
    }
}
