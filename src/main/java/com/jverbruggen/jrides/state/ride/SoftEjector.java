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

package com.jverbruggen.jrides.state.ride;

import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class SoftEjector {
    public static int ejectInterval = 2*20;
    public static List<UUID> removeList = new ArrayList<>();
    public static Map<UUID, Integer> playerTimers = new HashMap<>();

    public static void startClock(JavaPlugin plugin){
        Bukkit.getScheduler().runTaskTimer(plugin, SoftEjector::tick, 1L, 1L);
    }

    public static void tick(){
        if(playerTimers.size() == 0) return;

        for(Map.Entry<UUID, Integer> entry : playerTimers.entrySet()){
            int timerState = entry.getValue();
            UUID uuid = entry.getKey();
            timerState -= 1;
            if(timerState <= 0){
                removeList.add(uuid);
            }else{
                playerTimers.put(uuid, timerState);
            }
        }

        for(UUID uuid : removeList){
            playerTimers.remove(uuid);
        }
        removeList.clear();
    }

    public static void addTimer(Player player){
        playerTimers.put(player.getBukkitPlayer().getUniqueId(), ejectInterval);
    }

    public static boolean hasTimer(Player player){
        return playerTimers.containsKey(player.getBukkitPlayer().getUniqueId());
    }

    public static void removeTimer(Player player){
        playerTimers.remove(player.getBukkitPlayer().getUniqueId());
    }
}
