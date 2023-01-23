package com.jverbruggen.jrides.state.player;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimation;
import com.jverbruggen.jrides.models.entity.Player;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private final SmoothAnimation smoothAnimation;
    private final HashMap<UUID, Player> players;

    public PlayerManager(SmoothAnimation smoothAnimation){
        this.smoothAnimation = smoothAnimation;
        players = new HashMap<>();
    }

    public Player registerPlayer(org.bukkit.entity.Player bukkitPlayer){
        Player player = new Player(bukkitPlayer);

        Bukkit.getScheduler().runTaskLater(
                JRidesPlugin.getBukkitPlugin(),
                () -> player.setSmoothAnimationSupport(smoothAnimation.isEnabled(player)), 20L);

        UUID uuid = bukkitPlayer.getUniqueId();
        players.put(uuid, player);
        return player;
    }

    public Player getPlayer(org.bukkit.entity.Player bukkitPlayer){
        UUID uuid = bukkitPlayer.getUniqueId();
        if(!players.containsKey(uuid)){
            return registerPlayer(bukkitPlayer);
        }
        return players.get(uuid);
    }

    public void removePlayer(org.bukkit.entity.Player bukkitPlayer){
        UUID uuid = bukkitPlayer.getUniqueId();
        if(!players.containsKey(uuid)){
            return;
        }
        players.remove(uuid);
    }
}
