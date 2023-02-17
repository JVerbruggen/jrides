package com.jverbruggen.jrides.config.coaster.objects.section.base;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.animator.trackbehaviour.TrackBehaviour;
import com.jverbruggen.jrides.animator.trackbehaviour.factory.TrackBehaviourFactory;
import com.jverbruggen.jrides.config.coaster.CoasterConfig;
import com.jverbruggen.jrides.config.coaster.objects.section.*;
import com.jverbruggen.jrides.config.coaster.objects.section.transfer.TransferSectionSpecConfig;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.properties.frame.SimpleFrame;
import com.jverbruggen.jrides.models.ride.factory.track.TrackDescription;
import com.jverbruggen.jrides.models.ride.section.reference.PointSectionReference;
import com.jverbruggen.jrides.models.ride.section.reference.RangedSectionReference;
import com.jverbruggen.jrides.models.ride.section.reference.SectionReference;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class PointSectionConfig extends SectionConfig {
    private final String identifier;
    private final int point;
    private final String trackSource;
    private final String type;
    private final String nextSection;

    public PointSectionConfig(String identifier, int point, String trackSource, String type, String nextSection) {
        this.identifier = identifier;
        this.point = point;
        this.trackSource = trackSource;
        this.type = type;
        this.nextSection = nextSection;
    }

    public int getPoint() {
        return point;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getNextSection() {
        return nextSection;
    }

    public String getParentTrackIdentifier() {
        return trackSource;
    }

    public String getType() {
        return type;
    }

    public static boolean accepts(String type){
        return switch (type) {
            case "track", "drive", "brake", "station", "blocksection", "transfer", "launch" -> true;
            default -> false;
        };
    }

    public static PointSectionConfig fromConfigurationSection(ConfigurationSection configurationSection, String sectionIdentifier) {
        List<?> range = configurationSection.getList("at");
        int point = (Integer)range.get(0);
        String trackSource = "default";
        if(range.size() == 2)
            trackSource = (String)range.get(1);

        String type = configurationSection.getString("type");
        String nextSection = configurationSection.getString("nextSection");

        return new PointSectionConfig(sectionIdentifier, point, trackSource, type, nextSection);
    }

    @Override
    public SectionReference build(TrackBehaviourFactory trackBehaviourFactory, List<TrackDescription> trackDescriptions, CoasterHandle coasterHandle,
                                  CoasterConfig coasterConfig) {
        String sectionIdentifier = getIdentifier();
        String nextSectionIdentifier = getNextSection();
        String parentTrackIdentifier = getParentTrackIdentifier();

        TrackDescription trackDescription = trackDescriptions.stream()
                .filter(d -> d.getIdentifier().equalsIgnoreCase(getParentTrackIdentifier()))
                .findFirst().orElseThrow();

        Frame point = new SimpleFrame(getPoint());

        TrackBehaviour trackBehaviour = trackBehaviourFactory.getTrackBehaviourFor(coasterHandle, coasterConfig, this);
        if(trackBehaviour == null) return null;

        return new PointSectionReference(sectionIdentifier, point, trackBehaviour, nextSectionIdentifier, parentTrackIdentifier);
    }
}

