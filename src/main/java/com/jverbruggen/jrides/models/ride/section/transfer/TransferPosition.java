package com.jverbruggen.jrides.models.ride.section.transfer;

import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;

public class TransferPosition {
    private Frame connectionAtStart;
    private Frame connectionAtEnd;

    private Vector3 transferPosition;
    private Quaternion transferRotation;


    public boolean canExitAtStart(){
        return connectionAtStart != null;
    }

    public boolean canExitAtEnd(){
        return connectionAtEnd != null;
    }

    public Quaternion getTransferRotation() {
        return transferRotation;
    }

    public Vector3 getTransferPosition() {
        return transferPosition;
    }

}
