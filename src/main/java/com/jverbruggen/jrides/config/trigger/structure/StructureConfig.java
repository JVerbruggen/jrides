package com.jverbruggen.jrides.config.trigger.structure;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.structure.StructureEffectTrigger;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class StructureConfig extends BaseTriggerConfig {
    private final String structureIdentifier;
    private final Vector3 position;
    private final int delayTicks;

    public StructureConfig(String structureIdentifier, Vector3 position, int delayTicks) {
        super(TriggerType.STRUCTURE);
        this.structureIdentifier = structureIdentifier;
        this.position = position;
        this.delayTicks = delayTicks;
    }

    public String getStructureIdentifier() {
        return structureIdentifier;
    }

    public Vector3 getPosition() {
        return position;
    }

    public int getDelayTicks() {
        return delayTicks;
    }

    public static StructureConfig fromConfigurationSection(ConfigurationSection configurationSection){
        String structureIdentifier = getString(configurationSection, "structureIdentifier");
        Vector3 position = Vector3.fromDoubleList(getDoubleList(configurationSection, "position"));
        int delayTicks = getInt(configurationSection, "delayTicks", 0);

        return new StructureConfig(structureIdentifier, position, delayTicks);
    }

    @Override
    public EffectTrigger createTrigger() {
        return StructureEffectTrigger.createStructureEffectTrigger(getDelayTicks(), getPosition(), getStructureIdentifier());
    }
}
