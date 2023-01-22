package com.jverbruggen.jrides.state.viewport;

import com.jverbruggen.jrides.models.entity.EntityIdFactory;
import com.jverbruggen.jrides.models.render.GlobalViewport;
import com.jverbruggen.jrides.packets.PacketSender;

public class ViewportManagerFactory {
    private final PacketSender packetSender;
    private final EntityIdFactory entityIdFactory;

    public ViewportManagerFactory(PacketSender packetSender, EntityIdFactory entityIdFactory) {
        this.packetSender = packetSender;
        this.entityIdFactory = entityIdFactory;
    }

    public ViewportManager createViewportManager(boolean globalMode){
        if(globalMode){
            return new GlobalViewportManager(new GlobalViewport(), packetSender, entityIdFactory);
        }else{
            return new SpecifiedViewportManager();
        }
    }
}
