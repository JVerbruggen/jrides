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
