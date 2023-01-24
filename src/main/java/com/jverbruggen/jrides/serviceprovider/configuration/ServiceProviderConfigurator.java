package com.jverbruggen.jrides.serviceprovider.configuration;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimation;
import com.jverbruggen.jrides.animator.smoothanimation.SmoothCoastersSmoothAnimation;
import com.jverbruggen.jrides.animator.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.models.entity.EntityIdFactory;
import com.jverbruggen.jrides.models.message.MessageFactory;
import com.jverbruggen.jrides.models.ride.factory.SeatFactory;
import com.jverbruggen.jrides.models.ride.factory.TrackFactory;
import com.jverbruggen.jrides.models.ride.factory.TrainFactory;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.PacketSender_1_19_2;
import com.jverbruggen.jrides.packets.listener.VirtualEntityPacketListener;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import com.jverbruggen.jrides.state.viewport.ViewportManagerFactory;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import me.m56738.smoothcoasters.api.SmoothCoastersAPI;
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

        SmoothAnimation smoothAnimation             = ServiceProvider.Register(SmoothAnimation.class, new SmoothCoastersSmoothAnimation(new SmoothCoastersAPI(plugin)));
        ConfigManager configManager                 = ServiceProvider.Register(ConfigManager.class, new ConfigManager(plugin));
        ProtocolManager protocolManager             = ServiceProvider.Register(ProtocolManager.class, ProtocolLibrary.getProtocolManager());
        PacketSender packetSender                   = ServiceProvider.Register(PacketSender.class, new PacketSender_1_19_2(logger, protocolManager));
        EntityIdFactory entityIdFactory             = ServiceProvider.Register(EntityIdFactory.class, new EntityIdFactory(1_500_000, Integer.MAX_VALUE));
        MessageFactory messageFactory               = ServiceProvider.Register(MessageFactory.class, new MessageFactory(protocolManager));
        PlayerManager playerManager                 = ServiceProvider.Register(PlayerManager.class, new PlayerManager());
        ViewportManagerFactory viewportManagerFactory = new ViewportManagerFactory(packetSender, entityIdFactory);
        ViewportManager viewportManager             = ServiceProvider.Register(ViewportManager.class, viewportManagerFactory.createViewportManager(true));
        SeatFactory seatFactory                     = ServiceProvider.Register(SeatFactory.class, new SeatFactory(viewportManager));
        TrainFactory trainFactory                   = ServiceProvider.Register(TrainFactory.class, new TrainFactory(viewportManager, seatFactory));
        CartMovementFactory cartMovementFactory     = ServiceProvider.Register(CartMovementFactory.class, new CartMovementFactory());
        TrackBehaviourFactory trackBehaviourFactory = ServiceProvider.Register(TrackBehaviourFactory.class, new TrackBehaviourFactory(cartMovementFactory));
        TrackFactory trackFactory                   = ServiceProvider.Register(TrackFactory.class, new TrackFactory(trackBehaviourFactory));
        RideManager rideManager                     = ServiceProvider.Register(RideManager.class, new RideManager(logger, dataFolder, viewportManager, configManager, trainFactory, trackFactory, seatFactory));
        VirtualEntityPacketListener packetListener  = ServiceProvider.Register(VirtualEntityPacketListener.class,
                new VirtualEntityPacketListener(plugin, ListenerPriority.NORMAL, new PacketType[]{
                            PacketType.Play.Client.USE_ENTITY,
                            PacketType.Play.Client.STEER_VEHICLE
                        },
                        viewportManager, playerManager, smoothAnimation));
    }
}
