package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.models.map.AbstractMap;
import com.jverbruggen.jrides.models.math.Vector2;
import com.jverbruggen.jrides.models.ride.section.Section;
import dev.cerus.maps.api.ClientsideMap;
import dev.cerus.maps.api.graphics.ClientsideMapGraphics;
import dev.cerus.maps.api.graphics.ColorCache;
import org.bukkit.map.MapView;

import java.util.List;

public class RideOverviewMap extends AbstractMap {
    private static final byte COLOR_BORDER_DARK = ColorCache.rgbToMap(55,55,55);
    private static final byte COLOR_BORDER_DARK_LIGHTER = ColorCache.rgbToMap(110,110,110);

    private static final byte COLOR_BACKGROUND = ColorCache.rgbToMap(190,190,190);
    private static final byte COLOR_DEFAULT = ColorCache.rgbToMap(50,50,50);
    private static final byte COLOR_OCCUPIED =  ColorCache.rgbToMap(27, 181, 0);
    private static final byte COLOR_RESERVED = ColorCache.rgbToMap(214, 143, 0);

    private final List<SectionVisual> sectionVisuals;
    private List<TrainVisual> trainVisuals;

    public RideOverviewMap(MapView mapView, List<SectionVisual> sectionVisuals, List<TrainVisual> trainVisuals) {
        super(mapView);
        this.sectionVisuals = sectionVisuals;
        this.trainVisuals = trainVisuals;
    }

    public void updateVisuals(){
        ClientsideMapGraphics currentGraphics = getGraphics();
        currentGraphics.fillComplete(COLOR_BACKGROUND);
        addBorder(currentGraphics);

        for(SectionVisual sectionVisual : sectionVisuals){
            List<Vector2> drawPoints = sectionVisual.getDrawPoints();

            Section section = sectionVisual.getSection();
            byte color = getSectionColor(section);

            if(drawPoints.size() <= 1) continue;
            Vector2 prev = drawPoints.get(0);
            for(int i = 1; i < drawPoints.size(); i++){
                Vector2 cur = drawPoints.get(i);

                currentGraphics.drawLine((int)prev.x, (int)prev.y, (int)cur.x, (int)cur.y, color, 1f);

                prev = cur;
            }
        }

        for(TrainVisual visual : trainVisuals){
            visual.update();
        }
    }

    private byte getSectionColor(Section section){
        if(section.isOccupied()) return COLOR_OCCUPIED;
        else if(section.getReservedBy() != null) return COLOR_RESERVED;
        else return COLOR_DEFAULT;
    }

    @Override
    protected void drawExtraGraphics(ClientsideMap clientsideMap){
        trainVisuals.forEach(v -> clientsideMap.addMarker(v.getMarker()));
    }


    private void addBorder(ClientsideMapGraphics graphics){
        int borderWidth = 6;
        int shadeWidth = 3;
        int size = 128;

        graphics.fillRect(0, 0, size, shadeWidth,               COLOR_BORDER_DARK, 1f);
        graphics.fillRect(0, 0, shadeWidth, size,               COLOR_BORDER_DARK, 1f);
        graphics.fillRect(size-shadeWidth, 0, shadeWidth, size, COLOR_BORDER_DARK, 1f);
        graphics.fillRect(0, size-shadeWidth, size, shadeWidth, COLOR_BORDER_DARK, 1f);

        graphics.fillRect(shadeWidth, shadeWidth, size-borderWidth, shadeWidth,               COLOR_BORDER_DARK_LIGHTER, 1f);
        graphics.fillRect(shadeWidth, shadeWidth, shadeWidth, size-borderWidth,               COLOR_BORDER_DARK_LIGHTER, 1f);
        graphics.fillRect(size-borderWidth, shadeWidth, shadeWidth, size-borderWidth, COLOR_BORDER_DARK_LIGHTER, 1f);
        graphics.fillRect(shadeWidth, size-borderWidth, size-borderWidth, shadeWidth, COLOR_BORDER_DARK_LIGHTER, 1f);
    }
}
