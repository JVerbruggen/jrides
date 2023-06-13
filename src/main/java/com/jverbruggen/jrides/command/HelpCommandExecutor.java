package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.command.control.ControlCommandExecutor;

public class HelpCommandExecutor extends BaseCommandExecutor {
    private final VisualizeCommandExecutor visualizeCommandExecutor;
    private final BlockSectionCommandExecutor blockSectionCommandExecutor;
    private final ControlCommandExecutor controlCommandExecutor;
    private final WarpCommandExecutor warpCommandExecutor;
    private final RideOverviewMapCommandExecutor rideOverviewMapCommandExecutor;

    public HelpCommandExecutor() {
        super(1);
        this.blockSectionCommandExecutor = new BlockSectionCommandExecutor();
        this.visualizeCommandExecutor = new VisualizeCommandExecutor();
        this.controlCommandExecutor = new ControlCommandExecutor();
        this.warpCommandExecutor = new WarpCommandExecutor();
        this.rideOverviewMapCommandExecutor = new RideOverviewMapCommandExecutor();
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getHelpMessageForParent() {
        return visualizeCommandExecutor.getHelpMessageForParent() + "\n" +
                blockSectionCommandExecutor.getHelpMessageForParent() + "\n" +
                controlCommandExecutor.getHelpMessageForParent() + "\n" +
                warpCommandExecutor.getHelpMessageForParent() + "\n" +
                rideOverviewMapCommandExecutor.getHelpMessageForParent();
    }
}
