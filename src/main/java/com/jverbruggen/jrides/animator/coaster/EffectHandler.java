package com.jverbruggen.jrides.animator.coaster;

import com.jverbruggen.jrides.effect.EffectTriggerCollection;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.cart.CartEffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EffectHandler {
    private final Train train;
    private CoasterHandle coasterHandle;

    private boolean hasEffects;
    private TrainEffectTriggerHandle nextEffect;

    private boolean hasCartEffects;
    private final Map<CoasterCart, CartEffectTriggerHandle> nextCartEffects;

    public EffectHandler(Train train, Map<CoasterCart, CartEffectTriggerHandle> nextCartEffects) {
        this.train = train;
        this.coasterHandle = null;
        this.hasEffects = false;
        this.nextEffect = null;
        this.hasCartEffects = false;
        this.nextCartEffects = nextCartEffects;
    }

    public void setCoasterHandle(CoasterHandle coasterHandle){
        this.coasterHandle = coasterHandle;

        EffectTriggerCollection<TrainEffectTriggerHandle> trainEffectTriggerCollection = coasterHandle.getTrainEffectTriggerCollection();
        if(trainEffectTriggerCollection != null && trainEffectTriggerCollection.size() > 0){
            hasEffects = true;
        }

        EffectTriggerCollection<CartEffectTriggerHandle> cartEffectTriggerCollection = coasterHandle.getCartEffectTriggerCollection();
        if(cartEffectTriggerCollection != null && cartEffectTriggerCollection.size() > 0){
            hasCartEffects = true;
        }

        if(hasEffects || hasCartEffects)
            resetEffectsRandomPos();
    }

    private void resetEffectsRandomPos(){
        if(hasEffects){
            nextEffect = EffectTriggerHandle.FindNearestNextEffect(
                    TrainEffectTriggerHandle.class,
                    coasterHandle.getTrainEffectTriggerCollection().getLinkedList(),
                    train.getHeadOfTrainFrame());

//            Bukkit.broadcastMessage("Next train effect: " + nextEffect.getFrame());
        }

        if(hasCartEffects){
            LinkedList<CartEffectTriggerHandle> cartEffectLinkedList = coasterHandle.getCartEffectTriggerCollection().getLinkedList();

            for(CoasterCart cart : train.getCarts()){
                CartEffectTriggerHandle nextCartEffect = EffectTriggerHandle.FindNearestNextEffect(
                        CartEffectTriggerHandle.class,
                        cartEffectLinkedList,
                        cart.getFrame()
                );
                nextCartEffects.put(cart, nextCartEffect);

//                Bukkit.broadcastMessage("Next cart effect: " + nextCartEffect.getFrame());
            }
        }
    }

    public void resetEffects(){
        if(hasEffects){
            nextEffect = coasterHandle.getTrainEffectTriggerCollection().first();
        }

        if(hasCartEffects){
            LinkedList<CartEffectTriggerHandle> cartEffectLinkedList = coasterHandle.getCartEffectTriggerCollection().getLinkedList();

            for(CoasterCart cart : train.getCarts()){
                CartEffectTriggerHandle nextCartEffect = cartEffectLinkedList.get(0);
                nextCartEffects.put(cart, nextCartEffect);
            }
        }
    }

    private boolean shouldTrainEffect(TrainEffectTriggerHandle trainEffectTriggerHandle, Frame currentFrame){
        return trainEffectTriggerHandle.shouldPlay(currentFrame);
    }

    public void playTrainEffects(Frame currentFrame){
        while(nextEffect != null){
            if(!shouldTrainEffect(nextEffect, currentFrame)) break;
            boolean processNext = nextEffect.executeForTrain(train);
            if(!processNext) break;

            nextEffect = nextEffect.next();
        }
    }

    private boolean shouldCartEffectPlay(CartEffectTriggerHandle cartEffectTriggerHandle, Frame currentFrame){
        return cartEffectTriggerHandle.shouldPlay(currentFrame);
    }

    public void playCartEffects(CoasterCart cart){
        Frame currentCartFrame = cart.getFrame();
        CartEffectTriggerHandle nextCartEffect = nextCartEffects.get(cart);
        while(nextCartEffect != null){
            if(!shouldCartEffectPlay(nextCartEffect, currentCartFrame)) break;
            boolean processNext = nextCartEffect.executeForCart(cart);
            if(!processNext) break;

            CartEffectTriggerHandle newNextEffect = nextCartEffect.next();
            nextCartEffects.put(cart, newNextEffect);
            nextCartEffect = newNextEffect;
        }
    }

    public static EffectHandler createForTrain(Train train){
        Map<CoasterCart, CartEffectTriggerHandle> nextCartEffects = new HashMap<>();

        for(CoasterCart cart : train.getCarts()){
            nextCartEffects.put(cart, null);
        }

        return new EffectHandler(train, nextCartEffects);
    }

    public boolean hasCartEffects() {
        return hasCartEffects;
    }
}
