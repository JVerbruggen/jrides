package com.jverbruggen.jrides.config.trigger.entity;

import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.config.coaster.objects.item.ItemStackConfig;
import com.jverbruggen.jrides.effect.entity.EntityMovementTrigger;
import com.jverbruggen.jrides.effect.entity.ReplaceItemEffectTrigger;
import com.jverbruggen.jrides.models.entity.TrainModelItem;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.math.Vector3;
import org.bukkit.configuration.ConfigurationSection;

public class ReplaceItemConfig extends BaseConfig implements EntityMovementConfig {
    private final ItemStackConfig itemStackConfig;
    private final int delayTicks;

    public ReplaceItemConfig(ItemStackConfig itemStackConfig, int delayTicks) {
        this.itemStackConfig = itemStackConfig;
        this.delayTicks = delayTicks;
    }

    @Override
    public EntityMovementTrigger createTrigger(VirtualEntity virtualEntity) {
        TrainModelItem trainModelItem = new TrainModelItem(itemStackConfig.createItemStack());
        return new ReplaceItemEffectTrigger(trainModelItem, virtualEntity, delayTicks);
    }

    @Override
    public Vector3 getInitialPosition() {
        return null;
    }

    @Override
    public Vector3 getInitialRotation() {
        return null;
    }

    public static ReplaceItemConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        ItemStackConfig itemStackConfig = ItemStackConfig.fromConfigurationSection(configurationSection.getConfigurationSection("item"));
        int delayTicks = getInt(configurationSection, "delayTicks", 0);

        return new ReplaceItemConfig(itemStackConfig, delayTicks);
    }
}
