package com.jverbruggen.jrides.nms;

import org.bukkit.Bukkit;

public class NMSHandlerFactory {
    public static NMSHandler getNMSHandler(){
        String currentVersion = Bukkit.getVersion();

        if(currentVersion.contains("1.19.2")){
            return new NMSHandler_1_19_2();
        }else if(currentVersion.contains("1.20")){
            return new NMSHandler_1_19_2();
        }

        throw new RuntimeException("No nms handler implemented for bukkit version '" + currentVersion + "'");
    }
}
