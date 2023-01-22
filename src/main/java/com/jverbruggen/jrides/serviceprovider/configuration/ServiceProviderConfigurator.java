package com.jverbruggen.jrides.serviceprovider.configuration;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.ride.RideConfig;
import com.jverbruggen.jrides.models.entity.EntityIdFactory;
import com.jverbruggen.jrides.models.entity.VirtualEntityFactory;
import com.jverbruggen.jrides.models.message.MessageFactory;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.PacketSender_1_19_2;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class ServiceProviderConfigurator {
    public static void configure(JavaPlugin plugin){
        File dataFolder = plugin.getDataFolder();

        Logger logger                               = ServiceProvider.Register(Logger.class, plugin.getLogger());
        ConfigManager configManager                 = ServiceProvider.Register(ConfigManager.class, new ConfigManager(plugin));
        ProtocolManager protocolManager             = ServiceProvider.Register(ProtocolManager.class, ProtocolLibrary.getProtocolManager());
        PacketSender packetSender                   = ServiceProvider.Register(PacketSender.class, new PacketSender_1_19_2(protocolManager));
        EntityIdFactory entityIdFactory             = ServiceProvider.Register(EntityIdFactory.class, new EntityIdFactory(1_500_000, Integer.MAX_VALUE));
        MessageFactory messageFactory               = ServiceProvider.Register(MessageFactory.class, new MessageFactory(protocolManager));
        PlayerManager playerManager                 = ServiceProvider.Register(PlayerManager.class, new PlayerManager());
        RideManager rideManager                     = ServiceProvider.Register(RideManager.class, new RideManager());
        RideConfig rideConfig                       = ServiceProvider.Register(RideConfig.class, new RideConfig(dataFolder, rideManager));
        VirtualEntityFactory virtualEntityFactory   = ServiceProvider.Register(VirtualEntityFactory.class, new VirtualEntityFactory(
                                                        packetSender,
                                                        entityIdFactory));

    }
}
