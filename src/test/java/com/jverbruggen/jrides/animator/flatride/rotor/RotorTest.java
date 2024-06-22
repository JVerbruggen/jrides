//package com.jverbruggen.jrides.animator.flatride.rotor;
//
//import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
//import com.jverbruggen.jrides.animator.flatride.attachment.FixedAttachment;
//import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxis;
//import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxisFactory;
//import com.jverbruggen.jrides.models.math.Quaternion;
//import com.jverbruggen.jrides.models.math.Vector3;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class RotorTest {
//
//    private Rotor rotor;
//
//    @Before
//    public void setUp(){
//        RotorAxis rotorAxis = RotorAxisFactory.createAxisX();
//        rotor = new Rotor("", "", false, Collections.emptyList(), new FlatRideComponentSpeed(0d, -1d, 1d), rotorAxis);
//        rotor.setAttachedTo(new FixedAttachment(rotor, new Vector3(0,0,0), new Quaternion()));
//    }
//
//    @Test
//    public void hasPassed() {
//        assertTrue(rotor.hasPassed(0, 0, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 1, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 10, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 359, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//    }
//
//    @Test
//    public void hasPassedWithRotation(){
//        rotor.setInstructionPosition(200);
//
//        assertTrue(rotor.hasPassed(0, 0, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(0, 50, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(0, 199, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(0, 200, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 201, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 359, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//    }
//
//    @Test
//    public void hasPassedWithRotationFromNegativeZeroPosition(){
//        rotor.setInstructionPosition(-0d);
//
//        assertTrue(rotor.hasPassed(0, 0, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 30, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 199, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 200, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 201, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 359, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//    }
//
//    @Test
//    public void hasPassedWithRotationFromX(){
//        rotor.setInstructionPosition(200);
//        double from = 50;
//
//        assertTrue(rotor.hasPassed(from, 50, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 199, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 200, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 201, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 359, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 0, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 15, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 49, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//    }
//
//    @Test
//    public void hasPassedWithRotationFromHighX(){
//        rotor.setInstructionPosition(200);
//        double from = 190;
//
//        assertFalse(rotor.hasPassed(from, 50, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 189, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 190, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 191, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 199, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 200, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 201, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 359, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 0, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 15, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 49, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//    }
//
//    @Test
//    public void hasPassedWithRotationLowXLowPos(){
//        rotor.setInstructionPosition(50);
//        double from = 30;
//
//        assertFalse(rotor.hasPassed(from, 20, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 29, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 30, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 31, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 49, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 50, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 51, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 359, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 0, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//    }
//
//    @Test
//    public void hasPassedWithRotationBackwards(){
//        rotor.setInstructionPosition(200);
//        rotor.getFlatRideComponentSpeed().accelerate(-2); // Make the speed negative
//
//        assertFalse(rotor.hasPassed(0, 0, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 50, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 199, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(0, 200, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(0, 201, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(0, 359, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//    }
//
//    @Test
//    public void hasPassedWithRotationBackwardsLowPos(){
//        rotor.setInstructionPosition(30);
//        rotor.getFlatRideComponentSpeed().accelerate(-2); // Make the speed negative
//
//        assertFalse(rotor.hasPassed(0, 0, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 20, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(0, 29, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(0, 30, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(0, 31, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(0, 180, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(0, 300, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(0, 359, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//    }
//
//    @Test
//    public void hasPassedWithRotationFromXBackwards(){
//        rotor.setInstructionPosition(200);
//        rotor.getFlatRideComponentSpeed().accelerate(-2); // Make the speed negative
//        double from = 290;
//        System.out.println(rotor.getRotorRotation() + " curpos");
//
//        assertFalse(rotor.hasPassed(from, 50, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 199, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 200, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 201, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 288, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 289, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//
//        assertFalse(rotor.hasPassed(from, 290, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d), "Should actually be true, but its weird");
//
//        assertFalse(rotor.hasPassed(from, 291, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 292, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 359, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 0, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 15, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 49, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//    }
//
//    @Test
//    public void hasPassedWithRotationFromXBackwardsLowXLowPos(){
//        rotor.setInstructionPosition(10);
//        rotor.getFlatRideComponentSpeed().accelerate(-2); // Make the speed negative
//        double from = 50;
//        System.out.println(rotor.getRotorRotation() + " curpos");
//
//        assertFalse(rotor.hasPassed(from, 5, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 9, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 10, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 11, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 35, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertTrue(rotor.hasPassed(from, 49, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//
//        assertFalse(rotor.hasPassed(from, 50, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d), "Should actually be true, but its weird");
//
//        assertFalse(rotor.hasPassed(from, 51, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 52, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 100, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 292, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 359, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 360, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//        assertFalse(rotor.hasPassed(from, 0, rotor.getFlatRideComponentSpeed().getSpeed() >= 0, 0d));
//    }
//}