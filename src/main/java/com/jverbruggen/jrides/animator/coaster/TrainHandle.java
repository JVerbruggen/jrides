/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.animator.coaster;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.properties.TrainEnd;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.provider.SectionProvider;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.SoundCategory;

import java.util.Map;
import java.util.Set;

public class TrainHandle {
    private final Train train;
    private final Track track;
    private final Speed speedBPS;
    private TrackBehaviour trackBehaviour;
    private final SectionProvider sectionProvider;
    private final CartMovementFactory cartMovementFactory;
    private CoasterHandle coasterHandle;

    private final EffectHandler effectHandler;

    private final int windSoundInterval;
    private int windSoundState;

    public TrainHandle(SectionProvider sectionProvider, Train train, Track track) {
        this.sectionProvider = sectionProvider;
        this.cartMovementFactory = ServiceProvider.getSingleton(CartMovementFactory.class);
        this.train = train;
        this.track = track;
        this.trackBehaviour = train.getHeadSection().getTrackBehaviour();
        this.speedBPS = new Speed(0);
        this.coasterHandle = null;
        this.effectHandler = EffectHandler.createForTrain(train);

        this.train.setHandle(this);

        this.windSoundInterval = 4;
        this.windSoundState = 0;
    }

    public void tick(){
        if(train.isCrashed()) return;

        // --- Calculate movement that should be applied to the train
        TrainMovement result = trackBehaviour.move(speedBPS, this, train.getHeadSection());
        if(result == null) return;

        // --- Apply movement: new head frame and speed
        speedBPS.setSpeed(result.getNewSpeed().getSpeedPerTick());

        // --- Move carts according to instructions
        Set<Map.Entry<CoasterCart, CartMovement>> cartMovements = cartMovementFactory.createOnTrackCartMovement(train.getHandle(), train.getCarts(), result.getNewSpeed().getFrameIncrement(), null).entrySet();
        if(cartMovements != null){
            for(Map.Entry<CoasterCart, CartMovement> cartMovement : cartMovements){
                CoasterCart cart = cartMovement.getKey();
                CartMovement movement = cartMovement.getValue();
                cart.setPosition(movement);
                cart.playEffects();
            }
        }

        Frame trainHeadOfTrainFrame = train.getHeadOfTrainFrame();
        Frame trainMiddleOfTrainFrame = train.getMiddleOfTrainFrame();
        Frame trainTailOfTrainFrame = train.getTailOfTrainFrame();

        boolean headAppliesBehaviour = speedBPS.isGoingForwards();
        boolean tailAppliesBehaviour = !headAppliesBehaviour;

        // Both Head and Tail update section occupations: if going forwards: head locks next and tail unlocks previous
        sectionProvider.addFramesWithSectionLogic(this, trainHeadOfTrainFrame, result.getNewHeadOfTrainFrame().getValue(),
                true, TrainEnd.HEAD, "HEAD", headAppliesBehaviour);
        sectionProvider.addFramesWithSectionLogic(this, trainMiddleOfTrainFrame, result.getNewMiddleOfTrainFrame().getValue());
        sectionProvider.addFramesWithSectionLogic(this, trainTailOfTrainFrame, result.getNewTailOfTrainFrame().getValue(),
                true, TrainEnd.TAIL, "TAIL", tailAppliesBehaviour);

        JRidesPlugin.getLogger().info(LogType.SECTIONS_DETAIL, trainHeadOfTrainFrame + " -= "
                + trainMiddleOfTrainFrame + " =- " + trainTailOfTrainFrame);

        // --- Set new train location according to new frames
        Vector3 headLocation = track.getLocationFor(trainHeadOfTrainFrame);
        Vector3 middleLocation = track.getLocationFor(trainMiddleOfTrainFrame);
        Vector3 tailLocation = track.getLocationFor(trainTailOfTrainFrame);
        train.setCurrentLocation(headLocation, middleLocation, tailLocation);

        sendPositionUpdatesToListeners("HEAD", trainHeadOfTrainFrame);
        sendPositionUpdatesToListeners("MIDD", trainMiddleOfTrainFrame);
        sendPositionUpdatesToListeners("TAIL", trainTailOfTrainFrame);
        train.sendPositionMessage("SPEED: " + speedBPS);

        effectHandler.playTrainEffects(trainHeadOfTrainFrame);
        if(effectHandler.hasCartEffects()){
            for(CoasterCart cart : train.getCarts()){
                effectHandler.playCartEffects(cart);
            }
        }
        playWindSounds();
    }

    private void sendPositionUpdatesToListeners(String identifier, Frame currentFrame) {
        train.sendPositionMessage(identifier + ": " + currentFrame.toString());
    }


    private void playWindSounds(){
        String sound = coasterHandle.getSounds().getOnrideWind();
        if(sound == null) return;
        if(windSoundState < windSoundInterval)
            windSoundState++;
        else{
            for(Player player : train.getPassengers()){
                float pitch = (float)Math.abs(speedBPS.getSpeedPerTick() / 10) + 0.4f;
                float volume = Math.abs((pitch - 0.3f) / 5) - 0.05f;
                player.getBukkitPlayer().playSound(player.getBukkitPlayer().getLocation(),
                        sound, SoundCategory.MASTER, volume, pitch);
            }
            windSoundState = 0;
        }
    }

    public Train getTrain() {
        return train;
    }

    public void setCoasterHandle(CoasterHandle coasterHandle) {
        this.coasterHandle = coasterHandle;

        this.effectHandler.setCoasterHandle(coasterHandle);
    }

    public CoasterHandle getCoasterHandle() {
        return coasterHandle;
    }

    public Speed getSpeed() {
        return speedBPS;
    }

    public void setTrackBehaviour(TrackBehaviour trackBehaviour) {
        this.trackBehaviour = trackBehaviour;
    }

    public void resetEffects() {
        effectHandler.resetEffects();
    }
}
