package com.jverbruggen.jrides;

import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class JRidesPlugin {
    private static JavaPlugin plugin;

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
}
