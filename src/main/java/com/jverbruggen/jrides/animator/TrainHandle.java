package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovement;
import com.jverbruggen.jrides.animator.trackbehaviour.result.TrainMovement;
import com.jverbruggen.jrides.effect.EffectTriggerCollection;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.Speed;
import com.jverbruggen.jrides.models.properties.TrainEnd;
import com.jverbruggen.jrides.models.ride.coaster.train.Cart;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;
import com.jverbruggen.jrides.models.ride.section.provider.SectionProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.SoundCategory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class TrainHandle {
    private Train train;
    private Track track;
    private Speed speedBPS;
    private TrackBehaviour trackBehaviour;
    private final SectionProvider sectionProvider;
    private CoasterHandle coasterHandle;

    private boolean hasEffects;
    private EffectTriggerHandle nextEffect;

    private final int windSoundInterval;
    private int windSoundState;

    public TrainHandle(SectionProvider sectionProvider, Train train, Track track) {
        this.sectionProvider = sectionProvider;
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

        LinkedList<EffectTriggerHandle> linkedList = coasterHandle.getEffectTriggerCollection().getLinkedList();
        nextEffect = findNearestNextEffect(linkedList, train.getHeadOfTrainFrame()); // TODO: Check validity
        Bukkit.broadcastMessage(ChatColor.GOLD + "Loaded next effect: " + nextEffect);
    }

    private EffectTriggerHandle findNearestNextEffect(LinkedList<EffectTriggerHandle> linkedList, Frame currentFrame){
        if(linkedList == null || linkedList.size() == 0 || currentFrame == null)
            return null;
        else if(linkedList.size() == 1)
            return linkedList.getFirst();
        else{
            EffectTriggerHandle selectedEffect = linkedList.getFirst();
            if(shouldPlay(selectedEffect, currentFrame)){
                Iterator<EffectTriggerHandle> iterator = linkedList.iterator();
                while(iterator.hasNext()){
                    EffectTriggerHandle comparing = iterator.next();
                    if(!shouldPlay(comparing, currentFrame)){
                        selectedEffect = comparing;
                        break;
                    }
                }
            }else{
                Iterator<EffectTriggerHandle> iterator = linkedList.descendingIterator();
                while(iterator.hasNext()){
                    EffectTriggerHandle comparing = iterator.next();
                    if(shouldPlay(comparing, currentFrame)){
                        selectedEffect = comparing;
                    }else break;
                }
            }
            return selectedEffect;
        }
    }

    public void tick(){
        if(train.isCrashed()) return;

        // --- Calculate movement that should be applied to the train
        TrainMovement result = trackBehaviour.move(speedBPS, this, train.getHeadSection());
        if(result == null) return;

        // --- Apply movement: new head frame and speed
        speedBPS.setSpeed(result.getNewSpeed().getSpeedPerTick());

        Frame trainHeadOfTrainFrame = train.getHeadOfTrainFrame();
        Frame trainMiddleOfTrainFrame = train.getMiddleOfTrainFrame();
        Frame trainTailOfTrainFrame = train.getTailOfTrainFrame();

        sectionProvider.addFramesWithSectionLogic(this, trainHeadOfTrainFrame, result.getNewHeadOfTrainFrame().getValue(),
                true, TrainEnd.HEAD, "HEAD", true);
        sectionProvider.addFramesWithSectionLogic(this, trainMiddleOfTrainFrame, result.getNewMiddleOfTrainFrame().getValue());
        sectionProvider.addFramesWithSectionLogic(this, trainTailOfTrainFrame, result.getNewTailOfTrainFrame().getValue(),
                true, TrainEnd.TAIL, "TAIL", false);

        JRidesPlugin.getLogger().info(LogType.SECTIONS, trainHeadOfTrainFrame + " -= "
                + trainMiddleOfTrainFrame + " =- " + trainTailOfTrainFrame);

        // --- Set new train location according to new frames
        Vector3 headLocation = track.getLocationFor(trainHeadOfTrainFrame);
        Vector3 middleLocation = track.getLocationFor(trainMiddleOfTrainFrame);
        Vector3 tailLocation = track.getLocationFor(trainTailOfTrainFrame);
        train.setCurrentLocation(headLocation, middleLocation, tailLocation);

        // --- Move carts according to instructions
        Set<Map.Entry<Cart, CartMovement>> cartMovements = result.getCartMovements();
        if(cartMovements != null){
            for(Map.Entry<Cart, CartMovement> cartMovement : cartMovements){
                Cart cart = cartMovement.getKey();
                CartMovement movement = cartMovement.getValue();
                cart.setPosition(movement);
            }
        }

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

    private boolean shouldPlay(EffectTriggerHandle effectTriggerHandle, Frame currentFrame){
        Section headSection = train.getHeadSection();
        return nextEffect.getFrame().getSection().equals(headSection)
            && headSection.hasPassed(nextEffect.getFrame(), currentFrame);
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
