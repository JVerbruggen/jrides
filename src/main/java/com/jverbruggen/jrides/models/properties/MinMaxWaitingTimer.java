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

package com.jverbruggen.jrides.models.properties;

import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.language.StringReplacementBuilder;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public class MinMaxWaitingTimer {
    private final int minimumWaitingTime;
    private final int maximumWaitingTime;
    private final DispatchLock lock;
    private final LanguageFile languageFile;

    private double preferredWaitingTime;
    private double waitingTimerState;
    private int waitingTimerInt;
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

        if(waitingTimerInt != (int)waitingTimerState){
            waitingTimerInt = (int) waitingTimerState;
            sendStatusMessage();
        }

        if(reachedFunction()){
            this.lock.unlock();
        }
    }

    public void reset(){
        waitingTimerState = 0d;
        waitingTimerInt = 0;
        preferredWaitingTime = minimumWaitingTime;
        sendStatusMessage();

        if(reachedFunction())
            this.lock.unlock();
        else
            this.lock.lock();
    }

    private void sendStatusMessage(){
        this.lock.setStatus(waitingTimerInt + "s / " + (int)preferredWaitingTime + "s waited");
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
                .add(LanguageFileTag.time, visualDispatchTime + "")
                .apply(languageFile.get(LanguageFileField.NOTIFICATION_DISPATCH_WAIT_SPECIFIC));

        players.forEach(p -> p.sendActionbarMessage(ChatColor.GOLD + waitingTimeNotification));
    }

    public void sendGenericWaitingNotification(List<Player> players){
        players.forEach(p -> p.sendActionbarMessage(ChatColor.GOLD + languageFile.get(LanguageFileField.NOTIFICATION_DISPATCH_WAIT_GENERIC)));
    }

    @Override
    public String toString() {
        return "<Timer " + waitingTimerState + " / " + preferredWaitingTime + ">";
    }
}
