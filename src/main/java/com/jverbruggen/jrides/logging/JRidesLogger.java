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
            log(Level.WARNING, msg);
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
