package com.jverbruggen.jrides.models.properties;

import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileTags;
import com.jverbruggen.jrides.language.StringReplacementBuilder;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MinMaxWaitingTimer {
    private final int minimumWaitingTime;
    private final int maximumWaitingTime;
    private final DispatchLock lock;
    private final LanguageFile languageFile;

    private double preferredWaitingTime;
    private double waitingTimerState;
    private Supplier<Boolean> reachedTimeFunction;

    public MinMaxWaitingTimer(int minimumWaitingTime, int maximumWaitingTime, DispatchLock lock) {
        this.minimumWaitingTime = minimumWaitingTime;
        this.maximumWaitingTime = maximumWaitingTime;
        this.lock = lock;
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.reachedTimeFunction = this::reachedPreferred;

        reset();
    }

    public void setReachedTimeFunction(@Nonnull Supplier<Boolean> function){
        this.reachedTimeFunction = function;
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

    public boolean reachedFunction(){
        return reachedTimeFunction.get();
    }

    public void increment(long tickInterval){
        waitingTimerState += (double)tickInterval/20d;

        if(reachedFunction()){
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

        String waitingTimeNotification = new StringReplacementBuilder()
                .add(LanguageFileTags.time, visualDispatchTime + "")
                .apply(languageFile.notificationDispatchWaitSpecific);

        players.forEach(p -> p.sendActionbarMessage(ChatColor.GOLD + waitingTimeNotification));
    }

    public void sendGenericWaitingNotification(List<Player> players){
        players.forEach(p -> p.sendActionbarMessage(ChatColor.GOLD + languageFile.notificationDispatchWaitGeneric));
    }

    @Override
    public String toString() {
        return "<Timer " + waitingTimerState + " / " + preferredWaitingTime + ">";
    }
}
