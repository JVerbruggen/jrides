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

package com.jverbruggen.jrides.models.entity.agent;

import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class MessageAgentManager {
    private final HashMap<String, MessageAgent> cache;

    public MessageAgentManager() {
        this.cache = new HashMap<>();
    }

    public MessageAgent getOrCreateMessageAgent(CommandSender commandSender){
        MessageAgent existing = cache.get(commandSender.getName());
        if(existing != null) return existing;

        existing = new CommandSenderMessageAgent(commandSender);
        cache.put(commandSender.getName(), existing);
        return existing;
    }

    public void removeMessageAgent(String name){
        cache.remove(name);
    }

}
