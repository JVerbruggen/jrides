package com.jverbruggen.jrides.config.coaster.objects.section.base;

import com.jverbruggen.jrides.animator.coaster.CoasterHandle;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.coaster.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.point.SwitchSectionSpecConfig;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.SimpleFrame;
import com.jverbruggen.jrides.models.ride.factory.track.TrackDescription;
import com.jverbruggen.jrides.models.ride.section.reference.PointSectionReference;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class PointSectionConfig extends SectionConfig {
    private final int point;
    private final String trackSource;
    private final String nextSection;
    private final SwitchSectionSpecConfig switchSectionSpecConfig;

    public PointSectionConfig(String identifier, int point, String trackSource, String type, String nextSection, SwitchSectionSpecConfig switchSectionSpecConfig) {
        super(type, identifier);
        this.point = point;
        this.trackSource = trackSource;
        this.nextSection = nextSection;
        this.switchSectionSpecConfig = switchSectionSpecConfig;
    }

    public int getPoint() {
        return point;
    }

    public String getNextSection() {
        return nextSection;
    }

    public String getParentTrackIdentifier() {
        return trackSource;
    }

    public SwitchSectionSpecConfig getSwitchSectionSpecConfig() {
        return switchSectionSpecConfig;
    }

    public static boolean accepts(String type){
        return switch (type) {
            case "switch" -> true;
            default -> false;
        };
    }

    public static PointSectionConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        List<?> range = configurationSection.getList("at");
        if(range == null) throw new RuntimeException("Point section " + sectionIdentifier + " requires 'at' keyword to define position of point section");

        int point = (Integer)range.get(0);
        String trackSource = "default";
        if(range.size() == 2)
            trackSource = (String)range.get(1);

        String type = configurationSection.getString("type");
        String nextSection = configurationSection.getString("nextSection");

        SwitchSectionSpecConfig switchSectionSpecConfig = null;
        if(configurationSection.contains("switchSpec"))
            switchSectionSpecConfig = SwitchSectionSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("switchSpec"));

        return new PointSectionConfig(sectionIdentifier, point, trackSource, type, nextSection, switchSectionSpecConfig);
    }

    @Override
    public SectionReference build(TrackBehaviourFactory trackBehaviourFactory, List<TrackDescription> trackDescriptions, CoasterHandle coasterHandle,
                                  CoasterConfig coasterConfig) {
        String sectionIdentifier = getIdentifier();
        String nextSectionIdentifier = getNextSection();
        String parentTrackIdentifier = getParentTrackIdentifier();

        Frame point = new SimpleFrame(getPoint());

        TrackBehaviour trackBehaviour = trackBehaviourFactory.getTrackBehaviourFor(coasterHandle, coasterConfig, this);
        if(trackBehaviour == null) return null;

        return new PointSectionReference(sectionIdentifier, point, trackBehaviour, nextSectionIdentifier, parentTrackIdentifier);
    }
}

