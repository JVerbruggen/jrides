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

package com.jverbruggen.jrides.animator.animatedjava;

import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.Bukkit;

public class AnimatedJavaExecutor {
    private final String rootEntityTag;
    private final String rigName;
    private final Vector3 location;
    private boolean isSpawned;

    public AnimatedJavaExecutor(String rootEntityTag, String rigName, Vector3 location) {
        this.rootEntityTag = rootEntityTag;
        this.rigName = rigName;
        this.location = location;
        this.isSpawned = false;
    }

    public boolean isSpawned(){
        return isSpawned;
    }

    public void spawnRig(){
        executeCommand(getSpawnRigCommand());
        isSpawned = true;
    }

    public void playAnimation(String animationName){
        if(!isSpawned) throw new RuntimeException("Rig was not spawned");
        executeCommand(getPlayAnimationCommand(animationName));
    }

    public void removeRig(){
        executeCommand(getRemoveRigCommand());
        isSpawned = false;
    }

    public void removeAllRigs(){
        executeCommand(getRemoveAllRigsCommand());
        isSpawned = false;
    }

    private void executeCommand(String command){
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    private String getSpawnRigCommand(){
        return getRunFunctionBaseCommand() + "animated_java:" + rigName + "/summon";
    }

    private String getRemoveRigCommand(){
        return getRunFunctionAsEntityBaseCommand() + "animated_java:" + rigName + "/remove/this";
    }

    private String getRemoveAllRigsCommand(){
        return getRunFunctionBaseCommand() + "animated_java:" + rigName + "/remove/all";
    }

    private String getPlayAnimationCommand(String animationName){
        return getRunFunctionAsEntityBaseCommand() + "animated_java:" + rigName + "/animations/" + animationName + "/play";
    }

    private String getRunFunctionAsEntityBaseCommand(){
        return "execute " +
                "positioned " + getLocationString() + " " +
                "as @e[type=minecraft:item_display,tag=" + rootEntityTag + ",limit=1] " +
                "run function ";
    }

    private String getRunFunctionBaseCommand(){
        return "execute " +
                "positioned " + getLocationString() + " " +
                "run function ";
    }

    private String getLocationString(){
        return location.getX() + " " + location.getY() + " " + location.getZ();
    }
}
