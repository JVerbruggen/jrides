package com.jverbruggen.jrides.config.coaster.objects.section;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class SectionConfig {
    private final int lowerRange;
    private final int upperRange;
    private final String type;
    private final BlockSectionSpecConfig blockSectionSpec;

    public SectionConfig(int lowerRange, int upperRange, String type, BlockSectionSpecConfig blockSectionSpec) {
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
        this.type = type;
        this.blockSectionSpec = blockSectionSpec;
    }

    public int getLowerRange() {
        return lowerRange;
    }

    public int getUpperRange() {
        return upperRange;
    }

    public String getType() {
        return type;
    }

    public BlockSectionSpecConfig getBlockSectionSpec() {
        return blockSectionSpec;
    }

    public static SectionConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        List<Integer> range = configurationSection.getIntegerList("range");
        int lowerRange = range.get(0);
        int upperRange = range.get(1);
        String type = configurationSection.getString("type");

        BlockSectionSpecConfig blockSectionSpec = null;
        if(configurationSection.contains("blockSectionSpec"))
            blockSectionSpec = BlockSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("blockSectionSpec"));

        return new SectionConfig(lowerRange, upperRange, type, blockSectionSpec);
    }
}
