package com.jverbruggen.jrides.config.utils.integersupplier;

import java.util.List;
import java.util.function.Supplier;

public class CycleIntegerSupplier implements Supplier<Integer> {
    private final List<Integer> cycle;
    private int pointer;

    public CycleIntegerSupplier(List<Integer> cycle) {
        this.cycle = cycle;
        this.pointer = 0;
    }

    @Override
    public Integer get() {
        int newPointer = pointer;
        pointer = (pointer + 1) % cycle.size();
        return cycle.get(newPointer);
    }
}
