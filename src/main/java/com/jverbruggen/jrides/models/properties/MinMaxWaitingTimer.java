package com.jverbruggen.jrides.models.properties;

import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.ChatColor;

import java.util.List;

public class MinMaxWaitingTimer {
    private final int minimumWaitingTime;
    private final int maximumWaitingTime;
    private final DispatchLock lock;

    private double preferredWaitingTime;
    private double waitingTimerState;

    public MinMaxWaitingTimer(int minimumWaitingTime, int maximumWaitingTime, DispatchLock lock) {
        this.minimumWaitingTime = minimumWaitingTime;
        this.maximumWaitingTime = maximumWaitingTime;
        this.lock = lock;

        reset();
    }

    public boolean reachedMinimum(){
        return minimumWaitingTime <= waitingTimerState;
    }

    public boolean reachedMaximum(){
        return maximumWaitingTime <= waitingTimerState;
    }

    public boolean reachedPreferred() {
        return preferredWaitingTime <= waitingTimerState;
    }

    public void increment(long tickInterval){
        waitingTimerState += (double)tickInterval/20d;

        if(reachedPreferred()){
            this.lock.unlock();
        }
    }

    public void reset(){
        waitingTimerState = 0d;
        preferredWaitingTime = minimumWaitingTime;
        this.lock.lock();
    }

    public int timeUntilMinimum(){
        return minimumWaitingTime - (int) waitingTimerState;
    }

    public int timeUntilMaximum(){
        return maximumWaitingTime - (int) waitingTimerState;
    }

    public double timeUntilPreferredWaitingTime(){
        return preferredWaitingTime - waitingTimerState;
    }

    public void setPreferredWaitingTimeFromNow(int addTime){
        double newPreferredWaitingTime = waitingTimerState + addTime;

        if(newPreferredWaitingTime < minimumWaitingTime) newPreferredWaitingTime = minimumWaitingTime;
        else if(newPreferredWaitingTime > maximumWaitingTime) newPreferredWaitingTime = maximumWaitingTime;

        preferredWaitingTime = newPreferredWaitingTime;
    }

    public int getMinimumWaitingTime() {
        return minimumWaitingTime;
    }

    public int getMaximumWaitingTime() {
        return maximumWaitingTime;
    }

    public int getVisualDispatchTime(double timeUntil){
        if(timeUntil < 0) timeUntil = 0;
        return (int) Math.ceil(timeUntil);
    }

    public void sendTimeWaitingNotification(List<Player> players, int visualDispatchTime){
        if(visualDispatchTime <= 0){
            players.forEach(p -> p.sendActionbarMessage(""));
            return;
        }

        players.forEach(p -> p.sendActionbarMessage(ChatColor.GOLD + "Waiting time: " + visualDispatchTime + " seconds"));
    }

    public void sendGenericWaitingNotification(List<Player> players){
        players.forEach(p -> p.sendActionbarMessage(ChatColor.GOLD + "Please wait until the ride is dispatched"));
    }
}