package com.jverbruggen.jrides.command.control;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.command.BaseCommandExecutor;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenu;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.models.entity.Player;
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
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args, CommandContext commandContext) {
        if(!(commandSender instanceof org.bukkit.entity.Player)){
            languageFile.sendMessage(commandSender, languageFile.errorPlayerCommandOnlyMessage);
            return true;
        }
        if(!commandSender.hasPermission(Permissions.COMMAND_MENU)){
            languageFile.sendMessage(commandSender, languageFile.errorGeneralNoPermissionMessage);
            return true;
        }
        Player player = playerManager.getPlayer((org.bukkit.entity.Player) commandSender);

        RideHandle rideHandle = commandContext.get(RideHandle.class);
        RideControlMenu rideControlMenu = rideHandle.getRideControlMenu();
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
    public String getHelpMessageForParent() {
        return "/jrides control <identifier> menu";
    }
}
