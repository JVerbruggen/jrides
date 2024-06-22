package com.jverbruggen.jrides.config.utils.integersupplier;

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
