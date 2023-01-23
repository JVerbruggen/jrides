package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.Cart;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import org.bukkit.Bukkit;

public class TrainHandle {
    private Train train;
    private Vector3 currentLocation;
    private int currentAnimationFrame;
    private int frameCount;
    private Track track;
    private double speedBPS;

    public TrainHandle(Train train, Track track, int stationIndexOffset) {
        this.train = train;
        this.currentAnimationFrame = stationIndexOffset;
        this.track = track;
        this.frameCount = track.getRawPositions().size();
        this.currentLocation = track.getRawPositions().get(currentAnimationFrame).toVector3();
    }

    public void tick(){
        final double frameIncrementFactor = 3;
        final double dragFactorPerTick = 0.999;
        final double gravityAccelerationPerTick = 0.3;

        int frame = currentAnimationFrame;
        int frameIncrement = (int) (speedBPS * frameIncrementFactor);
        currentAnimationFrame = (currentAnimationFrame + frameIncrement) % frameCount;

        NoLimitsExportPositionRecord position = track.getRawPositions().get(frame);
        Vector3 location = position.toVector3();

        double dy = currentLocation.getY() - location.getY(); // negative if going up
        speedBPS += dy*gravityAccelerationPerTick;
        speedBPS *= dragFactorPerTick;
        if(speedBPS < 1) speedBPS = 1;

        for(Cart cart : train.getCarts()){
            cart.setPosition(location);
        }

        currentLocation = location;
    }

    public void setPosition(){
    }
}
