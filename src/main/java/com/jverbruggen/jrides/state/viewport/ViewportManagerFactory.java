package com.jverbruggen.jrides.state.viewport;

import com.jverbruggen.jrides.models.entity.EntityIdFactory;
import com.jverbruggen.jrides.models.render.GlobalViewport;
import com.jverbruggen.jrides.packets.PacketSender;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

public class ViewportManagerFactory {
    private final PacketSender packetSender;
    private final EntityIdFactory entityIdFactory;

    public ViewportManagerFactory() {
        this.packetSender = ServiceProvider.getSingleton(PacketSender.class);
        this.entityIdFactory = ServiceProvider.getSingleton(EntityIdFactory.class);
    }

    public ViewportManager createViewportManager(boolean globalMode){
        if(globalMode){
            return new GlobalViewportManager(new GlobalViewport(), packetSender, entityIdFactory);
        }else{
            return new SpecifiedViewportManager();
        }
    }
}
