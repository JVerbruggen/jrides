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
