package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector2;
import com.jverbruggen.jrides.models.ride.section.Section;
import dev.cerus.maps.api.ClientsideMap;
import dev.cerus.maps.api.graphics.ClientsideMapGraphics;
import dev.cerus.maps.api.graphics.ColorCache;
import dev.cerus.maps.version.VersionAdapterFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.Collection;
import java.util.List;

public class RideOverviewMap {
    private static final byte COLOR_BACKGROUND = ColorCache.rgbToMap(255,255,255);
    private static final byte COLOR_DEFAULT = ColorCache.rgbToMap(0,0,0);
    private static final byte COLOR_OCCUPIED =  ColorCache.rgbToMap(255,0,0);
    private static final byte COLOR_RESERVED = ColorCache.rgbToMap(100,100,255);

    private final CoasterHandle coasterHandle;
    private final MapView mapView;
    private final List<SectionVisual> sectionVisuals;
    private final ClientsideMapGraphics currentGraphics;
    private List<TrainVisual> trainVisuals;

    public RideOverviewMap(CoasterHandle coasterHandle, MapView mapView, List<SectionVisual> sectionVisuals, List<TrainVisual> trainVisuals) {
        this.coasterHandle = coasterHandle;
        this.mapView = mapView;
        this.sectionVisuals = sectionVisuals;
        this.currentGraphics = new ClientsideMapGraphics();
        this.trainVisuals = trainVisuals;
    }

    public void give(Player player){
        ItemStack itemStack = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
        assert mapMeta != null;
        mapMeta.setMapView(mapView);
        itemStack.setItemMeta(mapMeta);

        player.getBukkitPlayer().getInventory().addItem(itemStack);

        sendUpdate(player, true);
    }

    public void updateVisuals(){
        currentGraphics.fillComplete(COLOR_BACKGROUND);
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

    public void sendUpdate(Collection<Player> players) {
        if(players == null || players.isEmpty()) return;

        players.forEach(p -> sendUpdate(p, false));
    }

    private void sendUpdate(Player player, boolean delayed){
        int mapId = mapView.getId();

        ClientsideMap clientsideMap = new ClientsideMap(mapId);
        clientsideMap.draw(currentGraphics);
        trainVisuals.forEach(v -> clientsideMap.addMarker(v.getMarker()));

        if(delayed){
            Bukkit.getScheduler().runTaskLater(JRidesPlugin.getBukkitPlugin(),
                    () -> sendMap(clientsideMap, player),
                    10L);
        }else{
            sendMap(clientsideMap, player);
        }
    }

    private byte getSectionColor(Section section){
        if(section.isOccupied()) return COLOR_OCCUPIED;
        else if(section.getReservedBy() != null) return COLOR_RESERVED;
        else return COLOR_DEFAULT;
    }

    private void sendMap(ClientsideMap map, Player player){
        map.sendTo(new VersionAdapterFactory().makeAdapter(), player.getBukkitPlayer());
    }

    public MapView getMapView() {
        return mapView;
    }

    public List<SectionVisual> getSectionVisuals() {
        return sectionVisuals;
    }

}
