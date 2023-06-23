package com.jverbruggen.jrides.models.math;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class SpeedUtilTest {
    @Test
    @DisplayName("Position start braking")
    public void positionStartBrakingTest(){
        assertEquals(95, SpeedUtil.positionStartBraking(1, -.1f, 100, 0));
        assertEquals(480, SpeedUtil.positionStartBraking(2, -.1f, 500, 0));
    }
}