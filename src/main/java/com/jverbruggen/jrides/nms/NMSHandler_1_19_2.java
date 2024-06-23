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

package com.jverbruggen.jrides.nms;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSHandler_1_19_2 implements NMSHandler {

    /**
     * Same as:
     * ((CraftPlayer) player.getBukkitPlayer()).getHandle().setPos(0,0,0);
     * But should work across multiple versions
     * @param player
     * @param position
     */
    @Override
    public void setPlayerLocationNoTeleport(Player player, Vector3 position) {
        org.bukkit.entity.Player bukkitPlayer = player.getBukkitPlayer();
        double x = position.getX();
        double y = position.getY();
        double z = position.getZ();

        Class<?> craftPlayerClass = MinecraftReflection.getCraftPlayerClass();

        Method getHandleMethod = null;
        try {
            getHandleMethod = craftPlayerClass.getMethod("getHandle");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        Object serverPlayer = null;
        try {
            serverPlayer = getHandleMethod.invoke(bukkitPlayer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        Class<?> serverPlayerClass = MinecraftReflection.getEntityClass();
//        Arrays.stream(serverPlayerClass.getMethods()).toList().forEach(m -> Bukkit.broadcastMessage(m.getName() + ": " + String.join(",", Arrays.stream(m.getParameterTypes()).map(Class::getName).toList())));

        Method setPosMethod = null;
        try {
            setPosMethod = serverPlayerClass.getMethod("setPosRaw", double.class, double.class, double.class, boolean.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        try {
            setPosMethod.invoke(serverPlayer, x, y, z, false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
