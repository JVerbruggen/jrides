package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.command.control.ControlCommandExecutor;

public class MainCommandExecutor extends BaseCommandExecutor {
    private VisualizeCommandExecutor visualizeCommandExecutor;
    private BlockSectionCommandExecutor blockSectionCommandExecutor;
    private ControlCommandExecutor controlCommandExecutor;
    private WarpCommandExecutor warpCommandExecutor;

    public MainCommandExecutor() {
        super(0);
        this.blockSectionCommandExecutor = registerSubCommand(new BlockSectionCommandExecutor());
        this.visualizeCommandExecutor = registerSubCommand(new VisualizeCommandExecutor());
        this.controlCommandExecutor = registerSubCommand(new ControlCommandExecutor());
        this.warpCommandExecutor = registerSubCommand(new WarpCommandExecutor());
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public String getHelpMessageForParent() {
        return visualizeCommandExecutor.getHelpMessageForParent() + "\n" +
                blockSectionCommandExecutor.getHelpMessageForParent() + "\n" +
                controlCommandExecutor.getHelpMessageForParent() + "\n" +
                warpCommandExecutor.getHelpMessageForParent();
    }
}
