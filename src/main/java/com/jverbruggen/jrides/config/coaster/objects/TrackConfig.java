package com.jverbruggen.jrides.config.coaster.objects;

import com.jverbruggen.jrides.config.coaster.objects.section.SectionConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TrackConfig {
    private final String mode;
    private final List<Float> position;
    private final int offset;
    private final List<SectionConfig> sections;

    public TrackConfig(String mode, List<Float> position, int offset, List<SectionConfig> sections) {
        this.mode = mode;
        this.position = position;
        this.offset = offset;
        this.sections = sections;
    }

    public String getMode() {
        return mode;
    }

    public List<Float> getPosition() {
        return position;
    }

    public int getOffset() {
        return offset;
    }

    public List<SectionConfig> getSections() {
        return sections;
    }

    public static TrackConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        String mode = configurationSection.getString("mode");
        List<Float> position = configurationSection.getFloatList("position");
        int offset = configurationSection.getInt("offset");

        List<SectionConfig> sections = new ArrayList<>();
        ConfigurationSection configurationSectionSections = configurationSection.getConfigurationSection("sections");
        Set<String> sectionsKeys = configurationSectionSections.getKeys(false);
        for(String sectionKey : sectionsKeys){
            ConfigurationSection sectionSpec = configurationSectionSections.getConfigurationSection(sectionKey);

            assert sectionSpec != null;
            sections.add(SectionConfig.fromConfigurationSection(sectionSpec, sectionKey));
        }

        return new TrackConfig(mode, position, offset, sections);
    }
}

