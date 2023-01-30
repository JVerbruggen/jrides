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

    public SimpleDispatchLock(DispatchLockCollection parentCollection, String description, boolean initLocked) {
        this.parentCollection = parentCollection;
        this.description = description;
        this.locked = initLocked;
        this.eventListeners = new ArrayList<>();

        this.parentCollection.addDispatchLock(this);
    }

    @Override
    public String getDescription() {
        return description;
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
        return List.of(ChatColor.GRAY + "- " + this.getDescription());
    }

    @Override
    public void addEventListener(Consumer<DispatchLock> eventListener) {
        eventListeners.add(eventListener);
    }
}
