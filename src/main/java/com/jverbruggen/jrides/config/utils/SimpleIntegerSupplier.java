package com.jverbruggen.jrides.config.utils;

import java.util.function.Supplier;

public class SimpleIntegerSupplier implements Supplier<Integer> {
    private final int value;

    public SimpleIntegerSupplier(int value) {
        this.value = value;
    }

    @Override
    public Integer get() {
        return value;
    }
}
