package com.jverbruggen.jrides.models.ride.gate;

public interface Gate {
    void open();
    void close();
    boolean isOpen();
}
