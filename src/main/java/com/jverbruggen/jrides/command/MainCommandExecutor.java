package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.command.control.ControlCommandExecutor;
import org.bukkit.ChatColor;

public class MainCommandExecutor extends BaseCommandExecutor {
    private final HelpCommandExecutor helpCommandExecutor;
    private final VisualizeCommandExecutor visualizeCommandExecutor;
    private final BlockSectionCommandExecutor blockSectionCommandExecutor;
    private final ControlCommandExecutor controlCommandExecutor;
    private final WarpCommandExecutor warpCommandExecutor;
    private final RideOverviewMapCommandExecutor rideOverviewMapCommandExecutor;

    public MainCommandExecutor() {
        super(0);
        this.helpCommandExecutor = registerSubCommand(new HelpCommandExecutor());
        this.blockSectionCommandExecutor = registerSubCommand(new BlockSectionCommandExecutor());
        this.visualizeCommandExecutor = registerSubCommand(new VisualizeCommandExecutor());
        this.controlCommandExecutor = registerSubCommand(new ControlCommandExecutor());
        this.warpCommandExecutor = registerSubCommand(new WarpCommandExecutor());
        this.rideOverviewMapCommandExecutor = registerSubCommand(new RideOverviewMapCommandExecutor());
    }

    @Override
    public String getCommand() {
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
