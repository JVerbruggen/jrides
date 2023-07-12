package com.jverbruggen.jrides.models.math;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MathUtilTest {

    @Test
    public void floorMod() {
        assertEquals(0f, MathUtil.floorMod(0f, 360f));
        assertEquals(0f, MathUtil.floorMod(360f, 360f));
        assertEquals(180f, MathUtil.floorMod(180f, 360f));
        assertEquals(260f, MathUtil.floorMod(-100f, 360f));
    }
}