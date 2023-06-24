package com.jverbruggen.jrides.config.utils;

public class SimpleIntegerSupplier implements IntegerSupplier {
    private final int value;

    public SimpleIntegerSupplier(int value) {
        this.value = value;
    }

    @Override
    public int getInteger() {
        return value;
    }
}
