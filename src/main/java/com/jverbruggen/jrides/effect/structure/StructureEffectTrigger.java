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
        this.delayedEntityTask = createTask(delayTicks);
    }

    private boolean isStructureLibAvailable(){
        String bukkitVersion = JRidesPlugin.getBukkitPlugin().getServer().getBukkitVersion();
        return bukkitVersion.contains("1.19")
                || bukkitVersion.contains("1.20.1")
                || bukkitVersion.contains("1.20.2")
                || bukkitVersion.contains("1.20.3")
                || bukkitVersion.contains("1.20.4");
    }

    private DelayedEntityTask createTask(int delayTicks){
        if(isStructureLibAvailable()){
            return new DelayedEntityTask(this::runTask, delayTicks, 0);
        }else{
            JRidesPlugin.getLogger().severe("StructureBlock Lib is ONLY available in 1.19 and 1.20.1-1.20.4");
            return null;
        }
    }

    private void runTask(){
        if(isStructureLibAvailable()){
            loadStructure();
        }
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
        if(delayedEntityTask == null) return true;

        return delayedEntityTask.isFinished();
    }

    @Override
    public boolean execute(Train train) {
        if(delayedEntityTask == null) return true;
        if(delayedEntityTask.isBusy()) return true;

        delayedEntityTask.start();
        return true;
    }

    @Override
    public boolean executeReversed(Train train) {
        return execute(train);
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
