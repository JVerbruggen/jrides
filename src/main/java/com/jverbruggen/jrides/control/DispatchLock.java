package com.jverbruggen.jrides.control;

import java.util.List;
import java.util.function.Consumer;

public interface DispatchLock {
    String getDescription();

    void lock();

    void unlock();

    void setStatus(String status);
    void setDebugStatus(String status);

    boolean isUnlocked();

    void setLocked(boolean locked);

    List<String> getProblems(int detailLevel, boolean debug);

    void addEventListener(Consumer<DispatchLock> eventListener);
}
