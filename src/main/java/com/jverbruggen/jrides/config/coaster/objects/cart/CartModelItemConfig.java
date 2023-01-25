package com.jverbruggen.jrides.config.coaster.objects.cart;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class CartModelItemConfig{
    private final Material material;
    private final int damage;
    private final boolean unbreakable;

    public CartModelItemConfig(Material material, int damage, boolean unbreakable) {
        this.material = material;
        this.damage = damage;
        this.unbreakable = unbreakable;
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

    public static CartModelItemConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        Material material = Material.valueOf(configurationSection.getString("material"));
        int damage = configurationSection.getInt("damage");
        boolean unbreakable = configurationSection.getBoolean("unbreakable");
        return new CartModelItemConfig(material, damage, unbreakable);
    }
}
