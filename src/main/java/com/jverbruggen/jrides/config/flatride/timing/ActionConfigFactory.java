package com.jverbruggen.jrides.config.flatride.timing;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ActionConfigFactory extends BaseConfig {
    public static List<ActionConfig> fromConfigurationSection(@Nullable ConfigurationSection configurationSection){
        List<ActionConfig> actionConfigs = new ArrayList<>();
        if(configurationSection == null) return actionConfigs;

        for(String key : configurationSection.getKeys(false)){
            ConfigurationSection actionConfigurationSection = configurationSection.getConfigurationSection(key);
            assert actionConfigurationSection != null;

            if(actionConfigurationSection.contains("animation")){
                actionConfigs.add(createAnimationActionConfig(actionConfigurationSection));
                continue;
            }

            SimpleActionConfig actionConfig = new SimpleActionConfig(key);
            if(actionConfigurationSection.contains("speed"))
                actionConfig.setSpeed((float) getDouble(actionConfigurationSection, "speed"));
            if(actionConfigurationSection.contains("accelerate"))
                actionConfig.setAccelerate((float) getDouble(actionConfigurationSection, "accelerate"));
            if(actionConfigurationSection.contains("allowControl"))
                actionConfig.setAllowControl(getBoolean(actionConfigurationSection, "allowControl"));
            if(actionConfigurationSection.contains("targetPosition"))
                actionConfig.setTargetPosition((float) getDouble(actionConfigurationSection, "targetPosition"));

            actionConfigs.add(actionConfig);
        }

        return actionConfigs;
    }

    public static ActionConfig createAnimationActionConfig(@Nullable ConfigurationSection configurationSection){
        String animation = getString(configurationSection, "animation");

        return new AnimationActionConfig(animation);
    }
}
