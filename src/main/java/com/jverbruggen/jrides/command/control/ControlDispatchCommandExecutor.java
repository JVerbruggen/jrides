package com.jverbruggen.jrides.command.control;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.command.BaseCommandExecutor;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.language.LanguageFileTags;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.entity.SimpleMessageReceiver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ControlDispatchCommandExecutor extends BaseCommandExecutor {
    protected ControlDispatchCommandExecutor(int depth) {
        super(depth);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args, CommandContext commandContext) {
        MessageReceiver messageReceiver = SimpleMessageReceiver.from(commandSender);
        RideHandle rideHandle = commandContext.get(RideHandle.class);
        RideController rideController = rideHandle.getRideController();
        DispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();

        boolean dispatched = dispatchTrigger.execute(messageReceiver);
        if(dispatched)
            languageFile.sendMessage(messageReceiver, languageFile.commandRideDispatchedMessage,
                    b -> b.add(LanguageFileTags.rideIdentifier, rideHandle.getRide().getIdentifier()));

        return true;
    }

    @Override
    public String getCommand() {
        return "dispatch";
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides control <identifier> dispatch";
    }
}
