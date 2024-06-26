//package com.jverbruggen.jrides.animator.flatride.rotor;
//
//import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxis;
//import com.jverbruggen.jrides.animator.flatride.rotor.axis.RotorAxisFactory;
//import org.junit.Test;
//import org.junit.Before;
//
//import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
//import com.jverbruggen.jrides.api.JRidesPlayer;
//import com.jverbruggen.jrides.api.JRidesPlayerMock;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.Collections;
//
//public class RotorPlayerControlTest {
//    private static double EPSILON = 0.0001;
//
//    private FlatRideComponentSpeed componentSpeed;
//    private Rotor rotor;
//    private RotorPlayerControl rotorPlayerControl;
//
//    @Before
//    public void before(){
//        componentSpeed = new FlatRideComponentSpeed(0d);
//        rotorPlayerControl = new RotorTargetPositionPlayerControl(0, 90, .5f);
//        RotorAxis rotorAxis = RotorAxisFactory.createAxisX();
//        rotor = new Rotor("", "", false, Collections.emptyList(), componentSpeed, rotorAxis);
//        rotorPlayerControl.setRotor(rotor);
//
//        JRidesPlayer player = new JRidesPlayerMock();
//        rotorPlayerControl.addControlling(player);
//    }
//
//    @Test
//    public void rotorStaysAtZeroWithNoSpeed(){
//        assertTrue(equals(rotor.getRotorRotation(), 0d));
//        rotorPlayerControl.apply();
//        assertTrue(equals(rotor.getRotorRotation(), 0d));
//
//        for(int i = 0; i < 500; i++){
//            rotorPlayerControl.apply();
//        }
//
//        assertTrue(equals(rotor.getRotorRotation(), 0d));
//    }
//
//    private boolean equals(double a, double b){
//        return Math.abs(a - b) < EPSILON;
//    }
//}
