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

package com.jverbruggen.jrides.state.player;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideCounterManager;
import org.bukkit.Bukkit;

import java.util.*;

public class PlayerManager {
    private final HashMap<UUID, Player> players;
    private final List<Player> operators;
    private final RideCounterManager rideCounterManager;

    public PlayerManager(){
        players = new HashMap<>();
        operators = new ArrayList<>();
        rideCounterManager = ServiceProvider.getSingleton(RideCounterManager.class);
    }

    public Player registerPlayer(org.bukkit.entity.Player bukkitPlayer){
        RideCounterRecordCollection rideCounters = rideCounterManager.getCollection(bukkitPlayer.getUniqueId().toString());

        Player player = new Player(bukkitPlayer, rideCounters);

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

    public Player fromIdentifier(String playerIdentifier){
        UUID uuid = UUID.fromString(playerIdentifier);
        org.bukkit.entity.Player bukkitPlayer = Bukkit.getPlayer(uuid);
        if(bukkitPlayer == null) return null;

        return getPlayer(bukkitPlayer);
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
