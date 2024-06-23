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

package com.jverbruggen.jrides.logging;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JRidesLogger extends Logger {
    private final Logger bukkitPluginLogger;
    private Level threshold;
    private boolean broadcastMode;
    private List<LogType> enabledLogTypes;
    private boolean bukkitActive;

    public JRidesLogger(Logger bukkitPluginLogger, boolean broadcastMode, boolean bukkitActive) {
        super("JRidesLogger", null);
        this.bukkitPluginLogger = bukkitPluginLogger;
        this.threshold = Level.INFO;
        this.broadcastMode = broadcastMode;
        this.enabledLogTypes = new ArrayList<>();
        this.bukkitActive = bukkitActive;

        enableLogType(LogType.CRASH);
//        enableLogType(LogType.SECTIONS);
//        enableLogType(LogType.SECTIONS_DETAIL);
    }

    public void enableLogType(LogType logType){
        enabledLogTypes.add(logType);
    }

    public void info(LogType logType, String msg){
        if(enabledLogTypes.contains(logType)) log(Level.INFO, msg);
    }

    public void warning(LogType logType, String msg){
        if(enabledLogTypes.contains(logType)) log(Level.WARNING, msg);
    }

    public void severe(LogType logType, String msg){
        if(enabledLogTypes.contains(logType)) log(Level.SEVERE, msg);
    }

    /**
     * Only to be used temporarily
     */
    public void debug(String msg) {
        if(bukkitActive){
            Bukkit.broadcastMessage(msg);
        }else{
            System.out.println(msg);
        }
    }

    @Override
    public void log(Level level, String msg) {
        if(level.intValue() < threshold.intValue()) return;

        if(broadcastMode) {
            ChatColor color;
            if(level.equals(Level.INFO))
                color = ChatColor.GRAY;
            else if(level.equals(Level.WARNING))
                color = ChatColor.YELLOW;
            else if(level.equals(Level.SEVERE))
                color = ChatColor.DARK_RED;
            else
                color = ChatColor.GRAY;

            Bukkit.broadcastMessage(color + "[jrides] " + msg);
        } else
            bukkitPluginLogger.log(level, msg);
    }

}
