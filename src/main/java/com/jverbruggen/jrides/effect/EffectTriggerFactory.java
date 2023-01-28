package com.jverbruggen.jrides.effect;

import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.trigger.TriggerConfig;
import com.jverbruggen.jrides.effect.music.MusicEffectTriggerFactory;
import com.jverbruggen.jrides.models.properties.Frame;
import com.jverbruggen.jrides.models.properties.SimpleFrame;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.stream.Collectors;

public class EffectTriggerFactory {
    private final MusicEffectTriggerFactory musicEffectTriggerFactory;
    private final ConfigManager configManager;

    public EffectTriggerFactory() {
        this.musicEffectTriggerFactory = ServiceProvider.getSingleton(MusicEffectTriggerFactory.class);
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
    }

    public EffectTrigger getEffectTrigger(Frame frame, TriggerConfig triggerConfig){
        switch(triggerConfig.getType()){
            case MUSIC:
                return musicEffectTriggerFactory.getMusicEffectTrigger(frame, triggerConfig);
            default:
                throw new RuntimeException("Cannot resolve trigger type when creating effect trigger");
        }
    }

    public List<EffectTrigger> getFramelessEffectTriggers(String rideIdentifier, List<String> effectNames){
        return effectNames
                .stream()
                .map(effectName -> getEffectTrigger(null, configManager.getTriggerConfig(rideIdentifier, effectName)))
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

        EffectTrigger previous = null;
        List<EffectTrigger> effectTriggers = new ArrayList<>();
        for(Map.Entry<Frame, String> effect : effects.entrySet()){
            Frame frame = effect.getKey();
            String effectName = effect.getValue();

            EffectTrigger effectTrigger = getEffectTrigger(frame, configManager.getTriggerConfig(rideIdentifier, effectName));
            if(previous != null) previous.setNext(effectTrigger);
            effectTriggers.add(effectTrigger);

            previous = effectTrigger;
        }

        return new EffectTriggerCollection(effectTriggers);
    }
}
