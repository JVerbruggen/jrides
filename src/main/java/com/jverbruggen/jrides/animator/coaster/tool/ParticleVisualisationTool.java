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

package com.jverbruggen.jrides.animator.coaster.tool;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public abstract class ParticleVisualisationTool {
    private List<Player> viewers;
    private int taskId;
    private int interval;

    public abstract void tick();

    public ParticleVisualisationTool(int interval) {
        this.viewers = new ArrayList<>();
        this.taskId = -1;
        this.interval = interval;
    }

    public List<Player> getViewers() {
        return viewers;
    }

    public void addViewer(Player player){
        viewers.add(player);

        if(taskId == -1) runNewTask();
    }

    public void removeViewer(Player player){
        viewers.remove(player);

        if(viewers.size() == 0) cancelTask();
    }

    public boolean isViewer(Player player){
        return viewers.contains(player);
    }

    private void runNewTask(){
        if(taskId == -1){
            taskId = Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, interval, interval).getTaskId();
        }else throw new RuntimeException("Task set moment unexpected");
    }

    private void cancelTask(){
        if(taskId != -1){
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }else throw new RuntimeException("Task cancel moment unexpected");
    }

}
