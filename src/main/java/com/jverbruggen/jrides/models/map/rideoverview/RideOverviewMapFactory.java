package com.jverbruggen.jrides.models.map.rideoverview;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Vector2;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import dev.cerus.maps.api.ClientsideMap;
import dev.cerus.maps.api.graphics.ClientsideMapGraphics;
import dev.cerus.maps.api.graphics.ColorCache;
import dev.cerus.maps.version.VersionAdapterFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.List;

public class RideOverviewMapFactory {
    private final SectionVisualFactory sectionVisualFactory;
    private final MapView defaultView = Bukkit.createMap(JRidesPlugin.getWorld());

    public RideOverviewMapFactory(){
        sectionVisualFactory = ServiceProvider.getSingleton(SectionVisualFactory.class);
    }

    public void giveMap(Player player, CoasterHandle coasterHandle){
        List<SectionVisual> sectionVisuals = sectionVisualFactory.createVisuals(coasterHandle);

        ItemStack itemStack = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
        mapMeta.setMapView(defaultView);
        itemStack.setItemMeta(mapMeta);

        player.getBukkitPlayer().getInventory().addItem(itemStack);

        int mapId = mapMeta.getMapView().getId();

        ClientsideMap clientsideMap = new ClientsideMap(mapId);
        ClientsideMapGraphics graphics = new ClientsideMapGraphics();

        graphics.fillComplete(ColorCache.rgbToMap(255,255,255));
        for(SectionVisual sectionVisual : sectionVisuals){
            List<Vector2> drawPoints = sectionVisual.getDrawPoints();
            if(drawPoints.size() <= 1) continue;
            Vector2 prev = drawPoints.get(0);
            for(int i = 1; i < drawPoints.size(); i++){
                Vector2 cur = drawPoints.get(i);

                graphics.drawLine((int)prev.x, (int)prev.y, (int)cur.x, (int)cur.y, ColorCache.rgbToMap(0,0,0), 1f);

                prev = cur;
            }
        }

        clientsideMap.draw(graphics);
        clientsideMap.sendTo(new VersionAdapterFactory().makeAdapter(), player.getBukkitPlayer());

        player.sendMessage("Given map");
    }
}
