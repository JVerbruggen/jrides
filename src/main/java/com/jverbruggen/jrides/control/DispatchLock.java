package com.jverbruggen.jrides.control;

import java.util.List;
import java.util.function.Consumer;

public interface DispatchLock {
    String getDescription();

    void lock();

    void unlock();

    boolean isUnlocked();

    void setLocked(boolean locked);

    List<String> getProblems(int detailLevel);

    void addEventListener(Consumer<DispatchLock> eventListener);
}
