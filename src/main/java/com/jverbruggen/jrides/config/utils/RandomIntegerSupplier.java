package com.jverbruggen.jrides.config.utils;

public class RandomIntegerSupplier implements IntegerSupplier {
    private final int lowerRange;
    private final int upperRange;

    public RandomIntegerSupplier(int lowerRange, int upperRange) {
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
    }

    @Override
    public int getInteger() {
        return (int)(Math.random() * (upperRange - lowerRange)) + lowerRange;
    }
}
