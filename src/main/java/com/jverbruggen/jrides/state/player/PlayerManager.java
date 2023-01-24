package com.jverbruggen.jrides.state.player;

import com.jverbruggen.jrides.models.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private final HashMap<UUID, Player> players;

    public PlayerManager(){
        players = new HashMap<>();
    }

    public Player registerPlayer(org.bukkit.entity.Player bukkitPlayer){
        Player player = new Player(bukkitPlayer);

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
