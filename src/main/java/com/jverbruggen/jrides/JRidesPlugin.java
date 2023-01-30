package com.jverbruggen.jrides;

import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimation;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class JRidesPlugin {
    private static JRidesLogger logger;
    private static JavaPlugin plugin;
    private static PacketSender packetSender;
    private static SmoothAnimation smoothAnimation;
    private static LanguageFile languageFile;
    private static World world;

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
    }

    public static void initOtherStatics(){
        packetSender = ServiceProvider.getSingleton(PacketSender.class);
        smoothAnimation = ServiceProvider.getSingleton(SmoothAnimation.class);
        logger = ServiceProvider.getSingleton(JRidesLogger.class);
        languageFile = ServiceProvider.getSingleton(LanguageFile.class);

//        logger.enableLogType(LogType.SECTIONS);
    }

    public static PacketSender getPacketSender() {
        return packetSender;
    }

    public static SmoothAnimation getSmoothAnimation() {
        return smoothAnimation;
    }

    public static JRidesLogger getLogger() {
        return logger;
    }

    public static LanguageFile getLanguageFile() {
        return languageFile;
    }

    public static void setWorld(World world) {
        JRidesPlugin.world = world;
    }

    public static World getWorld() {
        return world;
    }
}
