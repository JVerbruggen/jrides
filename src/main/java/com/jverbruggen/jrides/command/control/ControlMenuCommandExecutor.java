package com.jverbruggen.jrides.command.control;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.command.BaseCommandExecutor;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFileFields;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.models.menu.Menu;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;

public class ControlMenuCommandExecutor extends BaseCommandExecutor {
    private final RideControlMenuFactory rideControlMenuFactory;

    protected ControlMenuCommandExecutor(int depth) {
        super(depth);
        this.rideControlMenuFactory = ServiceProvider.getSingleton(RideControlMenuFactory.class);
    }

    @Override
    public boolean onCommand(MessageAgent messageAgent, Command command, String s, String[] args, CommandContext commandContext) {
        if(!messageAgent.isPlayer()){
            languageFile.sendMessage(messageAgent, LanguageFileFields.ERROR_PLAYER_COMMAND_ONLY_MESSAGE, FeedbackType.CONFLICT);
            return true;
        }

        Player player = messageAgent.getPlayer(playerManager);

        RideHandle rideHandle = commandContext.get(RideHandle.class);
        Menu rideControlMenu = rideHandle.getRideControlMenu();
        if(rideControlMenu == null){
            languageFile.sendMessage(messageAgent, LanguageFileFields.ERROR_RIDE_CONTROL_MENU_NOT_FOUND, FeedbackType.CONFLICT);
            return true;
        }
        Inventory inventory = rideControlMenu.getInventoryFor(player);

        rideControlMenuFactory.addOpenRideControlMenu(player, rideControlMenu, inventory);
        player.getBukkitPlayer().openInventory(inventory);
        return true;
    }

    @Override
    public String getCommand() {
        return "menu";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_CONTROL_MENU;
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides control <identifier> menu";
    }
}
