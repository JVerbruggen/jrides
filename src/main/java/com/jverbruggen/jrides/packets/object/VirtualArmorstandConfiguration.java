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

package com.jverbruggen.jrides.packets.object;

import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandModels;
import com.jverbruggen.jrides.models.entity.armorstand.ArmorstandRotations;

public record VirtualArmorstandConfiguration(
        ArmorstandRotations rotations,
        ArmorstandModels models,
        boolean invisible,
        int leashedToEntity,
        String customName) {
    private static final int DEFAULT_LEASHED_TO_VALUE = -1;
    private static final boolean DEFAULT_INVISIBLE = true;

    public static VirtualArmorstandConfiguration createWithName(String customName){
        return new VirtualArmorstandConfiguration(new ArmorstandRotations(), new ArmorstandModels(), DEFAULT_INVISIBLE, DEFAULT_LEASHED_TO_VALUE, customName);
    }

    public static VirtualArmorstandConfiguration createDefault(){
        return new VirtualArmorstandConfiguration(new ArmorstandRotations(), new ArmorstandModels(), DEFAULT_INVISIBLE, DEFAULT_LEASHED_TO_VALUE, null);
    }
}
