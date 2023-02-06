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
import com.jverbruggen.jrides.models.ride.coaster.track.Track;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;
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

    public EffectTriggerHandle getEffectTrigger(String rideIdentifier, String effectName, boolean reversed, Frame frame, TriggerConfig triggerConfig){
        if(triggerConfig == null) return null;

        EffectTrigger effectTrigger;
        String mapKey = rideIdentifier + ":" + effectName;
        if(!effectTriggerMap.containsKey(mapKey)){
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

            effectTriggerMap.put(mapKey, effectTrigger);
        }else{
            effectTrigger = effectTriggerMap.get(mapKey);
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
                    return getEffectTrigger(rideIdentifier, effectName, reversed, null, configManager.getTriggerConfig(rideIdentifier, effectName));
                })
                .collect(Collectors.toList());
    }

    public EffectTriggerCollection getEffectTriggers(String rideIdentifier, Track track){
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

            effects.put(new SimpleFrame(frameIndex, track), effectIdentifier);

            lastFrameIndex = frameIndex;
        }

        EffectTriggerHandle previous = null;
        List<EffectTriggerHandle> effectTriggers = new ArrayList<>();
        for(Map.Entry<Frame, String> effect : effects.entrySet()){
            Frame frame = effect.getKey();
            String effectName = effect.getValue();

            EffectTriggerHandle effectTrigger = getEffectTrigger(rideIdentifier, effectName, false, frame, configManager.getTriggerConfig(rideIdentifier, effectName));
            if(effectTrigger == null) return null;
            if(previous != null) previous.setNext(effectTrigger);
            effectTriggers.add(effectTrigger);

            previous = effectTrigger;
        }

        return new EffectTriggerCollection(effectTriggers);
    }
}
