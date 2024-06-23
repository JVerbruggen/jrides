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

package com.jverbruggen.jrides.models.entity.armorstand;

import com.jverbruggen.jrides.models.entity.TrainModelItem;

public class ArmorstandModels {
    private TrainModelItem head, mainHand, offHand;

    public ArmorstandModels() {
        this.head = null;
        this.mainHand = null;
        this.offHand = null;
    }

    public void setHead(TrainModelItem head) {
        this.head = head;
    }
    public void setMainHand(TrainModelItem mainHand) {
        this.mainHand = mainHand;
    }
    public void setOffHand(TrainModelItem offHand) {
        this.offHand = offHand;
    }

    public boolean hasHead() { return head != null; }
    public boolean hasMainHand() { return mainHand != null; }
    public boolean hasOffHand() { return offHand != null; }

    public TrainModelItem getHead() {
        return head;
    }
    public TrainModelItem getMainHand() {
        return mainHand;
    }
    public TrainModelItem getOffHand() {
        return offHand;
    }
}
