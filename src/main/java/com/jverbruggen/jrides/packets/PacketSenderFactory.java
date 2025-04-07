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

package com.jverbruggen.jrides.packets;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.packets.impl.PacketSender_1_19_2;
import com.jverbruggen.jrides.packets.impl.PacketSender_1_20_1;
import com.jverbruggen.jrides.packets.impl.PacketSender_1_20_4;
import com.jverbruggen.jrides.packets.listener.VirtualEntityPacketListener;
import com.jverbruggen.jrides.packets.listener.VirtualEntityPacketListener_1_19_2;
import com.jverbruggen.jrides.packets.listener.VirtualEntityPacketListener_1_20_4;
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

    public static VirtualEntityPacketListener getPacketListener(){
        String currentVersion = Bukkit.getVersion();
        boolean debugMode = false;
        VirtualEntityPacketListener packetListener = null;

        if(currentVersion.contains("1.19.2")){
            packetListener = new VirtualEntityPacketListener_1_19_2();
        }else if(currentVersion.contains("1.20.4")){
            packetListener = new VirtualEntityPacketListener_1_20_4();
        }else if(currentVersion.contains("1.20")){
            packetListener = new VirtualEntityPacketListener_1_20_4();
        }

        if(packetListener == null)
            throw new RuntimeException("No packet listener implemented for bukkit version '" + currentVersion + "'");

        JRidesPlugin.getLogger().info("Using packet listener for '" + packetListener.getIdentifier() + "' (bukkit version '" + currentVersion + "')");

        return packetListener;
    }
}
