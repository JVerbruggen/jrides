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

package com.jverbruggen.jrides.models.message;

import com.comphenix.protocol.ProtocolManager;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

public class MessageFactory {
    private ProtocolManager protocolManager;

    public MessageFactory() {
        this.protocolManager = ServiceProvider.getSingleton(ProtocolManager.class);
    }

    public Message getTitle(String title, String subtitle){
        return new TitleMessage(title, subtitle);
    }

    public Message getActionBarMessage(String contents){
        return new ActionBarMessage(protocolManager, contents);
    }
}
