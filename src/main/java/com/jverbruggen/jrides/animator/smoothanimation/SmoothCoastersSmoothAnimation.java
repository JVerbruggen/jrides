package com.jverbruggen.jrides.animator.smoothanimation;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Quaternion;
import me.m56738.smoothcoasters.api.SmoothCoastersAPI;
import org.bukkit.Bukkit;

public class SmoothCoastersSmoothAnimation implements SmoothAnimation {
    private final SmoothCoastersAPI api;

    public SmoothCoastersSmoothAnimation(SmoothCoastersAPI api) {
        this.api = api;
    }

    @Override
    public boolean isEnabled(Player player) {
        return api.isEnabled(player.getBukkitPlayer());
    }

    @Override
    public void clearRotation(Player player) {
        api.resetRotation(null, player.getBukkitPlayer());
    }

    @Override
    public void setRotation(Player player, Quaternion orientation) {
        float x = (float) orientation.getX();
        float y = (float) orientation.getY();
        float z = (float) orientation.getZ();
        float w = (float) orientation.getW();
        api.setRotation(null, player.getBukkitPlayer(), x, y, z, w, (byte)3);
    }
}
