package com.jverbruggen.jrides.command.control;

import com.jverbruggen.jrides.animator.RideHandle;
import com.jverbruggen.jrides.command.BaseCommandExecutor;
import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.control.controller.RideController;
import com.jverbruggen.jrides.control.trigger.DispatchTrigger;
import com.jverbruggen.jrides.language.LanguageFileFields;
import com.jverbruggen.jrides.language.LanguageFileTags;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import org.bukkit.command.Command;

public class ControlDispatchCommandExecutor extends BaseCommandExecutor {
    protected ControlDispatchCommandExecutor(int depth) {
        super(depth);
    }

    @Override
    public boolean onCommand(MessageAgent messageAgent, Command command, String s, String[] args, CommandContext commandContext) {
        RideHandle rideHandle = commandContext.get(RideHandle.class);
        RideController rideController = rideHandle.getRideController();
        DispatchTrigger dispatchTrigger = rideController.getTriggerContext().getDispatchTrigger();

        boolean dispatched = dispatchTrigger.execute(messageAgent);
        if(dispatched)
            languageFile.sendMessage(messageAgent, LanguageFileFields.COMMAND_RIDE_DISPATCHED_MESSAGE,
                    b -> b.add(LanguageFileTags.rideDisplayName, rideHandle.getRide().getDisplayName()));

        return true;
    }

    @Override
    public String getCommand() {
        return "dispatch";
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_CONTROL_DISPATCH;
    }

    @Override
    public String getHelpMessageForParent() {
        return "/jrides control <identifier> dispatch";
    }
}
