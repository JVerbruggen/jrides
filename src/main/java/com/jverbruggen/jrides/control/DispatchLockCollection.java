package com.jverbruggen.jrides.control;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DispatchLockCollection {
    private final List<DispatchLock> locks;
    private final List<Consumer<DispatchLock>> lockEventListeners;
    private final List<Consumer<DispatchLock>> unlockEventListeners;

    public DispatchLockCollection() {
        this.locks = new ArrayList<>();
        this.lockEventListeners = new ArrayList<>();
        this.unlockEventListeners = new ArrayList<>();
    }

    public void addLockEventListener(Consumer<DispatchLock> lockEventListener){
        lockEventListeners.add(lockEventListener);
    }

    public void addUnlockEventListener(Consumer<DispatchLock> unlockEventListener){
        unlockEventListeners.add(unlockEventListener);
    }

    public void addDispatchLock(DispatchLock dispatchLock){
        this.locks.add(dispatchLock);
    }

    public boolean allUnlocked(){
        return locks.stream().allMatch(DispatchLock::isUnlocked);
    }

    public List<String> getProblems(){
        List<String> problems = new ArrayList<>();
        for(DispatchLock lock : locks){
            if(!lock.isUnlocked()){
                problems.add(ChatColor.GRAY + "- " + lock.getDescription());
            }
        }
        return problems;
    }

    public String getProblemString(){
        List<String> problems = getProblems();
        return problems.stream()
                .map(p -> ChatColor.YELLOW + p)
                .collect(Collectors.joining("\n"));
    }

    public void onLock(DispatchLock lock){
        lockEventListeners.forEach(l -> l.accept(lock));
    }

    public void onUnlock(DispatchLock lock){
        unlockEventListeners.forEach(l -> l.accept(lock));
    }
}
