package com.jverbruggen.jrides;

import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.command.JRidesCommandExecutor;
import com.jverbruggen.jrides.config.ride.RideConfig;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.packets.listener.VirtualEntityPacketListener;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.serviceprovider.configuration.ServiceProviderConfigurator;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.player.PlayerManagerListener;
import com.jverbruggen.jrides.state.ride.RideManager;
import com.jverbruggen.jrides.state.viewport.ViewportListener;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    private World world;

    @Override
    public void onEnable() {
        world = Bukkit.getWorld("world");

        JRidesPlugin.setBukkitPluginHost(this);
        ServiceProviderConfigurator.configure(this);
        JRidesPlugin.initOtherStatics();

        PlayerManager playerManager = ServiceProvider.GetSingleton(PlayerManager.class);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerManagerListener(playerManager), this);
        pluginManager.registerEvents(new ViewportListener(
                ServiceProvider.GetSingleton(ViewportManager.class),
                playerManager), this);

        getServer().getPluginCommand("jrides").setExecutor(
                new JRidesCommandExecutor(playerManager));

        RideManager rideManager = ServiceProvider.GetSingleton(RideManager.class);
        rideManager.initAllRides(world);

        ViewportManager viewportManager = ServiceProvider.GetSingleton(ViewportManager.class);

        for(org.bukkit.entity.Player bukkitPlayer : world.getPlayers()){
            Player player = playerManager.getPlayer(bukkitPlayer);
            getLogger().info("Initialising jrides for player " + bukkitPlayer.getName());
            viewportManager.updateVisuals(player);
        }

        VirtualEntityPacketListener packetListener = ServiceProvider.GetSingleton(VirtualEntityPacketListener.class);
        ProtocolManager protocolManager = ServiceProvider.GetSingleton(ProtocolManager.class);
        protocolManager.addPacketListener(packetListener);

        Logger logger = ServiceProvider.GetSingleton(Logger.class);
        logger.warning("JRides enabled");
    }

    @Override
    public void onDisable() {
        PlayerManager playerManager = ServiceProvider.GetSingleton(PlayerManager.class);
        ViewportManager viewportManager = ServiceProvider.GetSingleton(ViewportManager.class);

        viewportManager.despawnAll();

        Logger logger = ServiceProvider.GetSingleton(Logger.class);
        logger.warning("JRides disabled");
    }
}
