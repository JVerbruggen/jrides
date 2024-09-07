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

package com.jverbruggen.jrides.packets.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.UUID;

public class VirtualEntityPacketListener_1_20_4 extends PacketAdapter implements VirtualEntityPacketListener, Listener {
    private final ViewportManager viewportManager;
    private final PlayerManager playerManager;
    private final VirtualEntityPacketUtils virtualEntityPacketUtils;

    public VirtualEntityPacketListener_1_20_4() {
        super(JRidesPlugin.getBukkitPlugin(), ListenerPriority.NORMAL,
                PacketType.Play.Client.USE_ENTITY,
                PacketType.Play.Client.STEER_VEHICLE,
                PacketType.Play.Server.SPAWN_ENTITY);
        this.viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        this.virtualEntityPacketUtils = ServiceProvider.getSingleton(VirtualEntityPacketUtils.class);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketType packetType = event.getPacketType();
        if (packetType.equals(PacketType.Play.Client.USE_ENTITY)) {
            onUseEntity(event);
        } else if (packetType.equals(PacketType.Play.Client.STEER_VEHICLE)) {
            onSteerVehicle(event);
        }
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketType packetType = event.getPacketType();
        if(packetType == PacketType.Play.Server.SPAWN_ENTITY){
            onSendingSpawnPlayerPacket(event);
        }
    }

    private void onUseEntity(PacketEvent event) {
        int entityId = event.getPacket().getIntegers().read(0);
        VirtualEntity entity = viewportManager.getEntity(entityId);
        org.bukkit.entity.Player bukkitPlayer = event.getPlayer();

        if (entity == null) {
            return;
        }
        Player player = playerManager.getPlayer(bukkitPlayer);

        virtualEntityPacketUtils.onUseEntity(entity, player);
    }

    private void onSteerVehicle(PacketEvent event) {
        boolean dismountVehicle = event.getPacket().getBooleans().read(1);

        org.bukkit.entity.Player bukkitPlayer = event.getPlayer();
        Player player = playerManager.getPlayer(bukkitPlayer);

        float sidewaysLeft = event.getPacket().getFloat().read(0); // Left is positive, right is negative
        float forwards = event.getPacket().getFloat().read(1);
        boolean jump = event.getPacket().getBooleans().read(0);

        virtualEntityPacketUtils.onSteerVehicle(player, sidewaysLeft, forwards, jump, dismountVehicle);
    }

    private void onSendingSpawnPlayerPacket(PacketEvent event){
        UUID uuid = event.getPacket().getUUIDs().read(0);

        org.bukkit.entity.Player bukkitPlayer = Bukkit.getPlayer(uuid);
        if(bukkitPlayer == null) return;
        Player player = playerManager.getPlayer(bukkitPlayer);
        Player receiver = playerManager.getPlayer(event.getPlayer());

        virtualEntityPacketUtils.onSendingSpawnPlayerPacket(player, receiver);
    }

    @Override
    public String getIdentifier() {
        return "1.19.2";
    }
}
