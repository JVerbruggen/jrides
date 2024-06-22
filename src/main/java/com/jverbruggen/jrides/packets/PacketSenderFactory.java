package com.jverbruggen.jrides.packets;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.packets.impl.PacketSender_1_19_2;
import com.jverbruggen.jrides.packets.impl.PacketSender_1_20_1;
import com.jverbruggen.jrides.packets.impl.PacketSender_1_20_4;
import org.bukkit.Bukkit;

public class PacketSenderFactory {
    public static PacketSender getPacketSender(){
        String currentVersion = Bukkit.getVersion();
        boolean debugMode = false;
        PacketSender packetSender = null;

        if(currentVersion.contains("1.19.2")){
            packetSender = new PacketSender_1_19_2(debugMode);
        }else if(currentVersion.contains("1.20.4")){
            packetSender = new PacketSender_1_20_4(debugMode);
        }else if(currentVersion.contains("1.20")){
            packetSender = new PacketSender_1_20_1(debugMode);
        }

        if(packetSender == null)
            throw new RuntimeException("No packet sender implemented for bukkit version '" + currentVersion + "'");

        JRidesPlugin.getLogger().info("Using packet sender for '" + packetSender.getIdentifier() + "' (bukkit version '" + currentVersion + "')");

        return packetSender;
    }
}
