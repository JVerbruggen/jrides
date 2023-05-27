package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.effect.EffectTriggerCollection;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.properties.TrainEnd;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.provider.SectionProvider;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.SoundCategory;

import java.util.Map;
import java.util.Set;

public class TrainHandle {
    private Train train;
    private Track track;
    private Speed speedBPS;
    private TrackBehaviour trackBehaviour;
    private final SectionProvider sectionProvider;
    private final CartMovementFactory cartMovementFactory;
    private CoasterHandle coasterHandle;

    private boolean hasEffects;
    private TrainEffectTriggerHandle nextEffect;

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

        this.nextEffect = null;
        this.hasEffects = false;

        this.train.setHandle(this);

        this.windSoundInterval = 4;
        this.windSoundState = 0;
    }

    public void resetEffects(){
        if(!hasEffects) return;

        nextEffect = (TrainEffectTriggerHandle) EffectTriggerHandle.FindNearestNextEffect(
                coasterHandle.getEffectTriggerCollection().getLinkedList(),
                h -> h instanceof TrainEffectTriggerHandle,
                train.getHeadOfTrainFrame());
    }

    public void tick(){
        if(train.isCrashed()) return;

        // --- Calculate movement that should be applied to the train
        TrainMovement result = trackBehaviour.move(speedBPS, this, train.getHeadSection());
        if(result == null) return;

        // --- Apply movement: new head frame and speed
        speedBPS.setSpeed(result.getNewSpeed().getSpeedPerTick());

        // --- Move carts according to instructions
        Set<Map.Entry<Cart, CartMovement>> cartMovements = cartMovementFactory.createOnTrackCartMovement(train.getHandle(), train.getCarts(), result.getNewSpeed().getFrameIncrement(), null).entrySet();
        if(cartMovements != null){
            for(Map.Entry<Cart, CartMovement> cartMovement : cartMovements){
                Cart cart = cartMovement.getKey();
                CartMovement movement = cartMovement.getValue();
                cart.setPosition(movement);
                cart.playEffects();
            }
        }

        Frame trainHeadOfTrainFrame = train.getHeadOfTrainFrame();
        Frame trainMiddleOfTrainFrame = train.getMiddleOfTrainFrame();
        Frame trainTailOfTrainFrame = train.getTailOfTrainFrame();

        sectionProvider.addFramesWithSectionLogic(this, trainHeadOfTrainFrame, result.getNewHeadOfTrainFrame().getValue(),
                true, TrainEnd.HEAD, "HEAD", true);
        sectionProvider.addFramesWithSectionLogic(this, trainMiddleOfTrainFrame, result.getNewMiddleOfTrainFrame().getValue());
        sectionProvider.addFramesWithSectionLogic(this, trainTailOfTrainFrame, result.getNewTailOfTrainFrame().getValue(),
                true, TrainEnd.TAIL, "TAIL", false);

        JRidesPlugin.getLogger().info(LogType.SECTIONS_DETAIL, trainHeadOfTrainFrame + " -= "
                + trainMiddleOfTrainFrame + " =- " + trainTailOfTrainFrame);

        // --- Set new train location according to new frames
        Vector3 headLocation = track.getLocationFor(trainHeadOfTrainFrame);
        Vector3 middleLocation = track.getLocationFor(trainMiddleOfTrainFrame);
        Vector3 tailLocation = track.getLocationFor(trainTailOfTrainFrame);
        train.setCurrentLocation(headLocation, middleLocation, tailLocation);

        sendPositionUpdatesToListeners(trainHeadOfTrainFrame);
        playEffects(trainHeadOfTrainFrame);
        playWindSounds();
    }

    private void sendPositionUpdatesToListeners(Frame currentFrame) {
        train.sendPositionMessage(currentFrame.toString());
    }

    private void playEffects(Frame currentFrame){
        while(nextEffect != null){
            if(!shouldPlay(nextEffect, currentFrame)) break;
            nextEffect.execute(train);
            nextEffect = nextEffect.next();
        }
    }

    private boolean shouldPlay(TrainEffectTriggerHandle trainEffectTriggerHandle, Frame currentFrame){
        return trainEffectTriggerHandle.shouldPlay(currentFrame);
    }

    private void playWindSounds(){
        String sound = coasterHandle.getWindSound();
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

        EffectTriggerCollection effectTriggerCollection = coasterHandle.getEffectTriggerCollection();
        if(effectTriggerCollection != null && effectTriggerCollection.size() > 0){
            hasEffects = true;
            resetEffects();
        }
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
}
