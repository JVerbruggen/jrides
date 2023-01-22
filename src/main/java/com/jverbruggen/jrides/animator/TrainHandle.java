package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.Cart;
import com.jverbruggen.jrides.models.ride.coaster.Track;
import com.jverbruggen.jrides.models.ride.coaster.Train;

public class TrainHandle {
    private Train train;
    private int currentAnimationFrame;
    private int frameCount;
    private Track track;

    public TrainHandle(Train train, Track track) {
        this.train = train;
        this.currentAnimationFrame = 0;
        this.track = track;
        this.frameCount = track.getRawPositions().size();
    }

    public void tick(){
        int frame = currentAnimationFrame;
        currentAnimationFrame = (currentAnimationFrame + 1) % frameCount;

        NoLimitsExportPositionRecord position = track.getRawPositions().get(frame);
        Vector3 location = position.toVector3();

        for(Cart cart : train.getCarts()){
            cart.setPosition(location);
        }
    }

    public void setPosition(){
    }
}
