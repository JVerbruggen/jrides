package com.jverbruggen.jrides.serviceprovider.configuration;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.models.entity.EntityIdFactory;
import com.jverbruggen.jrides.models.message.MessageFactory;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.PacketSender_1_19_2;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import com.jverbruggen.jrides.state.viewport.ViewportManagerFactory;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceProviderConfigurator {
    public static void configure(JavaPlugin plugin){
        File dataFolder = plugin.getDataFolder();

        Logger logger = plugin.getLogger();
        logger.setLevel(Level.CONFIG);
        ServiceProvider.Register(Logger.class, logger);

        ConfigManager configManager                 = ServiceProvider.Register(ConfigManager.class, new ConfigManager(plugin));
        ProtocolManager protocolManager             = ServiceProvider.Register(ProtocolManager.class, ProtocolLibrary.getProtocolManager());
        PacketSender packetSender                   = ServiceProvider.Register(PacketSender.class, new PacketSender_1_19_2(logger, protocolManager));
        EntityIdFactory entityIdFactory             = ServiceProvider.Register(EntityIdFactory.class, new EntityIdFactory(1_500_000, Integer.MAX_VALUE));
        MessageFactory messageFactory               = ServiceProvider.Register(MessageFactory.class, new MessageFactory(protocolManager));
        PlayerManager playerManager                 = ServiceProvider.Register(PlayerManager.class, new PlayerManager());
        ViewportManagerFactory viewportManagerFactory = new ViewportManagerFactory(packetSender, entityIdFactory);
        ViewportManager viewportManager             = ServiceProvider.Register(ViewportManager.class, viewportManagerFactory.createViewportManager(true));
        RideManager rideManager                     = ServiceProvider.Register(RideManager.class, new RideManager(logger, dataFolder, viewportManager, configManager));

    }
}
