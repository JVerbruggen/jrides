package com.jverbruggen.jrides.effect;

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

    private <T extends EffectTriggerHandle> T getEffectTrigger(String rideIdentifier, String effectName, boolean reversed, Frame frame, TriggerConfig triggerConfig){
        if(triggerConfig == null) return null;

        EffectTrigger effectTrigger;
        String mapKey = rideIdentifier + ":" + effectName;
        if(!effectTriggerMap.containsKey(mapKey)){
            effectTrigger = triggerConfig.createTrigger();

            effectTriggerMap.put(mapKey, effectTrigger);
        }else{
            effectTrigger = effectTriggerMap.get(mapKey);
        }

        return (T) effectTrigger.createHandle(frame, reversed);
    }

    public TrainEffectTriggerHandle getTrainEffectTrigger(String rideIdentifier, String effectName, boolean reversed, Frame frame, TriggerConfig triggerConfig){
        return getEffectTrigger(rideIdentifier, effectName, reversed, frame, triggerConfig);
    }

    public CartEffectTriggerHandle getCartEffectTrigger(String rideIdentifier, String effectName, boolean reversed, Frame frame, TriggerConfig triggerConfig){
        return getEffectTrigger(rideIdentifier, effectName, reversed, frame, triggerConfig);
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
                .collect(Collectors.toList());
    }

    private <T extends EffectTriggerHandle> EffectTriggerCollection<T> getEffectTriggerCollectionGeneric(String rideIdentifier, Track track, BiConsumer<T, T> setNextFunction){
        ConfigurationSection allEffectsConfigurationSection = configManager.getAllEffectsConfigSection(rideIdentifier, "default");
        if(allEffectsConfigurationSection == null)
            return null;

        int lastFrameIndex = Integer.MIN_VALUE;
        Map<Frame, String> effects = new HashMap<>();
        Set<String> effectIdentifiers = allEffectsConfigurationSection.getKeys(false);
        for(String effectIdentifier : effectIdentifiers){
            ConfigurationSection effectConfigurationSection = allEffectsConfigurationSection.getConfigurationSection(effectIdentifier);
            int frameIndex = effectConfigurationSection.getInt("frame");
            if(lastFrameIndex > frameIndex) throw new RuntimeException("Every effect trigger frame needs to be after the frame of the effect before");

            effects.put(frameFactory.getStaticFrame(frameIndex, track), effectIdentifier);

            lastFrameIndex = frameIndex;
        }

        T previous = null;
        List<T> effectTriggers = new ArrayList<>();
        for(Map.Entry<Frame, String> effect : effects.entrySet()){
            Frame frame = effect.getKey();
            String effectName = effect.getValue();

            T effectTrigger = getEffectTrigger(rideIdentifier, effectName, false, frame, configManager.getTriggerConfig(RideType.COASTER, rideIdentifier, effectName));
            if(effectTrigger == null) return null;
            if(previous != null) setNextFunction.accept(previous, effectTrigger);
            effectTriggers.add(effectTrigger);

            previous = effectTrigger;
        }

        return new EffectTriggerCollection<T>(effectTriggers);
    }

    public EffectTriggerCollection<TrainEffectTriggerHandle> getTrainEffectTriggers(String rideIdentifier, Track track){
        return getEffectTriggerCollectionGeneric(rideIdentifier, track, TrainEffectTriggerHandle::setNext);
    }

    public EffectTriggerCollection<CartEffectTriggerHandle> getCartEffectTriggers(String rideIdentifier, Track track){
        return getEffectTriggerCollectionGeneric(rideIdentifier, track, CartEffectTriggerHandle::setNext);
    }
}
