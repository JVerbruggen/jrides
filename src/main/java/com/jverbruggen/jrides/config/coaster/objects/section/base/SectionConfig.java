package com.jverbruggen.jrides.config.coaster.objects.section.base;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.BaseConfig;
import com.jverbruggen.jrides.models.ride.factory.track.TrackDescription;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public abstract class SectionConfig extends BaseConfig {
    public abstract SectionReference build(TrackBehaviourFactory trackBehaviourFactory, List<TrackDescription> trackDescriptions, CoasterHandle coasterHandle,
                                           CoasterConfig coasterConfig);

    public static SectionConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        String type = getString(configurationSection, "type");

        if(RangedSectionConfig.accepts(type)) return RangedSectionConfig.fromConfigurationSection(configurationSection, sectionIdentifier);
        else if(PointSectionConfig.accepts(type)) return PointSectionConfig.fromConfigurationSection(configurationSection, sectionIdentifier);

        throw new RuntimeException("Type " + type + " is not accepted as a real section type");
    }
}
