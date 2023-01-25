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

    public JRidesLogger(Logger bukkitPluginLogger, boolean broadcastMode) {
        super("JRidesLogger", null);
        this.bukkitPluginLogger = bukkitPluginLogger;
        this.threshold = Level.INFO;
        this.broadcastMode = broadcastMode;
        this.enabledLogTypes = new ArrayList<>();
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

    @Override
    public void log(Level level, String msg) {
        if(level.intValue() < threshold.intValue()) return;

        if(broadcastMode) {
            ChatColor color;
            if(level.equals(Level.INFO))
                color = ChatColor.GRAY;
            else if(level.equals(Level.WARNING))
                color = ChatColor.RED;
            else if(level.equals(Level.SEVERE))
                color = ChatColor.DARK_RED;
            else
                color = ChatColor.GRAY;

            Bukkit.broadcastMessage(color + "[jrides] " + msg);
        } else
            bukkitPluginLogger.log(level, msg);
    }
}
