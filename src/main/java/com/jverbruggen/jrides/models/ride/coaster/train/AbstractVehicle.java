package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.SoundCategory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVehicle implements Vehicle {
    private final String name;
    private List<Player> passengers;
    private boolean crashed;
    private boolean debugMode;

    public AbstractVehicle(String name, boolean debugMode) {
        this.name = name;
        this.crashed = false;
        this.passengers = new ArrayList<>();
        this.debugMode = debugMode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }

    @Override
    public boolean isCrashed() {
        return crashed;
    }

    @Override
    public List<Player> getPassengers() {
        return passengers;
    }

    public void removePassenger(Player player){
        passengers.remove(player);
    }

    public void addPassenger(Player player){
        passengers.add(player);
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    protected void playSound(String soundName){
        if(soundName == null) return;
        JRidesPlugin.getWorld().playSound(getCurrentLocation().toBukkitLocation(JRidesPlugin.getWorld()), soundName, SoundCategory.MASTER, 0.1f, 1f);
    }
}
