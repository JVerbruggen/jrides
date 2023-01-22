package com.jverbruggen.jrides.animator;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class TrackVisualisationTool {
    private List<Location> locations;
    private World world;
    private List<Player> viewers;

    private int taskId;

    public TrackVisualisationTool(World world, List<Location> locations){
        this.world = world;
        this.locations = locations;
        this.viewers = new ArrayList<>();
        this.taskId = -1;
    }

    private void runNewTask(){
        if(taskId == -1){
            taskId = Bukkit.getScheduler().runTaskTimer(JRidesPlugin.getBukkitPlugin(), this::tick, 2, 2).getTaskId();
        }else throw new RuntimeException("Task set moment unexpected");
    }

    private void cancelTask(){
        if(taskId != -1){
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }else throw new RuntimeException("Task cancel moment unexpected");
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

    public void tick(){
        for(Player viewer : viewers){
            spawnVisualisationParticles(viewer);
        }
    }

    public void spawnVisualisationParticles(Player player){
        for(Location location : locations){
            world.spawnParticle(Particle.CRIT, location, 1, 0.01, 0.01, 0.01);
            player.getBukkitPlayer().spawnParticle(Particle.CRIT, location, 1);
        }
    }
}
