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

package com.jverbruggen.jrides.effect;

import com.jverbruggen.jrides.common.Tuple;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.trigger.TriggerConfig;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.cart.CartEffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.train.TrainEffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.factory.FrameFactory;
import com.jverbruggen.jrides.models.ride.RideType;
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class EffectTriggerFactory {
    private final ConfigManager configManager;
    private final Map<String, EffectTrigger> effectTriggerMap;
    private final FrameFactory frameFactory;

    public EffectTriggerFactory() {
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
        this.effectTriggerMap = new HashMap<>();
        this.frameFactory = ServiceProvider.getSingleton(FrameFactory.class);
    }

    private <T extends EffectTriggerHandle> T getEffectTrigger(Class<T> clazz, String rideIdentifier, String effectName, boolean reversed, Frame frame, TriggerConfig triggerConfig){
        if(triggerConfig == null)
            throw new RuntimeException("Trigger config was null for effect '" + effectName + "' in ride '" + rideIdentifier + "'");

        EffectTrigger effectTrigger;
        String mapKey = rideIdentifier + ":" + effectName;
        if(!effectTriggerMap.containsKey(mapKey)){
            effectTrigger = triggerConfig.createTrigger();

            effectTriggerMap.put(mapKey, effectTrigger);
        }else{
            effectTrigger = effectTriggerMap.get(mapKey);
        }

        EffectTriggerHandle effectTriggerHandle = effectTrigger.createHandle(frame, reversed);

        if(!clazz.isInstance(effectTriggerHandle))
            return null;

        return clazz.cast(effectTriggerHandle);
    }

    public TrainEffectTriggerHandle getTrainEffectTrigger(String rideIdentifier, String effectName, boolean reversed, Frame frame, TriggerConfig triggerConfig){
        return getEffectTrigger(TrainEffectTriggerHandle.class, rideIdentifier, effectName, reversed, frame, triggerConfig);
    }

    public CartEffectTriggerHandle getCartEffectTrigger(String rideIdentifier, String effectName, boolean reversed, Frame frame, TriggerConfig triggerConfig){
        return getEffectTrigger(CartEffectTriggerHandle.class, rideIdentifier, effectName, reversed, frame, triggerConfig);
    }

    public List<TrainEffectTriggerHandle> getFramelessEffectTriggers(RideType rideType, String rideIdentifier, List<String> effectNames){
        if(effectNames == null) return null;
        return effectNames
                .stream()
                .map(effectNameRaw -> {
                    String[] effectNameRawComponents = effectNameRaw.split(":");
                    String effectName = effectNameRawComponents[0];
                    boolean reversed = false;
                    if(effectNameRawComponents.length == 2){
                        reversed = effectNameRawComponents[1].equalsIgnoreCase("reversed");
                    }
                    return getTrainEffectTrigger(rideIdentifier, effectName, reversed, null, configManager.getTriggerConfig(rideType, rideIdentifier, effectName));
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private <T extends EffectTriggerHandle> EffectTriggerCollection<T> getEffectTriggerCollectionGeneric(Class<T> clazz, String rideIdentifier, Track track, BiConsumer<T, T> setNextFunction){
        ConfigurationSection allEffectsConfigurationSection = configManager.getAllEffectsConfigSection(rideIdentifier, "default");
        if(allEffectsConfigurationSection == null)
            return null;

        boolean goneRound = false;
        int lastFrameIndex = Integer.MIN_VALUE;
        List<Tuple<Frame, String>> effects = new ArrayList<>();
        Set<String> effectIdentifiers = allEffectsConfigurationSection.getKeys(false);
        for(String effectIdentifier : effectIdentifiers){
            ConfigurationSection effectConfigurationSection = allEffectsConfigurationSection.getConfigurationSection(effectIdentifier);
            int frameIndex = effectConfigurationSection.getInt("frame");
            if(lastFrameIndex > frameIndex){
                if(goneRound)
                    throw new RuntimeException("Every effect trigger frame needs to be after the frame of the effect before (except for once going round)");

                goneRound = true;
            }

            effects.add(new Tuple<>(frameFactory.getStaticFrame(frameIndex, track), effectIdentifier));

            lastFrameIndex = frameIndex;
        }

        T previous = null;
        LinkedList<T> effectTriggers = new LinkedList<>();
        for(Tuple<Frame, String> effect : effects){
            Frame frame = effect.getA();
            String effectName = effect.getB();

            T effectTrigger = getEffectTrigger(clazz, rideIdentifier, effectName, false, frame, configManager.getTriggerConfig(RideType.COASTER, rideIdentifier, effectName));
            if(effectTrigger == null) continue;
            if(previous != null) setNextFunction.accept(previous, effectTrigger);
            effectTriggers.add(effectTrigger);

            previous = effectTrigger;
        }

        return new EffectTriggerCollection<>(effectTriggers);
    }

    public EffectTriggerCollection<TrainEffectTriggerHandle> getTrainEffectTriggers(String rideIdentifier, Track track){
        return getEffectTriggerCollectionGeneric(TrainEffectTriggerHandle.class, rideIdentifier, track, TrainEffectTriggerHandle::setNext);
    }

    public EffectTriggerCollection<CartEffectTriggerHandle> getCartEffectTriggers(String rideIdentifier, Track track){
        return getEffectTriggerCollectionGeneric(CartEffectTriggerHandle.class, rideIdentifier, track, CartEffectTriggerHandle::setNext);
    }
}
