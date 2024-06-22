package com.jverbruggen.jrides.models.entity;

import org.bukkit.inventory.ItemStack;

public class TrainModelItem {
    private final ItemStack model;

    public TrainModelItem(ItemStack model) {
        this.model = model;
    }

    public ItemStack getItemStack() {
        return model;
    }
}
