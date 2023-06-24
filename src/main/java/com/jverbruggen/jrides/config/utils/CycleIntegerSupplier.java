package com.jverbruggen.jrides.config.utils;

import java.util.List;

public class CycleIntegerSupplier implements IntegerSupplier {
    private final List<Integer> cycle;
    private int pointer;

    public CycleIntegerSupplier(List<Integer> cycle) {
        this.cycle = cycle;
        this.pointer = 0;
    }

    @Override
    public int getInteger() {
        int newPointer = pointer;
        pointer = (pointer + 1) % cycle.size();
        return cycle.get(newPointer);
    }
}
