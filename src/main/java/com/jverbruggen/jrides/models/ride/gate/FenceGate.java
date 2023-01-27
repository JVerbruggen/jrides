package com.jverbruggen.jrides.models.ride.gate;

import com.jverbruggen.jrides.control.DispatchLock;
import com.jverbruggen.jrides.models.math.Vector3;

public class FenceGate implements Gate {
    private final String name;
    private final DispatchLock dispatchLock;
    private final Vector3 location;
    private boolean isOpen;

    public FenceGate(String name, DispatchLock dispatchLock, Vector3 location) {
        this.name = name;
        this.dispatchLock = dispatchLock;
        this.location = location;
        this.isOpen = false;
    }

    public String getName() {
        return name;
    }

    @Override
    public void open() {
        isOpen = true;
        dispatchLock.lock();
    }

    @Override
    public void close() {
        isOpen = false;
        dispatchLock.unlock();
    }

    @Override
    public boolean isOpen() {
        return this.isOpen;
    }
}
