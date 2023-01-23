package com.jverbruggen.jrides;

import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimation;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class JRidesPlugin {
    private static JavaPlugin plugin;
    private static PacketSender packetSender;
    private static SmoothAnimation smoothAnimation;

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
        packetSender = ServiceProvider.GetSingleton(PacketSender.class);
        smoothAnimation = ServiceProvider.GetSingleton(SmoothAnimation.class);
    }

    public static PacketSender getPacketSender() {
        return packetSender;
    }

    public static SmoothAnimation getSmoothAnimation() {
        return smoothAnimation;
    }
}
