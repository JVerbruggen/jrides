package com.jverbruggen.jrides.models.entity.armorstand;

import com.jverbruggen.jrides.models.entity.TrainModelItem;

public class ArmorstandModels {
    private TrainModelItem head, mainHand, offHand;

    public void setHead(TrainModelItem head) {
        this.head = head;
    }

    public void setMainHand(TrainModelItem mainHand) {
        this.mainHand = mainHand;
    }

    public void setOffHand(TrainModelItem offHand) {
        this.offHand = offHand;
    }

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
