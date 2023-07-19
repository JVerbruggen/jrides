package com.jverbruggen.jrides.config.trigger.external;

import com.jverbruggen.jrides.config.trigger.BaseTriggerConfig;
import com.jverbruggen.jrides.config.trigger.TriggerType;
import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.effect.common.CommandForPlayerEffectTrigger;
import org.bukkit.configuration.ConfigurationSection;

public class CommandForPlayerTriggerConfig extends BaseTriggerConfig {
    private final String command;

    public CommandForPlayerTriggerConfig(String command) {
        super(TriggerType.COMMAND_FOR_PLAYER);
        this.command = command;
    }

    public static CommandForPlayerTriggerConfig fromConfigurationSection(ConfigurationSection configurationSection){
        String command = getString(configurationSection, "command");

        return new CommandForPlayerTriggerConfig(command);
    }

    @Override
    public EffectTrigger createTrigger() {
        return new CommandForPlayerEffectTrigger(command);
    }
}
