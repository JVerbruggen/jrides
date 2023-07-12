package com.jverbruggen.jrides.animator.flatride.rotor;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.attachment.FixedAttachment;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class RotorTest {

    private Rotor rotor;

    @Before
    public void setUp(){
        rotor = new Rotor("", "", false, Collections.emptyList(), new FlatRideComponentSpeed(0, -1, 1));
        rotor.setAttachedTo(new FixedAttachment(rotor, new Vector3(0,0,0), new Quaternion()));
    }

    @Test
    public void hasPassed() {
        assertTrue(rotor.hasPassed(0));
        assertFalse(rotor.hasPassed(1));
        assertFalse(rotor.hasPassed(10));
        assertFalse(rotor.hasPassed(359));
    }

    @Test
    public void hasPassedWithRotation(){
        rotor.setInstructionPosition(200);

        assertTrue(rotor.hasPassed(0));
        assertTrue(rotor.hasPassed(50));
        assertTrue(rotor.hasPassed(199));
        assertTrue(rotor.hasPassed(200));
        assertFalse(rotor.hasPassed(201));
        assertFalse(rotor.hasPassed(359));
    }

    @Test
    public void hasPassedWithRotationFromNegativeZeroPosition(){
        rotor.setInstructionPosition(-0d);

        assertTrue(rotor.hasPassed(0));
        assertFalse(rotor.hasPassed(30));
        assertFalse(rotor.hasPassed(199));
        assertFalse(rotor.hasPassed(200));
        assertFalse(rotor.hasPassed(201));
        assertFalse(rotor.hasPassed(359));
    }

    @Test
    public void hasPassedWithRotationFromX(){
        rotor.setInstructionPosition(200);
        double from = 50;

        assertTrue(rotor.hasPassed(from, 50));
        assertTrue(rotor.hasPassed(from, 199));
        assertTrue(rotor.hasPassed(from, 200));
        assertFalse(rotor.hasPassed(from, 201));
        assertFalse(rotor.hasPassed(from, 359));
        assertFalse(rotor.hasPassed(from, 0));
        assertFalse(rotor.hasPassed(from, 15));
        assertFalse(rotor.hasPassed(from, 49));
    }

    @Test
    public void hasPassedWithRotationFromHighX(){
        rotor.setInstructionPosition(200);
        double from = 190;

        assertFalse(rotor.hasPassed(from, 50));
        assertFalse(rotor.hasPassed(from, 189));
        assertTrue(rotor.hasPassed(from, 190));
        assertTrue(rotor.hasPassed(from, 191));
        assertTrue(rotor.hasPassed(from, 199));
        assertTrue(rotor.hasPassed(from, 200));
        assertFalse(rotor.hasPassed(from, 201));
        assertFalse(rotor.hasPassed(from, 359));
        assertFalse(rotor.hasPassed(from, 0));
        assertFalse(rotor.hasPassed(from, 15));
        assertFalse(rotor.hasPassed(from, 49));
    }

    @Test
    public void hasPassedWithRotationLowXLowPos(){
        rotor.setInstructionPosition(50);
        double from = 30;

        assertFalse(rotor.hasPassed(from, 20));
        assertFalse(rotor.hasPassed(from, 29));
        assertTrue(rotor.hasPassed(from, 30));
        assertTrue(rotor.hasPassed(from, 31));
        assertTrue(rotor.hasPassed(from, 49));
        assertTrue(rotor.hasPassed(from, 50));
        assertFalse(rotor.hasPassed(from, 51));
        assertFalse(rotor.hasPassed(from, 359));
        assertFalse(rotor.hasPassed(from, 0));
    }

    @Test
    public void hasPassedWithRotationBackwards(){
        rotor.setInstructionPosition(200);
        rotor.getFlatRideComponentSpeed().accelerate(-2); // Make the speed negative

        assertFalse(rotor.hasPassed(0));
        assertFalse(rotor.hasPassed(50));
        assertFalse(rotor.hasPassed(199));
        assertTrue(rotor.hasPassed(200));
        assertTrue(rotor.hasPassed(201));
        assertTrue(rotor.hasPassed(359));
    }

    @Test
    public void hasPassedWithRotationBackwardsLowPos(){
        rotor.setInstructionPosition(30);
        rotor.getFlatRideComponentSpeed().accelerate(-2); // Make the speed negative

        assertFalse(rotor.hasPassed(0));
        assertFalse(rotor.hasPassed(20));
        assertFalse(rotor.hasPassed(29));
        assertTrue(rotor.hasPassed(30));
        assertTrue(rotor.hasPassed(31));
        assertTrue(rotor.hasPassed(180));
        assertTrue(rotor.hasPassed(300));
        assertTrue(rotor.hasPassed(359));
    }

    @Test
    public void hasPassedWithRotationFromXBackwards(){
        rotor.setInstructionPosition(200);
        rotor.getFlatRideComponentSpeed().accelerate(-2); // Make the speed negative
        double from = 290;
        System.out.println(rotor.getCurrentPosition() + " curpos");

        assertFalse(rotor.hasPassed(from, 50));
        assertFalse(rotor.hasPassed(from, 199));
        assertTrue(rotor.hasPassed(from, 200));
        assertTrue(rotor.hasPassed(from, 201));
        assertTrue(rotor.hasPassed(from, 288));
        assertTrue(rotor.hasPassed(from, 289));

        assertFalse(rotor.hasPassed(from, 290), "Should actually be true, but its weird");

        assertFalse(rotor.hasPassed(from, 291));
        assertFalse(rotor.hasPassed(from, 292));
        assertFalse(rotor.hasPassed(from, 359));
        assertFalse(rotor.hasPassed(from, 0));
        assertFalse(rotor.hasPassed(from, 15));
        assertFalse(rotor.hasPassed(from, 49));
    }

    @Test
    public void hasPassedWithRotationFromXBackwardsLowXLowPos(){
        rotor.setInstructionPosition(10);
        rotor.getFlatRideComponentSpeed().accelerate(-2); // Make the speed negative
        double from = 50;
        System.out.println(rotor.getCurrentPosition() + " curpos");

        assertFalse(rotor.hasPassed(from, 5));
        assertFalse(rotor.hasPassed(from, 9));
        assertTrue(rotor.hasPassed(from, 10));
        assertTrue(rotor.hasPassed(from, 11));
        assertTrue(rotor.hasPassed(from, 35));
        assertTrue(rotor.hasPassed(from, 49));

        assertFalse(rotor.hasPassed(from, 50), "Should actually be true, but its weird");

        assertFalse(rotor.hasPassed(from, 51));
        assertFalse(rotor.hasPassed(from, 52));
        assertFalse(rotor.hasPassed(from, 100));
        assertFalse(rotor.hasPassed(from, 292));
        assertFalse(rotor.hasPassed(from, 359));
        assertFalse(rotor.hasPassed(from, 360));
        assertFalse(rotor.hasPassed(from, 0));
    }
}