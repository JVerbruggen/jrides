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

package com.jverbruggen.jrides.config.flatride.structure.attachment;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.math.Vector3;

public abstract class AbstractRelativeMultipleAttachmentConfig extends BaseConfig implements AttachmentConfig {
    private final String toComponentIdentifier;
    private final Vector3 offsetPosition;
    private final int amount;

    protected AbstractRelativeMultipleAttachmentConfig(String toComponentIdentifier, Vector3 offsetPosition, int amount) {
        this.toComponentIdentifier = toComponentIdentifier;
        this.offsetPosition = offsetPosition;
        this.amount = amount;
    }

    public String getToComponentIdentifier() {
        return toComponentIdentifier;
    }

    public Vector3 getOffsetPosition() {
        return offsetPosition;
    }

    public int getAmount() {
        return amount;
    }

}
