package com.jverbruggen.jrides.control.uiinterface.menu.open;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.common.MenuSessionManager;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class SignMenuListener implements Listener {
    private final String secondLineTriggerText;
    private final RideManager rideManager;
    private final PlayerManager playerManager;
    private final MenuSessionManager menuSessionManager;

    public SignMenuListener(String secondLineTriggerText) {
        this.secondLineTriggerText = secondLineTriggerText;
        this.rideManager = ServiceProvider.getSingleton(RideManager.class);
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        this.menuSessionManager = ServiceProvider.getSingleton(MenuSessionManager.class);
    }

    /**
     * Sign format:
     * <br/>---
     * <br/>
     * <br/>[< ride_identifier >]
     * <br/>Control panel
     * <br/>
     * <br/>---
     * @param event
     */
    @EventHandler
    public void onSignClick(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;


        Block block = event.getClickedBlock();
        BlockData blockData = block.getBlockData();
        if(!(blockData instanceof WallSign)) return;

        Sign sign = (Sign) block.getState();
        String lineText = ChatColor.stripColor(sign.getLine(2));
        if(!lineText.equalsIgnoreCase(secondLineTriggerText)) return;

        String identifierRaw = sign.getLine(1);
        String identifier = identifierRaw
                .replace("[", "")
                .replace("]", "");

        Player player = playerManager.getPlayer(event.getPlayer());
        if(!player.hasPermission(Permissions.CABIN_OPERATE)){
            JRidesPlugin.getLanguageFile().sendMessage(player, LanguageFileField.ERROR_OPERATING_NO_PERMISSION);
            return;
        }


        RideHandle rideHandle = rideManager.getRideHandle(identifier);
        if(rideHandle == null) return;

        Menu rideControlMenu = rideHandle.getRideControlMenu();
        Inventory inventory = rideControlMenu.getInventoryFor(player);

        menuSessionManager.addOpenMenu(player, rideControlMenu, inventory);
        player.getBukkitPlayer().openInventory(inventory);
        player.playSound(Sound.UI_BUTTON_CLICK);
    }
}
