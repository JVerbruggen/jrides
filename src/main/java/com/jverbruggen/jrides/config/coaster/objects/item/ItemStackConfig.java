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

package com.jverbruggen.jrides.config.coaster.objects.item;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class ItemStackConfig extends BaseConfig implements ItemConfig {
    private final Material material;
    private final int damage;
    private final boolean unbreakable;
    private final int customModelData;

    public ItemStackConfig(Material material, int damage, boolean unbreakable, int customModelData) {
        this.material = material;
        this.damage = damage;
        this.unbreakable = unbreakable;
        this.customModelData = customModelData;
    }

    public ItemStackConfig() {
        this.material = Material.STONE;
        this.damage = 0;
        this.unbreakable = false;
        this.customModelData = -1;
    }

    public Material getMaterial() {
        return material;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public ItemStack createItemStack(){
        return ItemStackFactory.getCoasterStackFromConfig(this);
    }

    @Override
    public VirtualEntity spawnEntity(ViewportManager viewportManager, Vector3 spawnPosition, Quaternion spawnRotation, String customName) {
        return viewportManager.spawnModelEntity(spawnPosition, spawnRotation, new TrainModelItem(createItemStack()), customName);
    }

    public static ItemStackConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        if(configurationSection == null) return new ItemStackConfig();

        Material material = Material.valueOf(getString(configurationSection, "material", "STONE"));
        int damage = getInt(configurationSection, "damage", 0);
        boolean unbreakable = getBoolean(configurationSection, "unbreakable", false);
        int customModelData = getInt(configurationSection, "customModelData", -1);
        return new ItemStackConfig(material, damage, unbreakable, customModelData);
    }
}
