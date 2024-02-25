package com.jverbruggen.jrides.effect.structure;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.effect.entity.common.DelayedEntityTask;
import com.jverbruggen.jrides.effect.train.BaseTrainEffectTrigger;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import org.bukkit.Location;
import org.bukkit.World;

public class StructureEffectTrigger extends BaseTrainEffectTrigger {
    private final Location location;
    private final String worldName;
    private final String structureIdentifierName;
    private final String structureIdentifierAuthor;

    private final DelayedEntityTask delayedEntityTask;

    public StructureEffectTrigger(int delayTicks, Vector3 position, String structureIdentifierName, String structureIdentifierAuthor) {
        this.structureIdentifierName = structureIdentifierName;
        this.structureIdentifierAuthor = structureIdentifierAuthor;
        World world = JRidesPlugin.getWorld();

        this.location = position.toBukkitLocation(world);
        this.worldName = world.getName();
        this.delayedEntityTask = new DelayedEntityTask(this::runTask, delayTicks, 0);
    }

    private void runTask(){
        loadStructure();
    }

    private void loadStructure(){
        StructureBlockLibApi.INSTANCE
                .loadStructure(JRidesPlugin.getBukkitPlugin())
                .at(location)
                .loadFromWorld(worldName, structureIdentifierAuthor, structureIdentifierName)
                .onException(e -> JRidesPlugin.getLogger().warning(
                        "Could not load StructureEffectTrigger, " +
                                "author: '" + structureIdentifierAuthor + "', " +
                                "name: '" + structureIdentifierName + "' " +
                                "for reason: '" + e.getMessage() + "'"));
    }

    @Override
    public boolean finishedPlaying() {
        return delayedEntityTask.isFinished();
    }

    @Override
    public void execute(Train train) {
        if(delayedEntityTask.isBusy()) return;

        delayedEntityTask.start();
    }

    @Override
    public void executeReversed(Train train) {
        execute(train);
    }

    public static StructureEffectTrigger createStructureEffectTrigger(int delayTicks, Vector3 position, String structureIdentifier){
        if(!structureIdentifier.contains(":")){
            throw new RuntimeException("Structure identifier should be of format '<author>:<structure_name>'");
        }

        String[] splitIdentifier = structureIdentifier.split(":");
        String structureIdentifierAuthor = splitIdentifier[0];
        String structureIdentifierName = splitIdentifier[1];

        return new StructureEffectTrigger(delayTicks, position, structureIdentifierName, structureIdentifierAuthor);
    }
}
