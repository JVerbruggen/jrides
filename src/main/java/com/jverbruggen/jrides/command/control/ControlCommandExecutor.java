package com.jverbruggen.jrides.command.control;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.command.BaseCommandExecutor;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenu;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.language.StringReplacementBuilder;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.SimpleMessageReceiver;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;

public class ControlCommandExecutor extends BaseCommandExecutor {
    private final RideManager rideManager;
    private final RideControlMenuFactory rideControlMenuFactory;

    public ControlCommandExecutor() {
        rideManager = ServiceProvider.getSingleton(RideManager.class);
        rideControlMenuFactory = ServiceProvider.getSingleton(RideControlMenuFactory.class);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args) {
        if(args.length <= 2){
            languageFile.sendMultilineMessage(commandSender, getHelpMessage());
            return true;
        }

        MessageReceiver messageReceiver = SimpleMessageReceiver.from(commandSender);

        String identifier = args[1];
        String subCommand = args[2];
        CoasterHandle rideHandle = rideManager.getRideHandle(identifier);
        RideController rideController = rideHandle.getRideController();

        if(subCommand.equalsIgnoreCase("dispatch")){
            DispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();

            boolean dispatched = dispatchTrigger.execute(messageReceiver);
            if(dispatched) languageFile.sendMessage(messageReceiver,
                    languageFile.commandRideDispatchedMessage,
                    new StringReplacementBuilder().add("RIDE_IDENTIFIER", identifier).collect());

            return true;
        }else if(subCommand.equalsIgnoreCase("menu")){
            if(!(commandSender instanceof org.bukkit.entity.Player)){
                languageFile.sendMessage(commandSender, languageFile.errorPlayerCommandOnlyMessage);
                return true;
            }
            if(!commandSender.hasPermission(Permissions.COMMAND_MENU)){
                languageFile.sendMessage(commandSender, languageFile.errorGeneralNoPermissionMessage);
                return true;
            }
            Player player = playerManager.getPlayer((org.bukkit.entity.Player) commandSender);

            RideControlMenu rideControlMenu = rideHandle.getRideControlMenu();
            Inventory inventory = rideControlMenu.getInventoryFor(player);

            rideControlMenuFactory.addOpenRideControlMenu(player, rideControlMenu, inventory);
            player.getBukkitPlayer().openInventory(inventory);
            return true;
        }

        languageFile.sendMultilineMessage(messageReceiver, getHelpMessage());
        return true;
    }

    private String getHelpMessage(){
        return "/jrides control <identifier> dispatch\n" +
                "/jrides control <identifier> menu";
    }

    @Override
    public String getGenericHelpMessage() {
        return "/jrides control";
    }
}
