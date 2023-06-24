package com.jverbruggen.jrides.config.utils;

import java.util.function.Supplier;

public interface IntegerSupplier extends Supplier<Integer> {
    int getInteger();

    @Override
    default Integer get() {
        return getInteger();
    }
}
