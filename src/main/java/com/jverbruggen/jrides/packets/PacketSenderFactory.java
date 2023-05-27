package com.jverbruggen.jrides.packets;

import org.bukkit.Bukkit;

public class PacketSenderFactory {
    public static PacketSender getPacketSender(){
        String currentVersion = Bukkit.getVersion();

        if(currentVersion.contains("1.19.2")){
            return new PacketSender_1_19_2();
        }

        throw new RuntimeException("No packet sender implemented for bukkit version '" + currentVersion + "'");
    }
}
