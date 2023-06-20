package com.jverbruggen.jrides.config.coaster.objects.item;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public class ItemConfig {
    private final Material material;
    private final int damage;
    private final boolean unbreakable;

    public ItemConfig(Material material, int damage, boolean unbreakable) {
        this.material = material;
        this.damage = damage;
        this.unbreakable = unbreakable;
    }

    public ItemConfig() {
        this.material = Material.STONE;
        this.damage = 0;
        this.unbreakable = false;
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

    public static ItemConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        if(configurationSection == null) return new ItemConfig();

        Material material = Material.valueOf(configurationSection.getString("material"));
        int damage = configurationSection.getInt("damage");
        boolean unbreakable = configurationSection.getBoolean("unbreakable");
        return new ItemConfig(material, damage, unbreakable);
    }
}
