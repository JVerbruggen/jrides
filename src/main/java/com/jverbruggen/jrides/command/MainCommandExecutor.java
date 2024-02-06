package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.command.control.ControlCommandExecutor;
import org.bukkit.ChatColor;

public class MainCommandExecutor extends BaseCommandExecutor {
    public MainCommandExecutor() {
        super(0);
        registerSubCommand(new HelpCommandExecutor(this));
        registerSubCommand(new BlockSectionCommandExecutor());
        registerSubCommand(new VisualizeCommandExecutor());
        registerSubCommand(new ControlCommandExecutor());
        registerSubCommand(new WarpCommandExecutor());
        registerSubCommand(new RideOverviewMapCommandExecutor());
        registerSubCommand(new RideCounterMapCommandExecutor());
        registerSubCommand(new RidesCommandExecutor());
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getHelpMessageForParent() {
        return ChatColor.GOLD + "This server is running jrides - " + JRidesPlugin.getVersion();
    }

    @Override
    public boolean canEveryoneRun() {
        return true;
    }
}
