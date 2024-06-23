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

package com.jverbruggen.jrides.config.flatride.structure;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.cart.ModelConfig;
import com.jverbruggen.jrides.config.flatride.structure.attachment.AttachmentConfig;

import java.util.List;

public abstract class AbstractStructureConfig extends BaseConfig implements StructureConfigItem {
    private final String identifier;
    private final boolean root;
    private final List<ModelConfig> flatRideModels;
    private final AttachmentConfig attachmentConfig;

    public AbstractStructureConfig(String identifier, boolean root, List<ModelConfig> flatRideModels, AttachmentConfig attachmentConfig) {
        this.identifier = identifier;
        this.root = root;
        this.flatRideModels = flatRideModels;
        this.attachmentConfig = attachmentConfig;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public boolean isRoot() {
        return root;
    }

    public AttachmentConfig getAttachmentConfig() {
        return attachmentConfig;
    }

    public List<ModelConfig> getFlatRideModels() {
        return flatRideModels;
    }
}
