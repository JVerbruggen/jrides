package com.jverbruggen.jrides.config.flatride.timing;

import com.jverbruggen.jrides.animator.flatride.FlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideHandle;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.ControlInstruction;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.InstructionBinding;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.SpeedInstruction;
import com.jverbruggen.jrides.animator.flatride.timing.instruction.TimingAction;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionConfig extends BaseConfig {
    private final String targetIdentifier;
    private Float speed;
    private Float accelerate;
    private Float controlSpeed;
    private Boolean allowControl;

    public ActionConfig(String targetIdentifier) {
        this.targetIdentifier = targetIdentifier;
        speed = null;
        accelerate = null;
        controlSpeed = null;
        allowControl = null;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getAccelerate() {
        return accelerate == null ? 0 : accelerate;
    }

    public void setAccelerate(Float accelerate) {
        this.accelerate = accelerate;
    }

    public float getControlSpeed() {
        return controlSpeed == null ? 0 : controlSpeed;
    }

    public void setControlSpeed(Float controlSpeed) {
        this.controlSpeed = controlSpeed;
    }

    public Boolean allowsControl() {
        return allowControl;
    }

    public void setAllowControl(Boolean allowControl) {
        this.allowControl = allowControl;
    }

    public static List<ActionConfig> fromConfigurationSection(@Nullable ConfigurationSection configurationSection){
        List<ActionConfig> actionConfigs = new ArrayList<>();
        if(configurationSection == null) return actionConfigs;

        for(String key : configurationSection.getKeys(false)){
            ActionConfig actionConfig = new ActionConfig(key);
            ConfigurationSection actionConfigurationSection = configurationSection.getConfigurationSection(key);

            if(actionConfigurationSection.contains("speed"))
                actionConfig.setSpeed((float) getDouble(actionConfigurationSection, "speed"));
            if(actionConfigurationSection.contains("accelerate"))
                actionConfig.setAccelerate((float) getDouble(actionConfigurationSection, "accelerate"));
            if(actionConfigurationSection.contains("controlSpeed"))
                actionConfig.setControlSpeed((float) getDouble(actionConfigurationSection, "controlSpeed"));
            if(actionConfigurationSection.contains("allowControl"))
                actionConfig.setAllowControl(getBoolean(actionConfigurationSection, "allowControl"));

            actionConfigs.add(actionConfig);
        }

        return actionConfigs;
    }

    public List<TimingAction> getTimingAction(List<FlatRideComponent> flatRideComponents){
        List<FlatRideComponent> targetedFlatRideComponents = flatRideComponents.stream()
                .filter(c -> c.equalsToIdentifier(targetIdentifier))
                .toList();

        if(targetedFlatRideComponents.size() == 0) return Collections.emptyList();

        List<TimingAction> timingActions = new ArrayList<>();
        if(speed != null){
            timingActions.add(new InstructionBinding(new SpeedInstruction(getAccelerate(), getSpeed()), targetedFlatRideComponents));
        }
        if(allowControl != null){
            timingActions.add(new InstructionBinding(new ControlInstruction(allowsControl(), getControlSpeed()), targetedFlatRideComponents));
        }
        return timingActions;
    }
}
