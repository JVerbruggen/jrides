package com.jverbruggen.jrides.event.action;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.common.MenuSessionManager;
import com.jverbruggen.jrides.common.Sync;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileField;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.VirtualEntity;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.inventory.Inventory;

public class OperateRideAction implements RideAction {
    @Override
    public void accept(VirtualEntity virtualEntity, RideHandle rideHandle, Player player) {
        Sync.runSynced(() -> acceptInternal(rideHandle, player));
    }

    private void acceptInternal(RideHandle rideHandle, Player player){
        MenuSessionManager menuSessionManager = ServiceProvider.getSingleton(MenuSessionManager.class);
        LanguageFile languageFile = ServiceProvider.getSingleton(LanguageFile.class);

        Menu rideControlMenu = rideHandle.getRideControlMenu();
        if(rideControlMenu == null){
            languageFile.sendMessage(player, LanguageFileField.ERROR_RIDE_CONTROL_MENU_NOT_FOUND, FeedbackType.CONFLICT);
            return;
        }
        Inventory inventory = rideControlMenu.getInventoryFor(player);

        menuSessionManager.addOpenMenu(player, rideControlMenu, inventory);
        player.getBukkitPlayer().openInventory(inventory);
    }
}
