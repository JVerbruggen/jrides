/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

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
        int renderDistance = 100;
        int renderChunkSize = 8;

        if(globalMode){
            return new GlobalViewportManager(new GlobalViewport(renderDistance), packetSender, entityIdFactory, renderDistance, renderChunkSize);
        }else{
            return new SpecifiedViewportManager(renderDistance, renderChunkSize);
        }
    }
}
