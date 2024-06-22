package com.jverbruggen.jrides.command;

import java.util.Objects;
import java.util.stream.Collectors;

public class HelpCommandExecutor extends BaseCommandExecutor {
    private final MainCommandExecutor mainCommandExecutor;

    public HelpCommandExecutor(MainCommandExecutor mainCommandExecutor) {
        super(1);
        this.mainCommandExecutor = mainCommandExecutor;
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getHelpMessageForParent() {
        return mainCommandExecutor.subCommands.values().stream()
                .map(executor -> {
                    if(executor == this) return null;
                    return executor.getHelpMessageForParent();
                })
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.joining("\n"));
    }
}
