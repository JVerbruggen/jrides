package com.jverbruggen.jrides.packets;

import org.bukkit.Bukkit;

public class PacketSenderFactory {
    public static PacketSender getPacketSender(){
        String currentVersion = Bukkit.getVersion();
        boolean debugMode = false;

        if(currentVersion.contains("1.19.2")){
            return new PacketSender_1_19_2(debugMode);
        }else if(currentVersion.contains("1.20")){
            return new PacketSender_1_20_1(debugMode);
        }

        throw new RuntimeException("No packet sender implemented for bukkit version '" + currentVersion + "'");
    }
}
