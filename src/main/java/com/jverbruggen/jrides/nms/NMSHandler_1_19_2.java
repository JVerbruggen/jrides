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
