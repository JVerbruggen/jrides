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

package com.jverbruggen.jrides;

import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.api.PlayerAPI;
import com.jverbruggen.jrides.command.MainCommandExecutor;
import com.jverbruggen.jrides.common.MenuSessionManager;
import com.jverbruggen.jrides.common.startup.StartMessage;
import com.jverbruggen.jrides.config.ride.RideState;
import com.jverbruggen.jrides.control.uiinterface.menu.button.event.ButtonClickEventListener;
import com.jverbruggen.jrides.control.uiinterface.menu.open.SignMenuListener;
import com.jverbruggen.jrides.listener.PlayerTeleportToRideListener;
import com.jverbruggen.jrides.models.map.ridecounter.RideCounterMapListener;
import com.jverbruggen.jrides.models.entity.listener.PassengerListener;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordRideCollection;
import com.jverbruggen.jrides.state.player.BukkitPlayerJoinEventListener;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgentManagerListener;
import com.jverbruggen.jrides.models.map.rideoverview.RideOverviewMapListener;
import com.jverbruggen.jrides.models.ride.count.RideCounterListener;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecord;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecordCollection;
import com.jverbruggen.jrides.packets.listener.VirtualEntityPacketListener;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.serviceprovider.configuration.ServiceProviderConfigurator;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.player.PlayerManagerListener;
import com.jverbruggen.jrides.state.ride.RideCounterManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import com.jverbruggen.jrides.state.ride.SoftEjector;
import com.jverbruggen.jrides.state.ride.menu.RideMenuLoader;
import com.jverbruggen.jrides.state.viewport.ViewportListener;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        World world = Bukkit.getWorld("Lobby");
        JRidesPlugin.setWorld(world);

        JRidesPlugin.setBukkitPluginHost(this);
        ServiceProviderConfigurator.configure(this);
        JRidesPlugin.initOtherStatics();

        PlayerManager playerManager = ServiceProvider.getSingleton(PlayerManager.class);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BukkitPlayerJoinEventListener(), this);
        pluginManager.registerEvents(new PlayerManagerListener(playerManager), this);
        pluginManager.registerEvents(new ViewportListener(
                ServiceProvider.getSingleton(ViewportManager.class),
                playerManager), this);
        pluginManager.registerEvents(new ButtonClickEventListener(), this);
        pluginManager.registerEvents(new SignMenuListener("Control panel"), this);
        pluginManager.registerEvents(new RideCounterListener(), this);
        pluginManager.registerEvents(new RideOverviewMapListener(), this);
        pluginManager.registerEvents(new RideCounterMapListener(), this);
        pluginManager.registerEvents(new MessageAgentManagerListener(), this);
        pluginManager.registerEvents(new PlayerTeleportToRideListener(), this);
        pluginManager.registerEvents(new RideMenuLoader(), this);
        pluginManager.registerEvents(new PassengerListener(), this);

        Bukkit.getServicesManager().register(PlayerAPI.class, new PlayerAPI(), this, ServicePriority.Highest);

        ConfigurationSerialization.registerClass(RideCounterRecord.class);
        ConfigurationSerialization.registerClass(RideCounterRecordCollection.class);
        ConfigurationSerialization.registerClass(RideCounterRecordRideCollection.class);
        ConfigurationSerialization.registerClass(RideState.class);

        MainCommandExecutor commandExecutor = new MainCommandExecutor();
        getServer().getPluginCommand("jrides").setTabCompleter(commandExecutor);
        getServer().getPluginCommand("jrides").setExecutor(commandExecutor);

        RideManager rideManager = ServiceProvider.getSingleton(RideManager.class);
        rideManager.initAllRides(world);

        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        for(org.bukkit.entity.Player bukkitPlayer : world.getPlayers()){
            Player player = playerManager.getPlayer(bukkitPlayer);
            getLogger().info("Initialising jrides for player " + bukkitPlayer.getName());
            viewportManager.updateVisuals(player);
        }

        VirtualEntityPacketListener packetListener = ServiceProvider.getSingleton(VirtualEntityPacketListener.class);
        ProtocolManager protocolManager = ServiceProvider.getSingleton(ProtocolManager.class);
        protocolManager.addPacketListener(packetListener);

        SoftEjector.startClock(this);

        StartMessage.sendEnabledMessage();
    }

    @Override
    public void onDisable() {
        PlayerManager playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);
        RideManager rideManager = ServiceProvider.getSingleton(RideManager.class);
        RideCounterManager rideCounterManager = ServiceProvider.getSingleton(RideCounterManager.class);
        MenuSessionManager menuSessionManager = ServiceProvider.getSingleton(MenuSessionManager.class);

        for(Player player : playerManager.getPlayers()){
            player.clearSmoothAnimationRotation();
        }

        menuSessionManager.closeAllOpenMenus();
        rideManager.unloadAllRides();
        viewportManager.despawnAll();
        rideCounterManager.saveAndUnloadAll();

        StartMessage.sendDisabledMessage();
    }
}
