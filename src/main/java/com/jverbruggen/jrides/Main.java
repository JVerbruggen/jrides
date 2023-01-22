package com.jverbruggen.jrides;

import com.jverbruggen.jrides.command.JRidesCommandExecutor;
import com.jverbruggen.jrides.config.ride.RideConfig;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.serviceprovider.configuration.ServiceProviderConfigurator;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.player.PlayerManagerListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        JRidesPlugin.setBukkitPluginHost(this);
        ServiceProviderConfigurator.configure(this);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerManagerListener(ServiceProvider.GetSingleton(PlayerManager.class)), this);

        getServer().getPluginCommand("jrides").setExecutor(
                new JRidesCommandExecutor(ServiceProvider.GetSingleton(PlayerManager.class)));

        Logger logger = ServiceProvider.GetSingleton(Logger.class);
        logger.warning("JRides enabled");

        World world = Bukkit.getWorld("world");
        ServiceProvider.GetSingleton(RideConfig.class).initAllRides(world);
    }

    @Override
    public void onDisable() {
        Logger logger = ServiceProvider.GetSingleton(Logger.class);
        logger.warning("JRides disabled");
    }
}
