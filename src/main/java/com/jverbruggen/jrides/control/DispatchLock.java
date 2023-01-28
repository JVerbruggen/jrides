package com.jverbruggen.jrides.control;

public interface DispatchLock {
    String getDescription();

    void lock();

    void unlock();

    boolean isUnlocked();

    void setLocked(boolean locked);
}
