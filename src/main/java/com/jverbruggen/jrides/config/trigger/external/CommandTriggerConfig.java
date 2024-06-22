package com.jverbruggen.jrides.config.trigger.external;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.external.CommandEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

public class CommandTriggerConfig extends BaseTriggerConfig {
    private final String command;

    public CommandTriggerConfig(String command) {
        super(TriggerType.COMMAND);
        this.command = command;
    }

    public static CommandTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        String command = getString(configurationSection, "command");

        return new CommandTriggerConfig(command);
    }

    @Override
    public EffectTrigger createTrigger() {
        return new CommandEffectTrigger(command);
    }
}
