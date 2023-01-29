package com.jverbruggen.jrides;

import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.command.MainCommandExecutor;
import com.jverbruggen.jrides.control.uiinterface.menu.event.ButtonClickEventListener;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.packets.listener.VirtualEntityPacketListener;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.serviceprovider.configuration.ServiceProviderConfigurator;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.player.PlayerManagerListener;
import com.jverbruggen.jrides.state.ride.RideManager;
import com.jverbruggen.jrides.state.ride.SoftEjector;
import com.jverbruggen.jrides.state.viewport.ViewportListener;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    private World world;

    @Override
    public void onEnable() {
        world = Bukkit.getWorld("world");

        JRidesPlugin.setBukkitPluginHost(this);
        ServiceProviderConfigurator.configure(this);
        JRidesPlugin.initOtherStatics();

        PlayerManager playerManager = ServiceProvider.getSingleton(PlayerManager.class);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerManagerListener(playerManager), this);
        pluginManager.registerEvents(new ViewportListener(
                ServiceProvider.getSingleton(ViewportManager.class),
                playerManager), this);
        pluginManager.registerEvents(new ButtonClickEventListener(), this);

        getServer().getPluginCommand("jrides").setExecutor(new MainCommandExecutor());

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

        Logger logger = ServiceProvider.getSingleton(Logger.class);
        logger.warning("JRides enabled");
    }

    @Override
    public void onDisable() {
        PlayerManager playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        ViewportManager viewportManager = ServiceProvider.getSingleton(ViewportManager.class);

        viewportManager.despawnAll();

        Logger logger = ServiceProvider.getSingleton(Logger.class);
        logger.warning("JRides disabled");
    }
}
