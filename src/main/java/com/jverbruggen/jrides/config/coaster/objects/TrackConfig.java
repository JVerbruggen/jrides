package com.jverbruggen.jrides.config.coaster.objects;

import com.jverbruggen.jrides.config.coaster.objects.section.base.RangedSectionConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.base.SectionConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TrackConfig {
    private final List<Float> position;
    private final List<SectionConfig> sections;
    private final List<String> parts;

    public TrackConfig(List<Float> position, List<SectionConfig> sections, List<String> parts) {
        this.position = position;
        this.sections = sections;
        this.parts = parts;
    }

    public List<Float> getPosition() {
        return position;
    }

    public List<SectionConfig> getSections() {
        return sections;
    }

    public List<String> getParts() {
        return parts;
    }

    public static TrackConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        List<Float> position = configurationSection.getFloatList("position");
        List<String> parts = configurationSection.getStringList("parts");

        List<SectionConfig> sections = new ArrayList<>();
        ConfigurationSection configurationSectionSections = configurationSection.getConfigurationSection("sections");
        Set<String> sectionsKeys = configurationSectionSections.getKeys(false);
        for(String sectionKey : sectionsKeys){
            ConfigurationSection sectionSpec = configurationSectionSections.getConfigurationSection(sectionKey);

            String track = "spline";
            assert sectionSpec != null;
            if(sectionSpec.contains("track")){
                track = sectionSpec.getString("track");
            }

            assert track != null;
            if(track.equalsIgnoreCase("spline")){
                sections.add(RangedSectionConfig.fromConfigurationSection(sectionSpec, sectionKey));
            }else if(track.equalsIgnoreCase("straight")){
                throw new RuntimeException("Straight track not implemented yet");
            }

        }

        return new TrackConfig(position, sections, parts);
    }
}

