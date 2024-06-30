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

package com.jverbruggen.jrides;

import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimation;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.GlobalConfig;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.logging.LogType;
import com.jverbruggen.jrides.models.entity.BroadcastMessageReceiver;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class JRidesPlugin {
    private static JRidesLogger logger = null;
    private static JavaPlugin plugin;
    private static PacketSender packetSender;
    private static SmoothAnimation smoothAnimation;
    private static LanguageFile languageFile;
    private static World world;
    private static MessageReceiver broadcastMessageReceiver;

    public static ServiceProvider getServiceProvider(){
        return ServiceProvider.instance;
    }

    public static JavaPlugin getBukkitPlugin(){
        if(plugin == null){
            throw new RuntimeException("JRidesPlugin not defined since it is not yet enabled");
        }

        return plugin;
    }

    public static void setBukkitPluginHost(JavaPlugin plugin){
        JRidesPlugin.plugin = plugin;
        logger = ServiceProvider.getSingleton(JRidesLogger.class);
    }

    public static void initOtherStatics(){
        packetSender = ServiceProvider.getSingleton(PacketSender.class);
        smoothAnimation = ServiceProvider.getSingleton(SmoothAnimation.class);
        languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        broadcastMessageReceiver = new BroadcastMessageReceiver();

//        logger.enableLogType(LogType.SECTIONS);
    }

    public static PacketSender getPacketSender() {
        return packetSender;
    }

    public static SmoothAnimation getSmoothAnimation() {
        return smoothAnimation;
    }

    public static JRidesLogger getLogger() {
        if(logger == null){ // Only occurs when not running as plugin
            logger = new JRidesLogger(Logger.getGlobal(), false, false);
        }
        return logger;
    }

    public static LanguageFile getLanguageFile() {
        return languageFile;
    }

    public static void configureWorld() {
        ConfigManager configManager = ServiceProvider.getSingleton(ConfigManager.class);
        GlobalConfig globalConfig = configManager.getGlobalConfig();
        JRidesPlugin.world = Bukkit.getWorld(globalConfig.getWorldName());
    }

    public static World getWorld() {
        return world;
    }

    private static String getBukkitVersion(){
        return Bukkit.getServer().getBukkitVersion().split("-")[0];
    }

    public static String getVersion() {
        return getBukkitVersion() + "-j0.0";
    }

    public static MessageReceiver getBroadcastReceiver(){
        return broadcastMessageReceiver;
    }
}
