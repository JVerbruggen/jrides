package com.jverbruggen.jrides.models.map;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.models.entity.Player;

import dev.cerus.maps.api.ClientsideMap;
import dev.cerus.maps.api.graphics.ClientsideMapGraphics;
import dev.cerus.maps.version.VersionAdapterFactory;

public abstract class AbstractMap implements VirtualMap {
    private final MapView mapView;
    private final ClientsideMapGraphics currentGraphics;

    public AbstractMap(MapView mapView){
        this.mapView = mapView;
        this.currentGraphics = new ClientsideMapGraphics();
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

    public void sendUpdate(Collection<Player> players) {
        if(players == null || players.isEmpty()) return;

        players.forEach(p -> sendUpdate(p, false));
    }

    protected ClientsideMapGraphics getGraphics(){
        return currentGraphics;
    }

    private void sendUpdate(Player player, boolean delayed){
        int mapId = mapView.getId();

        ClientsideMap clientsideMap = new ClientsideMap(mapId);
        clientsideMap.draw(currentGraphics);
        drawExtraGraphics(clientsideMap);

        if(delayed){
            Bukkit.getScheduler().runTaskLater(JRidesPlugin.getBukkitPlugin(),
                    () -> sendMap(clientsideMap, player),
                    10L);
        }else{
            sendMap(clientsideMap, player);
        }
    }

    private void sendMap(ClientsideMap map, Player player){
        map.sendTo(new VersionAdapterFactory().makeAdapter(), player.getBukkitPlayer());
    }

    protected abstract void drawExtraGraphics(ClientsideMap clientsideMap);
}
