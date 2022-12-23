package com.jverbruggen.jrides;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.models.entity.VirtualEntityFactory;
import com.jverbruggen.jrides.models.message.MessageFactory;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.PacketSender_1_19_2;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.modelmapper.ModelMapper;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        ServiceProvider.Register(Logger.class, getLogger());
        ServiceProvider.Register(ModelMapper.class, new ModelMapper());
        ServiceProvider.Register(ProtocolManager.class, ProtocolLibrary.getProtocolManager());
        ServiceProvider.Register(MessageFactory.class, sp -> new MessageFactory(sp.getSingleton(ProtocolManager.class)));
        ServiceProvider.Register(RideManager.class, new RideManager());
        ServiceProvider.Register(PacketSender.class,
                sp -> new PacketSender_1_19_2(sp.getSingleton(ProtocolManager.class)));
        ServiceProvider.Register(VirtualEntityFactory.class,
                sp -> new VirtualEntityFactory(sp.getSingleton(PacketSender.class)));

        Logger logger = ServiceProvider.GetSingleton(Logger.class);
        logger.warning("JRIDES ENABLED YE");
    }

    @Override
    public void onDisable() {
        Logger logger = ServiceProvider.GetSingleton(Logger.class);
        logger.warning("JRIDES DISABLED YE");
    }
}
