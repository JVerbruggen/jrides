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

package com.jverbruggen.jrides.models.ride.section.result;

import com.jverbruggen.jrides.models.ride.coaster.train.Train;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public record BlockSectionSafetyResult(boolean safe, @Nullable Train forTrain, @Nonnull String reason) {
    public static BlockSectionSafetyResult emptyFalse(){
        return new BlockSectionSafetyResult(false, null, "");
    }

    @Override
    public String reason() {
        String trainName = forTrain != null ? forTrain.getName() : "null";
        String sections = forTrain != null ? String.join(",", forTrain.getCurrentSections().stream().map(Object::toString).toList()) : "null";
        return "(" + trainName + ") [" + sections + "] " + reason;
    }

    @Override
    public String toString() {
        return reason();
    }
}
