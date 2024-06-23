/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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
