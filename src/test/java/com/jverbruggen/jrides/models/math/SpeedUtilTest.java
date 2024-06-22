package com.jverbruggen.jrides.models.math;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class SpeedUtilTest {
    @Test
    @DisplayName("Position start braking")
    public void positionStartBrakingTest(){
        assertEquals(95d, SpeedUtil.positionStartBraking(1d, -.1d, 100d, 0d));
        assertEquals(480d, SpeedUtil.positionStartBraking(2d, -.1d, 500d, 0d));
    }

    @Test
    public void testRangeNormal(){
        assertFalse(SpeedUtil.inRange(90, 89, 120));
        assertTrue(SpeedUtil.inRange(90, 90, 120));
        assertTrue(SpeedUtil.inRange(90, 100, 120));
        assertTrue(SpeedUtil.inRange(90, 119, 120));
        assertTrue(SpeedUtil.inRange(90, 120, 120));
        assertFalse(SpeedUtil.inRange(90, 121, 120));
    }

    @Test
    public void testRangeRotated(){
        assertFalse(SpeedUtil.inRange(120, 119, 90));
        assertTrue(SpeedUtil.inRange(120, 120, 90));
        assertTrue(SpeedUtil.inRange(120, 121, 90));
        assertTrue(SpeedUtil.inRange(120, 360, 90));
        assertTrue(SpeedUtil.inRange(120, 0, 90));
        assertTrue(SpeedUtil.inRange(120, 89, 90));
        assertTrue(SpeedUtil.inRange(120, 90, 90));
        assertFalse(SpeedUtil.inRange(120, 91, 90));
        assertFalse(SpeedUtil.inRange(120, 100, 90));
    }

    @Test
    public void hasPassedForwardsNormal(){
        assertTrue(SpeedUtil.hasPassed(10, 9, 30, true));
        assertFalse(SpeedUtil.hasPassed(10, 10, 30, true));
        assertFalse(SpeedUtil.hasPassed(10, 11, 30, true));
        assertFalse(SpeedUtil.hasPassed(10, 20, 30, true));
        assertFalse(SpeedUtil.hasPassed(10, 29, 30, true));
        assertTrue(SpeedUtil.hasPassed(10, 30, 30, true));
        assertTrue(SpeedUtil.hasPassed(10, 31, 30, true));
    }

    @Test
    public void hasPassedForwardsRotate(){
        assertTrue(SpeedUtil.hasPassed(270, 250, 45, true));
        assertFalse(SpeedUtil.hasPassed(270, 270, 45, true));
        assertFalse(SpeedUtil.hasPassed(270, 271, 45, true));
        assertFalse(SpeedUtil.hasPassed(270, 360, 45, true));
        assertFalse(SpeedUtil.hasPassed(270, 0, 45, true));
        assertFalse(SpeedUtil.hasPassed(270, 44, 45, true));
        assertTrue(SpeedUtil.hasPassed(270, 45, 45, true));
        assertTrue(SpeedUtil.hasPassed(270, 46, 45, true));
        assertTrue(SpeedUtil.hasPassed(270, 50, 45, true));
    }

    @Test
    public void hasPassedBackwards(){
        assertTrue(SpeedUtil.hasPassed(30, 270, 10, false));
        assertTrue(SpeedUtil.hasPassed(30, 180, 10, false));
        assertTrue(SpeedUtil.hasPassed(30, 31, 10, false));
        assertFalse(SpeedUtil.hasPassed(30, 30, 10, false));
        assertFalse(SpeedUtil.hasPassed(30, 29, 10, false));
        assertFalse(SpeedUtil.hasPassed(30, 20, 10, false));
        assertFalse(SpeedUtil.hasPassed(30, 11, 10, false));
        assertTrue(SpeedUtil.hasPassed(30, 10, 10, false));
        assertTrue(SpeedUtil.hasPassed(30, 9, 10, false));
    }

    @Test
    public void hasPassedBackwardsRotate(){
        assertTrue(SpeedUtil.hasPassed(100, 120, 150, false));
        assertTrue(SpeedUtil.hasPassed(100, 101, 150, false));
        assertFalse(SpeedUtil.hasPassed(100, 100, 150, false));
        assertFalse(SpeedUtil.hasPassed(100, 99, 150, false));
        assertFalse(SpeedUtil.hasPassed(100, 50, 150, false));
        assertFalse(SpeedUtil.hasPassed(100, 0, 150, false));
        assertFalse(SpeedUtil.hasPassed(100, 360, 150, false));
        assertFalse(SpeedUtil.hasPassed(100, 270, 150, false));
        assertFalse(SpeedUtil.hasPassed(100, 151, 150, false));
        assertTrue(SpeedUtil.hasPassed(100, 150, 150, false));
        assertTrue(SpeedUtil.hasPassed(100, 149, 150, false));
    }

    @Test
    public void aboveInRange(){
        assertTrue(SpeedUtil.aboveInRange(0, 30, 50, 80, true));
        assertTrue(SpeedUtil.aboveInRange(0, 49, 50, 80, true));
        assertTrue(SpeedUtil.aboveInRange(0, 50, 50, 80, true));
        assertFalse(SpeedUtil.aboveInRange(0, 51, 50, 80, true));
        assertFalse(SpeedUtil.aboveInRange(0, 80, 50, 80, true));
    }

    @Test
    public void aboveInRangeRotate(){
        assertTrue(SpeedUtil.aboveInRange(320, 322, 20, 40, true));
        assertTrue(SpeedUtil.aboveInRange(320, 360, 20, 40, true));
        assertTrue(SpeedUtil.aboveInRange(320, 0, 20, 40, true));
        assertTrue(SpeedUtil.aboveInRange(320, 19, 20, 40, true));
        assertTrue(SpeedUtil.aboveInRange(320, 20, 20, 40, true));
        assertFalse(SpeedUtil.aboveInRange(320, 21, 20, 40, true));
        assertFalse(SpeedUtil.aboveInRange(320, 22, 20, 40, true));
    }
}