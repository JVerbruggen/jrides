package com.jverbruggen.jrides.config.trigger.external;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.common.CommandAsPlayerEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

public class CommandAsPlayerTriggerConfig extends BaseTriggerConfig {
    private final String command;

    public CommandAsPlayerTriggerConfig(String command) {
        super(TriggerType.COMMAND_AS_PLAYER);
        this.command = command;
    }

    public static CommandAsPlayerTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        String command = getString(configurationSection, "command");

        return new CommandAsPlayerTriggerConfig(command);
    }

    @Override
    public EffectTrigger createTrigger() {
        return new CommandAsPlayerEffectTrigger(command);
    }
}
