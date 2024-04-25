package com.jverbruggen.jrides.config.coaster.objects.item;

import com.jverbruggen.jrides.items.ItemStackFactory;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.state.viewport.ViewportManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class ItemStackConfig implements ItemConfig {
    private final Material material;
    private final int damage;
    private final boolean unbreakable;

    public ItemStackConfig(Material material, int damage, boolean unbreakable) {
        this.material = material;
        this.damage = damage;
        this.unbreakable = unbreakable;
    }

    public ItemStackConfig() {
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

    public ItemStack createItemStack(){
        return ItemStackFactory.getCoasterStackFromConfig(this);
    }

    @Override
    public VirtualEntity spawnEntity(ViewportManager viewportManager, Vector3 spawnPosition, String customName) {
        return viewportManager.spawnModelEntity(spawnPosition, new TrainModelItem(createItemStack()), customName);
    }

    public static ItemStackConfig fromConfigurationSection(@Nullable ConfigurationSection configurationSection) {
        if(configurationSection == null) return new ItemStackConfig();

        Material material = Material.valueOf(configurationSection.getString("material"));
        int damage = configurationSection.getInt("damage", 0);
        boolean unbreakable = configurationSection.getBoolean("unbreakable", false);
        return new ItemStackConfig(material, damage, unbreakable);
    }
}
