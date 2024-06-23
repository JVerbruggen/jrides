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

package com.jverbruggen.jrides.models.ride.coaster.train;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.List;

public interface Vehicle {
    List<Player> getPassengers();
    void onPlayerEnter(Player player);
    void onPlayerExit(Player player);

    boolean isStationary();

    /**
     * @return true if restraints are closed
     */
    boolean getRestraintState();
    void setRestraintForAll(boolean closed);

    void ejectPassengers();

    void playRestraintOpenSound();

    void playRestraintCloseSound();

    void playDispatchSound();

    String getName();

    void setCrashed(boolean crashed);
    boolean isCrashed();
    Vector3 getCurrentLocation();
}
