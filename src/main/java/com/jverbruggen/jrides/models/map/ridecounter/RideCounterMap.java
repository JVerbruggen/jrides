package com.jverbruggen.jrides.models.map.ridecounter;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.entity.Player;
import dev.cerus.maps.api.ClientsideMap;
import dev.cerus.maps.api.graphics.ClientsideMapGraphics;
import dev.cerus.maps.api.graphics.ColorCache;
import dev.cerus.maps.version.VersionAdapterFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RideCounterMap  {

    private final RideHandle rideHandle;
    private final MapView mapView;
    private final Map<Integer, Integer> lines;
    private final BufferedImage backgroundImage;


    private final ClientsideMapGraphics currentGraphics;

    public RideCounterMap(RideHandle rideHandle, MapView mapView, Map<Integer, Integer> lines, BufferedImage backgroundImage) {
        this.rideHandle = rideHandle;
        this.mapView = mapView;
        this.lines = lines;
        this.backgroundImage = backgroundImage;
        this.currentGraphics = new ClientsideMapGraphics();
    }

    public void give(Player player) {
        ItemStack itemStack = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
        assert mapMeta != null;
        mapMeta.setMapView(mapView);
        itemStack.setItemMeta(mapMeta);

        player.getBukkitPlayer().getInventory().addItem(itemStack);

        // Temporary update visuals call for testing
        updateVisuals();

        sendUpdate(player, true);
    }

    public void updateVisuals() {
        // TODO: Implement this functionality to show the ride counters
        currentGraphics.drawImage(backgroundImage, 0, 0);

        // For each line, draw text on the map
        lines.forEach((index, height) -> {
            drawHorizontallyCenteredText("Test " + index, height, currentGraphics);
        });
    }

    public void sendUpdate(Collection<Player> players) {
        if(players == null || players.isEmpty()) return;

        players.forEach(p -> sendUpdate(p, false));
    }

    private void sendUpdate(Player player, boolean delayed) {
        int mapId = mapView.getId();

        ClientsideMap clientsideMap = new ClientsideMap(mapId);
        clientsideMap.draw(currentGraphics);

        if(delayed) {
            Bukkit.getScheduler().runTaskLater(JRidesPlugin.getBukkitPlugin(),
                    () -> sendMap(clientsideMap, player),
                    10L);
        }else {
            sendMap(clientsideMap, player);
        }
    }

    private void sendMap(ClientsideMap map, Player player) {
        map.sendTo(new VersionAdapterFactory().makeAdapter(), player.getBukkitPlayer());
    }

    private void drawHorizontallyCenteredText(String text, int h, ClientsideMapGraphics graphics) {
        MinecraftFont font = MinecraftFont.Font;
        int x = (128 - font.getWidth(text)) / 2;
        graphics.drawText(x, h, text, ColorCache.rgbToMap(0, 0, 0), 1);
    }

}
