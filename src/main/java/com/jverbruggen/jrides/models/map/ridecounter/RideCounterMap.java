package com.jverbruggen.jrides.models.map.ridecounter;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.map.AbstractMap;
import dev.cerus.maps.api.ClientsideMap;
import dev.cerus.maps.api.Marker;
import dev.cerus.maps.api.graphics.ClientsideMapGraphics;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;

import java.awt.image.BufferedImage;
import java.util.Map;

public class RideCounterMap extends AbstractMap {

    private final RideHandle rideHandle;
    private final Map<Integer, Integer> lines;
    private final String lineFormat;
    private final BufferedImage backgroundImage;
    private final Integer rideNameLine;
    private final Integer typeLine;
    private final String typeText;
    private final byte primaryColor;
    private final byte secondaryColor;
    private final byte tertiaryColor;


    public RideCounterMap(RideHandle rideHandle, MapView mapView, Map<Integer, Integer> lines, String lineFormat, BufferedImage backgroundImage,
                            Integer rideNameLine, Integer typeLine, String typeText, byte primaryColor, byte secondaryColor, byte tertiaryColor) {
        super(mapView);
        this.rideHandle = rideHandle;
        this.lines = lines;
        this.lineFormat = lineFormat;
        this.backgroundImage = backgroundImage;
        this.rideNameLine = rideNameLine;
        this.typeLine = typeLine;
        this.typeText = typeText;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.tertiaryColor = tertiaryColor;
    }

    public void updateVisuals() {
        ClientsideMapGraphics currentGraphics = getGraphics();
        currentGraphics.drawImage(backgroundImage, 0, 0);

        // Draw the ride name if it is not null
        if(rideNameLine != null) {
            drawHorizontallyCenteredText(rideHandle.getRide().getDisplayName(), rideNameLine, primaryColor, currentGraphics);
        }

        // Draw the type line if it is not null
        if(typeLine != null) {
            drawHorizontallyCenteredText(typeText, typeLine, secondaryColor, currentGraphics);
        }

        // For each line, draw text on the map
        lines.forEach((index, height) -> {
            String line = lineFormat.replace("%RANK%", String.valueOf(index + 1)).replace("%COUNT%", "NaN");
            drawHorizontallyCenteredText(line, height, tertiaryColor, currentGraphics);
            drawHorizontallyCenteredText("player", height + MinecraftFont.Font.getHeight() + 2, primaryColor, currentGraphics);
        });
    }

    private void drawHorizontallyCenteredText(String text, int h, byte color, ClientsideMapGraphics graphics) {
        MinecraftFont font = MinecraftFont.Font;
        int x = (128 - font.getWidth(text)) / 2;
        graphics.drawText(x, h, text, color, 1);
    }

    @Override
    protected void drawExtraGraphics(ClientsideMap clientsideMap){
        // Do nothing
    }

}
