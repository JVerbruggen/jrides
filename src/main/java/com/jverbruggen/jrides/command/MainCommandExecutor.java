package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.command.control.ControlCommandExecutor;

public class MainCommandExecutor extends BaseCommandExecutor {
    private VisualizeCommandExecutor visualizeCommandExecutor;
    private ControlCommandExecutor controlCommandExecutor;

    public MainCommandExecutor() {
        super(0);
        this.visualizeCommandExecutor = registerSubCommand(new VisualizeCommandExecutor(1));
        this.controlCommandExecutor = registerSubCommand(new ControlCommandExecutor(1));
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public String getHelpMessageForParent() {
        return visualizeCommandExecutor.getHelpMessageForParent() + "\n" +
                controlCommandExecutor.getHelpMessageForParent();
    }
}
