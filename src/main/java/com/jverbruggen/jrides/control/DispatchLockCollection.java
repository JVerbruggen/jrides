package com.jverbruggen.jrides.control;

import java.util.ArrayList;
import java.util.List;

public class DispatchLockCollection {
    private final List<DispatchLock> locks;

    public DispatchLockCollection() {
        this.locks = new ArrayList<>();
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
                problems.add("Problem: " + lock.getName());
            }
        }
        return problems;
    }
}
