package com.jverbruggen.jrides.effect;

import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.trigger.TriggerConfig;
import com.jverbruggen.jrides.effect.handle.DefaultEffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.effect.handle.ReversedEffectTriggerHandle;
import com.jverbruggen.jrides.effect.music.MusicEffectTriggerFactory;
import com.jverbruggen.jrides.effect.platform.MultiArmorstandMovementEffectTriggerFactory;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.stream.Collectors;

public class EffectTriggerFactory {
    private final MusicEffectTriggerFactory musicEffectTriggerFactory;
    private final MultiArmorstandMovementEffectTriggerFactory platformEffectTriggerFactory;
    private final ConfigManager configManager;
    private final Map<String, EffectTrigger> effectTriggerMap;

    public EffectTriggerFactory() {
        this.musicEffectTriggerFactory = ServiceProvider.getSingleton(MusicEffectTriggerFactory.class);
        this.platformEffectTriggerFactory = ServiceProvider.getSingleton(MultiArmorstandMovementEffectTriggerFactory.class);
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
        this.effectTriggerMap = new HashMap<>();
    }

    public EffectTriggerHandle getEffectTrigger(String effectName, boolean reversed, Frame frame, TriggerConfig triggerConfig){
        EffectTrigger effectTrigger;
        if(!effectTriggerMap.containsKey(effectName)){
            switch(triggerConfig.getType()){
                case MUSIC:
                    effectTrigger = musicEffectTriggerFactory.getMusicEffectTrigger(triggerConfig);
                    break;
                case MULTI_ARMORSTAND_MOVEMENT:
                    effectTrigger = platformEffectTriggerFactory.getMultiArmorstandMovementEffectTrigger(triggerConfig);
                    break;
                default:
                    throw new RuntimeException("Cannot resolve trigger type when creating effect trigger");
            }

            effectTriggerMap.put(effectName, effectTrigger);
        }else{
            effectTrigger = effectTriggerMap.get(effectName);
        }

        if(reversed) {
            return new ReversedEffectTriggerHandle(frame, effectTrigger);
        } else {
            return new DefaultEffectTriggerHandle(frame, effectTrigger);
        }
    }

    public List<EffectTriggerHandle> getFramelessEffectTriggers(String rideIdentifier, List<String> effectNames){
        return effectNames
                .stream()
                .map(effectNameRaw -> {
                    String[] effectNameRawComponents = effectNameRaw.split(":");
                    String effectName = effectNameRawComponents[0];
                    boolean reversed = false;
                    if(effectNameRawComponents.length == 2){
                        reversed = effectNameRawComponents[1].equalsIgnoreCase("reversed");
                    }
//                    Bukkit.broadcastMessage("Initing effect " + effectName + " as reversed: " + reversed);
                    return getEffectTrigger(effectName, reversed, null, configManager.getTriggerConfig(rideIdentifier, effectName));
                })
                .collect(Collectors.toList());
    }

    public EffectTriggerCollection getEffectTriggers(String rideIdentifier, int globalOffset){
        ConfigurationSection allEffectsConfigurationSection = configManager.getAllEffectsConfigSection(rideIdentifier);

        int lastFrameIndex = Integer.MIN_VALUE;
        Map<Frame, String> effects = new HashMap<>();
        Set<String> effectIdentifiers = allEffectsConfigurationSection.getKeys(false);
        for(String effectIdentifier : effectIdentifiers){
            int frameIndex = allEffectsConfigurationSection.getConfigurationSection(effectIdentifier).getInt("frame");
            if(lastFrameIndex > frameIndex) throw new RuntimeException("Every effect trigger frame needs to be after the frame of the effect before");

            effects.put(new SimpleFrame(frameIndex + globalOffset), effectIdentifier);

            lastFrameIndex = frameIndex;
        }

        EffectTriggerHandle previous = null;
        List<EffectTriggerHandle> effectTriggers = new ArrayList<>();
        for(Map.Entry<Frame, String> effect : effects.entrySet()){
            Frame frame = effect.getKey();
            String effectName = effect.getValue();

            EffectTriggerHandle effectTrigger = getEffectTrigger(effectName, false, frame, configManager.getTriggerConfig(rideIdentifier, effectName));
            if(previous != null) previous.setNext(effectTrigger);
            effectTriggers.add(effectTrigger);

            previous = effectTrigger;
        }

        return new EffectTriggerCollection(effectTriggers);
    }
}
