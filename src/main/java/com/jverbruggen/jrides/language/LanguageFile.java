/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.language;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.api.JRidesPlayer;
import com.jverbruggen.jrides.config.ConfigManager;
import com.jverbruggen.jrides.logging.JRidesLogger;
import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.entity.SimpleMessageReceiver;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LanguageFile {
    private final ConfigManager configManager;
    private final JRidesLogger logger;
    private FeedbackType defaultFeedbackType = FeedbackType.INFO;
    private Map<LanguageFileField, String> language;

    public LanguageFile() {
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
        this.logger = ServiceProvider.getSingleton(JRidesLogger.class);
        this.language = new HashMap<>();

        // Start of language definitions (do not edit this line)

        setLanguageDefault(LanguageFileField.CHAT_FEEDBACK_PREFIX, "[jrides] ");
        setLanguageDefault(LanguageFileField.CHAT_FEEDBACK_INFO_COLOR, "&7");
        setLanguageDefault(LanguageFileField.CHAT_FEEDBACK_WARNING_COLOR, "&e");
        setLanguageDefault(LanguageFileField.CHAT_FEEDBACK_SEVERE_COLOR, "&c");

        setLanguageDefault(LanguageFileField.COMMAND_RIDE_DISPATCHED_MESSAGE, "Ride %" + LanguageFileTag.rideDisplayName + "% was dispatched!");

        setLanguageDefault(LanguageFileField.COMMAND_VISUALIZE_ADDED_VIEWER, "You are now viewing %" + LanguageFileTag.rideIdentifier + "% in visualize mode");
        setLanguageDefault(LanguageFileField.COMMAND_VISUALIZE_REMOVED_VIEWER, "You are no longer viewing %" + LanguageFileTag.rideIdentifier + "% in visualize mode");

        setLanguageDefault(LanguageFileField.NOTIFICATION_PLUGIN_STILL_LOADING, "Please try again later when jrides is loaded");

        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_CONTROL_ACTIVE, "You are now controlling %" + LanguageFileTag.rideDisplayName + "%");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_CONTROL_INACTIVE, "You are no longer controlling %" + LanguageFileTag.rideDisplayName + "%");

        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_DISPATCH_PROBLEMS, "Cannot dispatch due to the following problems:");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_NO_TRAIN_PRESENT, "No train present in station");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_NEXT_BLOCK_OCCUPIED, "Next block section is occupied");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_WAITING_TIME, "Waiting time has not passed yet");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_RESTRAINTS_NOT_CLOSED, "Not all restraints are closed");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_GATES_NOT_CLOSED, "Not all gates are closed");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_GATE_NOT_CLOSED, "Gate %" + LanguageFileTag.name + "% is not closed");

        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_COUNTER_UPDATE, "\nYou've ridden %" + LanguageFileTag.rideDisplayName + "% %" + LanguageFileTag.rideCount + "% times now\n");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_STATE_OPEN, "%" + LanguageFileTag.rideDisplayName + "% is now open");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RIDE_STATE_CLOSED, "%" + LanguageFileTag.rideDisplayName + "% is now closed");

        setLanguageDefault(LanguageFileField.NOTIFICATION_SHIFT_EXIT_CONFIRMATION, "Press shift again within 2 seconds to confirm exiting the ride");
        setLanguageDefault(LanguageFileField.NOTIFICATION_SHIFT_EXIT_CONFIRMED, "You just exited the ride while the restraints were closed");
        setLanguageDefault(LanguageFileField.NOTIFICATION_DISPATCH_WAIT_GENERIC, "Please wait until the ride is dispatched");
        setLanguageDefault(LanguageFileField.NOTIFICATION_DISPATCH_WAIT_SPECIFIC, "Waiting time: %" + LanguageFileTag.time + "% seconds");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RESTRAINT_ON_EXIT_ATTEMPT, "The restraints are closed");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RESTRAINT_ON_ENTER_ATTEMPT, "The restraints are closed");
        setLanguageDefault(LanguageFileField.NOTIFICATION_RESTRAINT_ENTER_OVERRIDE, "You just entered the ride while the restraints were closed");
        setLanguageDefault(LanguageFileField.NOTIFICATION_CANNOT_ENTER_RIDE, "You currently cannot enter this ride, try again later");
        setLanguageDefault(LanguageFileField.NOTIFICATION_CANNOT_ENTER_RIDE_CLOSED, "This ride is currently closed");

        setLanguageDefault(LanguageFileField.NOTIFICATION_OPERATOR_IDLE_TOO_LONG, "You were idle for too long while operating %" + LanguageFileTag.rideDisplayName + "%");
        setLanguageDefault(LanguageFileField.NOTIFICATION_WARPED, "");

        setLanguageDefault(LanguageFileField.ELEVATED_OPERATOR_OVERRIDE_VICTIM_MESSAGE, "Player %" + LanguageFileTag.player + "% took over control of the operating cabin");

        setLanguageDefault(LanguageFileField.ERROR_SMOOTH_COASTERS_DISABLED, "Smoother ride experience is disabled, please install SmoothCoasters");
        setLanguageDefault(LanguageFileField.ERROR_GENERAL_NO_PERMISSION_MESSAGE, "You do not have permissions to execute this action");
        setLanguageDefault(LanguageFileField.ERROR_PLAYER_COMMAND_ONLY_MESSAGE, "Only players can execute this command");
        setLanguageDefault(LanguageFileField.ERROR_UNKNOWN_COMMAND_MESSAGE, "Unknown jrides command. Type '/jrides help' for help");
        setLanguageDefault(LanguageFileField.ERROR_OPERATING_CABIN_OCCUPIED, "You can not take this operating cabin since it is already in use by another operator");
        setLanguageDefault(LanguageFileField.ERROR_OPERATING_NO_PERMISSION, "You are not allowed to operate this ride");
        setLanguageDefault(LanguageFileField.ERROR_RIDE_CONTROL_MENU_NOT_FOUND, "Ride control menu was not found");
        setLanguageDefault(LanguageFileField.ERROR_RIDE_OVERVIEW_MAP_NOT_FOUND, "Could not retrieve map for ride %" + LanguageFileTag.rideIdentifier + "%, was the map id configured?");
        setLanguageDefault(LanguageFileField.ERROR_CANNOT_WARP, "Could not warp to ride %" + LanguageFileTag.rideDisplayName + "%");

        setLanguageDefault(LanguageFileField.MENU_RIDE_OVERVIEW_TITLE, "Ride overview menu");
        setLanguageDefault(LanguageFileField.MENU_RIDE_OVERVIEW_STATUS_OPEN, "This ride is currently opened");
        setLanguageDefault(LanguageFileField.MENU_RIDE_OVERVIEW_STATUS_CLOSED, "This ride is currently closed");
        setLanguageDefault(LanguageFileField.MENU_RIDE_OVERVIEW_STATUS_MAINTENANCE, "This ride is in maintenance");
        setLanguageDefault(LanguageFileField.MENU_RIDE_CONTROL_TITLE, "Ride control menu");
        setLanguageDefault(LanguageFileField.MENU_ADMIN_RIDE_CONTROL_TITLE, "Admin ride control menu");

        setLanguageDefault(LanguageFileField.BUTTON_CLAIM_CABIN, "Claim operating cabin");
        setLanguageDefault(LanguageFileField.BUTTON_CABIN_CLAIMED, "Claim operating cabin");
        setLanguageDefault(LanguageFileField.BUTTON_DISPATCH_STATE, "Dispatch");
        setLanguageDefault(LanguageFileField.BUTTON_DISPATCH_PROBLEM_STATE, "Not allowed");
        setLanguageDefault(LanguageFileField.BUTTON_PROBLEMS_STATE, "Problems");
        setLanguageDefault(LanguageFileField.BUTTON_GATES_OPEN_STATE, "Gates are open");
        setLanguageDefault(LanguageFileField.BUTTON_GATES_CLOSED_STATE, "Gates are closed");
        setLanguageDefault(LanguageFileField.BUTTON_RESTRAINTS_OPEN_STATE, "Restraints are open");
        setLanguageDefault(LanguageFileField.BUTTON_RESTRAINTS_CLOSED_STATE, "Restraints are closed");

        // End of language definitions (do not edit this line)

        fillOverrides();
    }

    private void setLanguageDefault(LanguageFileField field, String _default){
        this.language.put(field, _default);
    }

    private void fillOverrides(){
        Map<String, String> languageOverrides = configManager.getLanguageFile();
        if(languageOverrides == null){
            logger.info("language.yml file was not found.");
            return;
        }

        for(Map.Entry<String, String> entry : languageOverrides.entrySet()){
            String keyString = entry.getKey();
            LanguageFileField field;
            try{
                field = LanguageFileField.valueOf(keyString);
            }catch (IllegalArgumentException e){
                JRidesPlugin.getLogger().severe("Could not override language field " + keyString + " because it does not exist");
                return;
            }

            String overrideValue = entry.getValue();
            overrideValue = ChatColor.translateAlternateColorCodes('&', overrideValue);

            language.put(field, overrideValue);
        }

        this.logger.info("Loaded language file with " + languageOverrides.size() + " overrides");
    }

    public @Nonnull String get(@Nonnull LanguageFileField field){
        return get(field, null);
    }

    public @Nonnull String get(@Nonnull LanguageFileField field, Function<StringReplacementBuilder, StringReplacementBuilder> builderFunction){
        StringReplacementBuilder builder = new StringReplacementBuilder();
        if(builderFunction != null) builder = builderFunction.apply(builder);

        String value = language.get(field);
        if(value == null)
            throw new RuntimeException("Language value for language field " + field + " could not be found");

        value = builder.apply(value);

        return value;
    }

    public void sendMessage(MessageReceiver messageReceiver, @Nonnull LanguageFileField field){
        sendMessage(messageReceiver, field, defaultFeedbackType, null);
    }

    public void sendMessage(MessageReceiver messageReceiver, @Nonnull String content){
        sendMessage(messageReceiver, content, defaultFeedbackType, null);
    }

    public void sendMessage(MessageReceiver messageReceiver, @Nonnull LanguageFileField field, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        sendMessage(messageReceiver, field, defaultFeedbackType, builder);
    }

    public void sendMessage(MessageReceiver messageReceiver, @Nonnull LanguageFileField field, FeedbackType feedbackType) {
        sendMessage(messageReceiver, field, feedbackType, null);
    }

    public void sendMessage(MessageReceiver messageReceiver, @Nonnull String content, FeedbackType feedbackType) {
        sendMessage(messageReceiver, content, feedbackType, null);
    }

    public void sendMessage(MessageReceiver messageReceiver, @Nonnull LanguageFileField field, FeedbackType feedbackType, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        String content = get(field);
        sendMessage(messageReceiver, content, feedbackType, builder);
    }

    public void sendMessage(MessageReceiver messageReceiver, @Nonnull String content, FeedbackType feedbackType, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        String prefix = getChatFeedbackColor(feedbackType) + get(LanguageFileField.CHAT_FEEDBACK_PREFIX);
        sendMessage(messageReceiver, prefix, content, builder);
    }

    public void sendMessage(CommandSender commandSender, @Nonnull String content){
        sendMessage(new SimpleMessageReceiver(commandSender), content);
    }

    public void sendMessage(CommandSender commandSender, @Nonnull LanguageFileField field){
        sendMessage(new SimpleMessageReceiver(commandSender), field);
    }

    public void sendMessage(CommandSender commandSender, @Nonnull LanguageFileField field, FeedbackType feedbackType){
        sendMessage(new SimpleMessageReceiver(commandSender), field, feedbackType, null);
    }

    public void sendMessage(CommandSender commandSender, @Nonnull LanguageFileField field, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        sendMessage(new SimpleMessageReceiver(commandSender), field, defaultFeedbackType, builder);
    }

    public void sendMultilineMessage(CommandSender commandSender, @Nonnull LanguageFileField field){
        sendMultilineMessage(new SimpleMessageReceiver(commandSender), field);
    }

    public void sendMultilineMessage(CommandSender commandSender, @Nonnull LanguageFileField field, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        sendMultilineMessage(new SimpleMessageReceiver(commandSender), get(field), defaultFeedbackType, builder);
    }

    public void sendMultilineMessage(CommandSender commandSender, @Nonnull String content){
        sendMultilineMessage(new SimpleMessageReceiver(commandSender), content);
    }

    public void sendMultilineMessage(CommandSender commandSender, @Nonnull String content, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        sendMultilineMessage(new SimpleMessageReceiver(commandSender), content, defaultFeedbackType, builder);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, @Nonnull LanguageFileField field){
        sendMultilineMessage(messageReceiver, field, defaultFeedbackType);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, @Nonnull LanguageFileField field, FeedbackType feedbackType){
        sendMultilineMessage(messageReceiver, get(field), feedbackType, null);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, @Nonnull LanguageFileField field, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        sendMultilineMessage(messageReceiver, get(field), defaultFeedbackType, builder);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, @Nonnull String content){
        sendMultilineMessage(messageReceiver, content, defaultFeedbackType);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, @Nonnull String content, FeedbackType feedbackType){
        sendMultilineMessage(messageReceiver, content, feedbackType, null);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, @Nonnull String content, FeedbackType feedbackType, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        String prefix = getChatFeedbackColor(feedbackType);
        sendMessage(messageReceiver, prefix, content, builder);
    }

    public void sendMultilineMessage(JRidesPlayer apiPlayer, @Nonnull LanguageFileField field, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        if(!(apiPlayer instanceof MessageReceiver)){
            throw new RuntimeException("Unexpected type in JRidesPlayer at languagefile");
        }

        sendMultilineMessage((MessageReceiver) apiPlayer, get(field), defaultFeedbackType, builder);
    }

    public void sendMessage(MessageReceiver messageReceiver, String prefix, @Nonnull String content, Function<StringReplacementBuilder, StringReplacementBuilder> builderFunction){
        if(content.equals("")) return;

        StringReplacementBuilder builder = new StringReplacementBuilder();
        if(builderFunction != null) builder = builderFunction.apply(builder);

        content = builder.apply(content);

        messageReceiver.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + content));
    }

    private String getChatFeedbackColor(FeedbackType feedbackType){
        String chatFeedbackColor;
        if(feedbackType.equals(FeedbackType.INFO)) chatFeedbackColor = get(LanguageFileField.CHAT_FEEDBACK_INFO_COLOR);
        else if(feedbackType.equals(FeedbackType.WARNING)) chatFeedbackColor = get(LanguageFileField.CHAT_FEEDBACK_WARNING_COLOR);
        else if(feedbackType.equals(FeedbackType.SEVERE)) chatFeedbackColor = get(LanguageFileField.CHAT_FEEDBACK_SEVERE_COLOR);
        else if(feedbackType.equals(FeedbackType.CONFLICT)) chatFeedbackColor = get(LanguageFileField.CHAT_FEEDBACK_WARNING_COLOR);
        else throw new RuntimeException("Unsupported feedback type");

        return chatFeedbackColor;
    }
}
