package com.jverbruggen.jrides.effect.entity.common;

import com.jverbruggen.jrides.JRidesPlugin;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class DelayedEntityTask {
    private final Runnable runnable;
    private final List<Runnable> onFinishRunnables;
    private final int animationTicks;
    private final int delayTicks;
    private int bukkitTimerTracker;
    private boolean started;
    private boolean finished;

    private int animationTickState;
    private int delayTickState;

    public DelayedEntityTask(Runnable runnable, int delayTicks, int animationTicks){
        this.runnable = runnable;
        this.onFinishRunnables = new ArrayList<>();
        this.animationTicks = animationTicks;
        this.delayTicks = delayTicks;
        this.started = false;
        this.finished = false;
        this.bukkitTimerTracker = -1;
    }

    public DelayedEntityTask(Runnable runnable, int delayTicks){
        this(runnable, delayTicks, 0);
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isBusy(){
        return started;
    }

    public void addOnFinishRunnable(Runnable onFinishRunnable) {
        this.onFinishRunnables.add(onFinishRunnable);
    }

    public int getAnimationTicks() {
        return animationTicks;
    }

    public int getCurrentAnimationTick(){
        return animationTickState;
    }

    private void runTask(){
        if(delayTickState < delayTicks){
            delayTickState++;
            return;
        }

        if(animationTicks == 0){
            runOnceAndStop();
            return;
        }

        runUntilAnimationDone();
    }

    private void runOnceAndStop(){
        runnable.run();
        stop();
    }

    private void runUntilAnimationDone(){
        if(animationTickState >= animationTicks){
            stop();
            return;
        }

        runnable.run();

        animationTickState++;
    }

    public void start(){
        if(started) return;
        this.started = true;
        this.finished = false;
        this.delayTickState = 0;
        this.animationTickState = 0;

        this.bukkitTimerTracker = Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::runTask, 1L, 1L).getTaskId();
    }

    private void stop(){
        if(bukkitTimerTracker == -1) return;
        this.started = false;
        this.finished = true;
        if(!onFinishRunnables.isEmpty())
            onFinishRunnables.forEach(Runnable::run);

        Bukkit.getScheduler().cancelTask(bukkitTimerTracker);
        bukkitTimerTracker = -1;
    }
}
