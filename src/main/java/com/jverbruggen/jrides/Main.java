package com.jverbruggen.jrides;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.models.entity.EntityIdFactory;
import com.jverbruggen.jrides.models.entity.VirtualEntityFactory;
import com.jverbruggen.jrides.models.message.MessageFactory;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.PacketSender_1_19_2;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        ServiceProvider.Register(Logger.class, getLogger());
        ServiceProvider.Register(ConfigManager.class, new ConfigManager(this));
        ServiceProvider.Register(ProtocolManager.class, ProtocolLibrary.getProtocolManager());
        ServiceProvider.Register(EntityIdFactory.class, sp -> new EntityIdFactory(1_500_000, Integer.MAX_VALUE));
        ServiceProvider.Register(MessageFactory.class, sp -> new MessageFactory(sp.getSingleton(ProtocolManager.class)));
        ServiceProvider.Register(RideManager.class, new RideManager());
        ServiceProvider.Register(PacketSender.class,
                sp -> new PacketSender_1_19_2(sp.getSingleton(ProtocolManager.class)));
        ServiceProvider.Register(VirtualEntityFactory.class,
                sp -> new VirtualEntityFactory(
                        sp.getSingleton(PacketSender.class),
                        sp.getSingleton(EntityIdFactory.class)));

        Logger logger = ServiceProvider.GetSingleton(Logger.class);
        logger.warning("JRIDES ENABLED YE");
    }

    @Override
    public void onDisable() {
        Logger logger = ServiceProvider.GetSingleton(Logger.class);
        logger.warning("JRIDES DISABLED YE");
    }
}
