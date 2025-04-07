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

package com.jverbruggen.jrides.models.menu.lore;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.language.LanguageFileTag;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.ride.Ride;
import com.jverbruggen.jrides.models.ride.count.RideCounterRecord;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;

public class RideCounterLore implements Lore{
    private final RideHandle rideHandle;

    public RideCounterLore(RideHandle rideHandle) {
        this.rideHandle = rideHandle;
    }

    @Override
    public String resolveLore(JRidesPlayer player) {
        Ride ride = rideHandle.getRide();
        Player actualPlayer = ServiceProvider.getSingleton(PlayerManager.class).getPlayer(player.getBukkitPlayer());

        RideCounterRecord rideCounterRecord = actualPlayer.getRideCounters().findOrCreate(ride.getIdentifier(), player);
        return JRidesPlugin.getLanguageFile()
                .get(LanguageFileField.MENU_RIDE_COUNTER_INDICATOR,
                        b -> b.add(LanguageFileTag.rideDisplayName, ride.getDisplayName())
                                .add(LanguageFileTag.rideCount, "" + rideCounterRecord.getRideCount()));
    }
}
