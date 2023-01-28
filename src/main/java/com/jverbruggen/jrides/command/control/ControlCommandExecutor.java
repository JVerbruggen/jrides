package com.jverbruggen.jrides.command.control;

import com.jverbruggen.jrides.animator.CoasterHandle;
import com.jverbruggen.jrides.command.JRidesCommandExecutor;
import com.jverbruggen.jrides.control.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenu;
import com.jverbruggen.jrides.control.uiinterface.menu.RideControlMenuFactory;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.entity.SimpleMessageReceiver;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import com.jverbruggen.jrides.state.ride.RideManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;

public class ControlCommandExecutor implements JRidesCommandExecutor {
    private final RideManager rideManager;
    private final PlayerManager playerManager;
    private final RideControlMenuFactory rideControlMenuFactory;

    public ControlCommandExecutor() {
        rideManager = ServiceProvider.GetSingleton(RideManager.class);
        playerManager = ServiceProvider.GetSingleton(PlayerManager.class);
        rideControlMenuFactory = ServiceProvider.GetSingleton(RideControlMenuFactory.class);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args) {
        if(args.length <= 2){
            commandSender.sendMessage(getHelpMessage());
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
            if(dispatched) messageReceiver.sendMessage("Ride " + identifier + " was dispatched!");

            return true;
        }else if(subCommand.equalsIgnoreCase("menu")){
            if(!(commandSender instanceof org.bukkit.entity.Player)){
                messageReceiver.sendMessage("Player command only");
                return true;
            }
            Player player = playerManager.getPlayer((org.bukkit.entity.Player) commandSender);

            RideControlMenu rideControlMenu = rideHandle.getRideControlMenu();
            Inventory inventory = rideControlMenu.getInventoryFor(player);

            rideControlMenuFactory.addOpenRideControlMenu(player, rideControlMenu, inventory);
            player.getBukkitPlayer().openInventory(inventory);
            return true;
        }

        messageReceiver.sendMessage(getHelpMessage());
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
