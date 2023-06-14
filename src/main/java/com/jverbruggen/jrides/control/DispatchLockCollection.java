package com.jverbruggen.jrides.control;

import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DispatchLockCollection implements DispatchLock {
    private final String description;
    private final List<DispatchLock> locks;
    private final List<Consumer<DispatchLock>> lockEventListeners;
    private final List<Consumer<DispatchLock>> unlockEventListeners;
    private DispatchLockCollection parentCollection;
    private boolean noticeSelfAsProblem;
    private @Nullable String statusCached;
    private @Nullable String debugStatusCached;

    public DispatchLockCollection(String description) {
        this.description = description;
        this.locks = new ArrayList<>();
        this.lockEventListeners = new ArrayList<>();
        this.unlockEventListeners = new ArrayList<>();
        this.parentCollection = null;
        this.noticeSelfAsProblem = false;
        this.statusCached = null;
        this.debugStatusCached = null;
    }

    public DispatchLockCollection(String description, DispatchLockCollection parentCollection) {
        this.description = description;
        this.locks = new ArrayList<>();
        this.lockEventListeners = new ArrayList<>();
        this.unlockEventListeners = new ArrayList<>();
        this.noticeSelfAsProblem = true;

        this.setParentCollection(parentCollection);
        this.statusCached = null;
        this.debugStatusCached = null;
    }

    public void addLockEventListener(Consumer<DispatchLock> lockEventListener){
        lockEventListeners.add(lockEventListener);
    }

    public void addUnlockEventListener(Consumer<DispatchLock> unlockEventListener){
        unlockEventListeners.add(unlockEventListener);
    }

    public void setParentCollection(DispatchLockCollection parentCollection) {
        this.parentCollection = parentCollection;

        this.parentCollection.addDispatchLock(this);
    }

    public DispatchLockCollection getParentCollection() {
        return parentCollection;
    }

    public void addEventListener(Consumer<DispatchLock> eventListener){
        addLockEventListener(eventListener);
        addUnlockEventListener(eventListener);
    }

    public void addDispatchLock(DispatchLock dispatchLock){
        this.locks.add(dispatchLock);
    }

    public boolean allUnlocked(){
        return locks.stream().allMatch(DispatchLock::isUnlocked);
    }

    @Override
    public List<String> getProblems(int detailLevel, boolean debug){
        List<String> problems = new ArrayList<>();
        if(noticeSelfAsProblem && !this.isUnlocked())
            problems.add(ChatColor.GRAY + "- " + this.getDescription());

        if(detailLevel <= 1 && noticeSelfAsProblem) return problems;
        int newDetailLevel = detailLevel;
        if(noticeSelfAsProblem) newDetailLevel--;

        for(DispatchLock lock : locks){
            if(!lock.isUnlocked()){
                problems.addAll(lock.getProblems(newDetailLevel, debug));
            }
        }

        return problems;
    }

    public void onLock(DispatchLock lock){
        lockEventListeners.forEach(l -> l.accept(lock));
        if(parentCollection != null) parentCollection.onLock(lock);
    }

    public void onUnlock(DispatchLock lock){
        unlockEventListeners.forEach(l -> l.accept(lock));
        if(parentCollection != null) parentCollection.onUnlock(lock);
    }

    public void onStatusUpdate(DispatchLock lock){
        unlockEventListeners.forEach(l -> l.accept(lock));
        if(parentCollection != null) parentCollection.onStatusUpdate(lock);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void lock() {
        locks.forEach(DispatchLock::lock);
    }

    @Override
    public void unlock() {
        locks.forEach(DispatchLock::unlock);
    }

    @Override
    public void setStatus(String status) {
        if(!Objects.equals(statusCached, status)){
            statusCached = status;
            unlockEventListeners.forEach(l -> l.accept(this));
        }
    }

    @Override
    public void setDebugStatus(String status) {
        if(!Objects.equals(debugStatusCached, status)){
            debugStatusCached = status;
            unlockEventListeners.forEach(l -> l.accept(this));
        }
    }

    @Override
    public boolean isUnlocked() {
        return allUnlocked();
    }

    @Override
    public void setLocked(boolean locked) {
        locks.forEach(l -> l.setLocked(locked));
    }

    @Override
    public String toString() {
        return "DispatchLockCollection{" +
                "description='" + description + '\'' +
                ", locks=" + locks +
                ", statusCached='" + statusCached + '\'' +
                '}';
    }
}
