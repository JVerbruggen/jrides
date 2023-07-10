package com.jverbruggen.jrides.models.map.ridecounter;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.map.rideoverview.MapScope;
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

public class RideCounterMap  {

    private final RideHandle rideHandle;
    private final MapView mapView;

    private final ClientsideMapGraphics currentGraphics;

    public RideCounterMap(RideHandle rideHandle, MapView mapView) {
        this.rideHandle = rideHandle;
        this.mapView = mapView;
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
        currentGraphics.fillComplete(ColorCache.rgbToMap(190,190,190));
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

}
