package com.jverbruggen.jrides.effect.platform;

import com.jverbruggen.jrides.effect.EffectTrigger;
import com.jverbruggen.jrides.models.ride.coaster.Train;
import org.bukkit.Bukkit;

import java.util.List;

public class MultiArmorstandMovementEffectTrigger implements EffectTrigger {
    private List<ArmorstandMovementEffectTrigger> armorstandMovements;

    public MultiArmorstandMovementEffectTrigger(List<ArmorstandMovementEffectTrigger> armorstandMovements) {
        this.armorstandMovements = armorstandMovements;
    }

    @Override
    public void execute(Train train) {
        Bukkit.broadcastMessage("Platform normal direction");

        armorstandMovements.forEach(a -> a.execute(train));
    }

    @Override
    public void executeReversed(Train train) {
        Bukkit.broadcastMessage("Platform reversed direction");

        armorstandMovements.forEach(a -> a.executeReversed(train));
    }

    @Override
    public boolean finishedPlaying() {
        return armorstandMovements.stream().allMatch(ArmorstandMovementEffectTrigger::finishedPlaying);
    }
}
