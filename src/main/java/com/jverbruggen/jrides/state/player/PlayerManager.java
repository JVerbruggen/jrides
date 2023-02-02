package com.jverbruggen.jrides.state.player;

import com.jverbruggen.jrides.models.entity.Player;

import java.util.*;

public class PlayerManager {
    private final HashMap<UUID, Player> players;
    private final List<Player> operators;

    public PlayerManager(){
        players = new HashMap<>();
        operators = new ArrayList<>();
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
        Player player = players.remove(uuid);
        if(isOperator(player)) unregisterOperator(player);
    }

    public Collection<Player> getPlayers(){
        return players.values();
    }

    public void registerOperator(Player player){
        operators.add(player);
    }

    public void unregisterOperator(Player player){
        operators.remove(player);
    }

    public boolean isOperator(Player player){
        return operators.contains(player);
    }

    public List<Player> getOperators(){
        return operators;
    }
}
