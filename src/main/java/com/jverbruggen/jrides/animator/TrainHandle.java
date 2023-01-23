package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.models.math.ArmorStandPose;
import com.jverbruggen.jrides.models.math.Matrix4x4;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.Cart;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class TrainHandle {
    private Train train;
    private Vector3 currentLocation;
    private int currentMassMiddleFrame;
    private int frameCount;
    private Track track;
    private double speedBPS;

    public TrainHandle(Train train, Track track, int stationIndexOffset) {
        this.train = train;
        this.currentMassMiddleFrame = stationIndexOffset;
        this.track = track;
        this.frameCount = track.getRawPositions().size();
        this.currentLocation = track.getRawPositions().get(currentMassMiddleFrame).toVector3();
    }

    public void tick(){
        final double frameIncrementFactor = 3;
        final double dragFactorPerTick = 0.999;
        final double gravityAccelerationPerTick = 0.3;

        int frame = currentMassMiddleFrame;
        int frameIncrement = (int) (speedBPS * frameIncrementFactor);
        currentMassMiddleFrame = Math.floorMod(currentMassMiddleFrame + frameIncrement, frameCount);

        NoLimitsExportPositionRecord position = track.getRawPositions().get(frame);
        Vector3 location = position.toVector3();

        double dy = currentLocation.getY() - location.getY(); // negative if going up
        speedBPS += dy*gravityAccelerationPerTick;
        speedBPS *= dragFactorPerTick;
        if(speedBPS < 1) speedBPS = 1;

        for(Cart cart : train.getCarts()){
            int cartFrame = Math.floorMod((frame + cart.getMassMiddleOffset()), frameCount);
            NoLimitsExportPositionRecord cartPositionTrack = track.getRawPositions().get(cartFrame);
            Quaternion orientation = cartPositionTrack.getOrientation();

            Matrix4x4 matrix = new Matrix4x4();
            matrix.rotate(orientation);
            matrix.translate(cart.getTrackOffset());
            Vector3 cartTrackOffsetVector = matrix.toVector3();
            Vector3 cartPositionVector = cartPositionTrack.toVector3();
            Vector3 totalVector = Vector3.add(cartPositionVector, cartTrackOffsetVector);

            final Vector3 armorstandHeightCompensationVector = new Vector3(0, -0.8, 0);
            totalVector = Vector3.add(totalVector, armorstandHeightCompensationVector);

            cart.setPosition(totalVector);
            cart.setRotation(ArmorStandPose.getArmorStandPose(orientation)); // TODO: expensive
        }

        currentLocation = location;
    }

    public void setPosition(){
    }
}
