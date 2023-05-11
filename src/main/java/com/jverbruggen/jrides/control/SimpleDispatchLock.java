package com.jverbruggen.jrides.control;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleDispatchLock implements DispatchLock {
    private DispatchLockCollection parentCollection;
    private boolean locked;
    private String description;
    private final List<Consumer<DispatchLock>> eventListeners;
    private String status;

    public SimpleDispatchLock(DispatchLockCollection parentCollection, String description, boolean initLocked) {
        this.parentCollection = parentCollection;
        this.description = description;
        this.locked = initLocked;
        this.eventListeners = new ArrayList<>();
        this.status = "";

        this.parentCollection.addDispatchLock(this);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void lock(){
        if(locked) return;
        locked = true;
        parentCollection.onLock(this);
        eventListeners.forEach(l -> l.accept(this));
    }

    @Override
    public void unlock(){
        if(!locked) return;
        locked = false;
        parentCollection.onUnlock(this);
        eventListeners.forEach(l -> l.accept(this));
    }

    @Override
    public boolean isUnlocked(){
        return !locked;
    }

    @Override
    public void setLocked(boolean locked) {
        if(locked) lock();
        else unlock();
    }

    @Override
    public List<String> getProblems(int detailLevel) {
        if(detailLevel <= 0) return List.of();
        String statusSpec = status.equals("") ? "" : " (" + status + ")";
        return List.of(ChatColor.GRAY + "- " + this.getDescription() + statusSpec);
    }

    @Override
    public void addEventListener(Consumer<DispatchLock> eventListener) {
        eventListeners.add(eventListener);
    }
}
