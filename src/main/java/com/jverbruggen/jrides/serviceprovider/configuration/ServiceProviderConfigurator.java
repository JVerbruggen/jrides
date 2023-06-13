package com.jverbruggen.jrides.serviceprovider.configuration;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.animator.smoothanimation.SmoothAnimation;
import com.jverbruggen.jrides.animator.smoothanimation.SmoothCoastersSmoothAnimation;
import com.jverbruggen.jrides.animator.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.animator.trackbehaviour.result.CartMovementFactory;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.config.trigger.TriggerConfigFactory;
import com.jverbruggen.jrides.control.controller.RideControllerFactory;
import com.jverbruggen.jrides.control.controlmode.factory.ControlModeFactory;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.control.uiinterface.menu.button.controller.ButtonUpdateController;
import com.jverbruggen.jrides.control.uiinterface.menu.button.factory.RideControlButtonFactory;
import com.jverbruggen.jrides.effect.EffectTriggerFactory;
import com.jverbruggen.jrides.effect.cart.rotation.CartRotationTriggerFactory;
import com.jverbruggen.jrides.effect.train.music.MusicEffectTriggerFactory;
import com.jverbruggen.jrides.effect.platform.MultiArmorstandMovementEffectTriggerFactory;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.models.entity.EntityIdFactory;
import com.jverbruggen.jrides.models.entity.agent.MessageAgentManager;
import com.jverbruggen.jrides.models.map.rideoverview.RideOverviewMapFactory;
import com.jverbruggen.jrides.models.map.rideoverview.SectionVisualFactory;
import com.jverbruggen.jrides.models.map.rideoverview.TrainVisualFactory;
import com.jverbruggen.jrides.models.message.MessageFactory;
import com.jverbruggen.jrides.models.properties.frame.factory.FrameFactory;
import com.jverbruggen.jrides.models.ride.factory.*;
import com.jverbruggen.jrides.models.ride.section.provider.SectionProvider;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.packets.PacketSenderFactory;
import com.jverbruggen.jrides.packets.PacketSender_1_19_2;
import com.jverbruggen.jrides.packets.listener.VirtualEntityPacketListener;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideCounterManager;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import com.jverbruggen.jrides.state.viewport.ViewportManagerFactory;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import me.m56738.smoothcoasters.api.SmoothCoastersAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceProviderConfigurator {
    public static void configure(JavaPlugin plugin){
        File dataFolder = plugin.getDataFolder();

        Logger logger = plugin.getLogger();
        logger.setLevel(Level.CONFIG);
        ServiceProvider.register(Logger.class, logger);
        ServiceProvider.register(JRidesLogger.class, new JRidesLogger(logger, true));

        ServiceProvider.register(PluginManager.class, Bukkit.getPluginManager());
        ServiceProvider.register(ConfigManager.class, new ConfigManager(plugin));
        ServiceProvider.register(LanguageFile.class, new LanguageFile());

        ServiceProvider.register(MessageAgentManager.class, new MessageAgentManager());
        ServiceProvider.register(RideCounterManager.class, new RideCounterManager());
        ServiceProvider.register(ControlModeFactory.class, new ControlModeFactory());
        ServiceProvider.register(RideControllerFactory.class, new RideControllerFactory());
        ServiceProvider.register(RideControlButtonFactory.class, new RideControlButtonFactory());
        ServiceProvider.register(RideControlMenuFactory.class, new RideControlMenuFactory());
        ServiceProvider.register(SectionProvider.class, new SectionProvider());
        ServiceProvider.register(ButtonUpdateController.class, new ButtonUpdateController());
        ServiceProvider.register(ProtocolManager.class, ProtocolLibrary.getProtocolManager());
        ServiceProvider.register(EntityIdFactory.class, new EntityIdFactory(1_500_000, Integer.MAX_VALUE));
        ServiceProvider.register(PacketSender.class, PacketSenderFactory.getPacketSender());
        ServiceProvider.register(ViewportManager.class, new ViewportManagerFactory().createViewportManager(true));
        ServiceProvider.register(FrameFactory.class, new FrameFactory());
        ServiceProvider.register(SmoothAnimation.class, new SmoothCoastersSmoothAnimation(new SmoothCoastersAPI(plugin)));
        ServiceProvider.register(TriggerConfigFactory.class, new TriggerConfigFactory());
        ServiceProvider.register(MultiArmorstandMovementEffectTriggerFactory.class, new MultiArmorstandMovementEffectTriggerFactory());
        ServiceProvider.register(MusicEffectTriggerFactory.class, new MusicEffectTriggerFactory());
        ServiceProvider.register(CartRotationTriggerFactory.class, new CartRotationTriggerFactory());
        ServiceProvider.register(EffectTriggerFactory.class, new EffectTriggerFactory());
        ServiceProvider.register(MessageFactory.class, new MessageFactory());
        ServiceProvider.register(PlayerManager.class, new PlayerManager());
        ServiceProvider.register(SeatFactory.class, new SeatFactory());
        ServiceProvider.register(TrainFactory.class, new TrainFactory());
        ServiceProvider.register(CartMovementFactory.class, new CartMovementFactory());
        ServiceProvider.register(TrackBehaviourFactory.class, new TrackBehaviourFactory());
        ServiceProvider.register(RideManager.class, new RideManager(dataFolder));
        ServiceProvider.register(SectionVisualFactory.class, new SectionVisualFactory());
        ServiceProvider.register(TrainVisualFactory.class, new TrainVisualFactory());
        ServiceProvider.register(RideOverviewMapFactory.class, new RideOverviewMapFactory());

        ServiceProvider.register(VirtualEntityPacketListener.class, new VirtualEntityPacketListener());
    }
}
