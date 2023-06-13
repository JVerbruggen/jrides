package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.command.context.CommandContext;
import com.jverbruggen.jrides.common.permissions.Permissions;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.language.LanguageFileFields;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.entity.SimpleMessageReceiver;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseCommandExecutor implements JRidesCommandExecutor {
    protected final LanguageFile languageFile;
    protected final PlayerManager playerManager;
    protected final Map<String, JRidesCommandExecutor> subCommands;
    protected final int depth;

    protected BaseCommandExecutor(int depth) {
        this.depth = depth;
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
        this.subCommands = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!commandSender.hasPermission(getPermission()) && !canEveryoneRun()){
            languageFile.sendMessage(commandSender, LanguageFileFields.ERROR_GENERAL_NO_PERMISSION_MESSAGE);
            return true;
        }

        return onCommand(commandSender, command, s, args, new CommandContext());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args, CommandContext commandContext){
        if(args.length == depth){
            languageFile.sendMultilineMessage(commandSender, getHelpMessageForParent());
            return true;
        }

        commandContext.add(MessageReceiver.class, new SimpleMessageReceiver(commandSender));
        String subCommand = args[depth];
        return runSubCommand(commandSender, command, s, args, subCommand, commandContext);
    }

    protected boolean runSubCommand(CommandSender commandSender, Command command, String s, String[] args, String subCommand, CommandContext commandContext){
        JRidesCommandExecutor firstCommand = findSubCommand(subCommand);
        if(firstCommand != null) {
            if(!commandSender.hasPermission(firstCommand.getPermission())){
                languageFile.sendMessage(commandSender, LanguageFileFields.ERROR_GENERAL_NO_PERMISSION_MESSAGE);
                return true;
            }

            firstCommand.onCommand(commandSender, command, s, args, commandContext);
            return true;
        }

        languageFile.sendMessage(commandSender, LanguageFileFields.ERROR_UNKNOWN_COMMAND_MESSAGE);
        return false;
    }

    protected <T extends BaseCommandExecutor> T registerSubCommand(T commandExecutor){
        subCommands.put(commandExecutor.getCommand(), commandExecutor);
        return commandExecutor;
    }

    public List<String> getCommandSuggestions(){
        return subCommands.keySet().stream().toList();
    }

    protected JRidesCommandExecutor findSubCommand(String command){
        return subCommands.get(command);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        String permission = getPermission();
        if(permission != null && !commandSender.hasPermission(permission)) return Collections.emptyList();

        if(strings.length <= depth+1){
            return getCommandSuggestions();
        }else{
            JRidesCommandExecutor subCommand = findSubCommand(strings[depth]);
            if(subCommand == null) return List.of();
            return subCommand.onTabComplete(commandSender, command, s, strings);
        }
    }

    @Override
    public String getHelpMessageForSelf() {
        return subCommands.values().stream()
                .map(JRidesCommandExecutor::getHelpMessageForParent)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String getPermission() {
        return Permissions.COMMAND_BASE;
    }

    public boolean canEveryoneRun(){
        return false;
    }
}
