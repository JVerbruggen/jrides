package com.jverbruggen.jrides.config.utils.integersupplier;

import java.util.function.Supplier;

public class RandomIntegerSupplier implements Supplier<Integer> {
    private final int lowerRange;
    private final int upperRange;

    public RandomIntegerSupplier(int lowerRange, int upperRange) {
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
    }

    @Override
    public Integer get() {
        return (int)(Math.random() * (upperRange - lowerRange)) + lowerRange;
    }
}
