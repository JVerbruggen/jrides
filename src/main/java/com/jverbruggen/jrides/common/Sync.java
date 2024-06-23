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

package com.jverbruggen.jrides.common;

import com.jverbruggen.jrides.JRidesPlugin;
import org.bukkit.Bukkit;

public class Sync {
    public static void runSynced(Runnable runnable){
        Bukkit.getScheduler().runTask(JRidesPlugin.getBukkitPlugin(), runnable);
    }

    public static int runRepeated(Runnable runnable, long interval){
        return Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), runnable, interval, interval).getTaskId();
    }

    public static void stopTask(int taskId){
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public static void runAfter(Runnable runnable, long afterTicks) {
        Bukkit.getScheduler().runTaskLater(JRidesPlugin.getBukkitPlugin(), runnable, afterTicks);
    }
}
