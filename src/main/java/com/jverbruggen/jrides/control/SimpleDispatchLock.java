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
    private String debugStatus;

    /**
     * Simple implementation of a DispatchLock
     * @param parentCollection the parent collection that it is part of.
     * @param description Description of what the lock is blocking for.
     * @param initLocked Whether the lock should initially be locked (preventing the ride from dispatching) or unlocked (allowing dispatching to happen)
     */
    public SimpleDispatchLock(DispatchLockCollection parentCollection, String description, boolean initLocked) {
        this.parentCollection = parentCollection;
        this.description = description;
        this.locked = initLocked;
        this.eventListeners = new ArrayList<>();
        this.status = "";
        this.debugStatus = "";

        this.parentCollection.addDispatchLock(this);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
        eventListeners.forEach(l -> l.accept(this));
        parentCollection.onStatusUpdate(this);
    }

    @Override
    public void setDebugStatus(String status) {
        debugStatus = status;
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
    public List<String> getProblems(int detailLevel, boolean debug) {
        if(detailLevel <= 0) return List.of();

        String statusSpec = status.equals("") ? "" : " (" + status + ")";
        if(debug) statusSpec += debugStatus.equals("") ? "" : " [" + debugStatus + "]";

        return List.of(ChatColor.GRAY + "- " + this.getDescription() + statusSpec);
    }

    @Override
    public void addEventListener(Consumer<DispatchLock> eventListener) {
        eventListeners.add(eventListener);
    }

    @Override
    public String toString() {
        return "SimpleDispatchLock{" +
                "locked=" + locked +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", debugStatus='" + debugStatus + '\'' +
                '}';
    }
}
