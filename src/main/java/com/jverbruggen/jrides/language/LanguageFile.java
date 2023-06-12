package com.jverbruggen.jrides.language;

import com.jverbruggen.jrides.JRidesPlugin;
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
    private Map<LanguageFileFields, String> language;

    public LanguageFile() {
        this.configManager = ServiceProvider.getSingleton(ConfigManager.class);
        this.logger = ServiceProvider.getSingleton(JRidesLogger.class);
        this.language = new HashMap<>();

        setLanguageDefault(LanguageFileFields.CHAT_FEEDBACK_PREFIX, "[jrides] ");
        setLanguageDefault(LanguageFileFields.CHAT_FEEDBACK_INFO_COLOR, ChatColor.GRAY + "");
        setLanguageDefault(LanguageFileFields.CHAT_FEEDBACK_WARNING_COLOR, ChatColor.YELLOW + "");
        setLanguageDefault(LanguageFileFields.CHAT_FEEDBACK_SEVERE_COLOR, ChatColor.RED + "");

        setLanguageDefault(LanguageFileFields.COMMAND_RIDE_DISPATCHED_MESSAGE, "Ride %" + LanguageFileTags.rideDisplayName + "% was dispatched!");

        setLanguageDefault(LanguageFileFields.COMMAND_VISUALIZE_ADDED_VIEWER, "You are now viewing %" + LanguageFileTags.rideIdentifier + "% in visualize mode");
        setLanguageDefault(LanguageFileFields.COMMAND_VISUALIZE_REMOVED_VIEWER, "You are no longer viewing %" + LanguageFileTags.rideIdentifier + "% in visualize mode");

        setLanguageDefault(LanguageFileFields.NOTIFICATION_PLUGIN_STILL_LOADING, "Please try again later when jrides is loaded");

        setLanguageDefault(LanguageFileFields.NOTIFICATION_RIDE_CONTROL_ACTIVE, "You are now controlling %" + LanguageFileTags.rideDisplayName + "%");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_RIDE_CONTROL_INACTIVE, "You are no longer controlling %" + LanguageFileTags.rideDisplayName + "%");

        setLanguageDefault(LanguageFileFields.NOTIFICATION_RIDE_DISPATCH_PROBLEMS, "Cannot dispatch due to the following problems:");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_RIDE_NO_TRAIN_PRESENT, "No train present in station");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_RIDE_NEXT_BLOCK_OCCUPIED, "Next block section is occupied");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_RIDE_WAITING_TIME, "Waiting time has not passed yet");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_RIDE_RESTRAINTS_NOT_CLOSED, "Not all restraints are closed");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_RIDE_GATES_NOT_CLOSED, "Not all gates are closed");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_RIDE_GATE_NOT_CLOSED, "Gate %" + LanguageFileTags.name + "% is not closed");

        setLanguageDefault(LanguageFileFields.NOTIFICATION_RIDE_COUNTER_UPDATE, "\nYou've ridden %" + LanguageFileTags.rideDisplayName + "% %" + LanguageFileTags.rideCount + "% times now\n");

        setLanguageDefault(LanguageFileFields.NOTIFICATION_SHIFT_EXIT_CONFIRMATION, "Press shift again within 2 seconds to confirm exiting the ride");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_SHIFT_EXIT_CONFIRMED, "You just exited the ride while the restraints were closed");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_DISPATCH_WAIT_GENERIC, "Please wait until the ride is dispatched");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_DISPATCH_WAIT_SPECIFIC, "Waiting time: %" + LanguageFileTags.time + "% seconds");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_RESTRAINT_ON_EXIT_ATTEMPT, "The restraints are closed");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_RESTRAINT_ON_ENTER_ATTEMPT, "The restraints are closed");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_RESTRAINT_ENTER_OVERRIDE, "You just entered the ride while the restraints were closed");
        setLanguageDefault(LanguageFileFields.NOTIFICATION_CANNOT_ENTER_RIDE, "You currently cannot enter this ride. Try again later.");

        setLanguageDefault(LanguageFileFields.ELEVATED_OPERATOR_OVERRIDE_VICTIM_MESSAGE, "Player %" + LanguageFileTags.player + "% took over control of the operating cabin");

        setLanguageDefault(LanguageFileFields.ERROR_SMOOTH_COASTERS_DISABLED, "Smoother ride experience is disabled, please install SmoothCoasters.");
        setLanguageDefault(LanguageFileFields.ERROR_GENERAL_NO_PERMISSION_MESSAGE, "You do not have permissions to execute this action");
        setLanguageDefault(LanguageFileFields.ERROR_PLAYER_COMMAND_ONLY_MESSAGE, "Only players can execute this command");
        setLanguageDefault(LanguageFileFields.ERROR_UNKNOWN_COMMAND_MESSAGE, "Unknown jrides command. Type /jrides for help");
        setLanguageDefault(LanguageFileFields.ERROR_OPERATING_CABIN_OCCUPIED, "You can not take this operating cabin since it is already in use by another operator");
        setLanguageDefault(LanguageFileFields.ERROR_OPERATING_NO_PERMISSION, "You are not allowed to operate this ride");
        setLanguageDefault(LanguageFileFields.ERROR_RIDE_CONTROL_MENU_NOT_FOUND, "Ride control menu was not found");
        setLanguageDefault(LanguageFileFields.ERROR_RIDE_OVERVIEW_MAP_NOT_FOUND, "Could not retrieve map for ride %" + LanguageFileTags.rideIdentifier + "%. Was the map id configured?");

        setLanguageDefault(LanguageFileFields.BUTTON_CLAIM_CABIN, "Claim operating cabin");
        setLanguageDefault(LanguageFileFields.BUTTON_CABIN_CLAIMED, "Claim operating cabin");
        setLanguageDefault(LanguageFileFields.BUTTON_DISPATCH_STATE, "Dispatch");
        setLanguageDefault(LanguageFileFields.BUTTON_DISPATCH_PROBLEM_STATE, "Not allowed");
        setLanguageDefault(LanguageFileFields.BUTTON_PROBLEMS_STATE, "Problems");
        setLanguageDefault(LanguageFileFields.BUTTON_GATES_OPEN_STATE, "Gates are open");
        setLanguageDefault(LanguageFileFields.BUTTON_GATES_CLOSED_STATE, "Gates are closed");
        setLanguageDefault(LanguageFileFields.BUTTON_RESTRAINTS_OPEN_STATE, "Restraints are open");
        setLanguageDefault(LanguageFileFields.BUTTON_RESTRAINTS_CLOSED_STATE, "Restraints are closed");

        fillOverrides();
    }

    private void setLanguageDefault(LanguageFileFields field, String _default){
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
            LanguageFileFields field;
            try{
                field = LanguageFileFields.valueOf(keyString);
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

    public @Nonnull String get(@Nonnull LanguageFileFields field){
        String value = language.get(field);
        if(value == null)
            throw new RuntimeException("Language value for language field " + field.toString() + " could not be found");
        return value;
    }

    public void sendMessage(MessageReceiver messageReceiver, LanguageFileFields field){
        sendMessage(messageReceiver, field, defaultFeedbackType, null);
    }

    public void sendMessage(MessageReceiver messageReceiver, String content){
        sendMessage(messageReceiver, content, defaultFeedbackType, null);
    }

    public void sendMessage(MessageReceiver messageReceiver, LanguageFileFields field, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        sendMessage(messageReceiver, field, defaultFeedbackType, builder);
    }

    public void sendMessage(MessageReceiver messageReceiver, LanguageFileFields field, FeedbackType feedbackType) {
        sendMessage(messageReceiver, field, feedbackType, null);
    }

    public void sendMessage(MessageReceiver messageReceiver, LanguageFileFields field, FeedbackType feedbackType, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        String content = get(field);
        sendMessage(messageReceiver, content, feedbackType, builder);
    }

    public void sendMessage(MessageReceiver messageReceiver, String content, FeedbackType feedbackType, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        String prefix = getChatFeedbackColor(feedbackType) + get(LanguageFileFields.CHAT_FEEDBACK_PREFIX);
        sendMessage(messageReceiver, prefix, content, builder);
    }

    public void sendMessage(CommandSender commandSender, String content){
        sendMessage(new SimpleMessageReceiver(commandSender), content);
    }

    public void sendMessage(CommandSender commandSender, LanguageFileFields field){
        sendMessage(new SimpleMessageReceiver(commandSender), field);
    }

    public void sendMessage(CommandSender commandSender, LanguageFileFields field, FeedbackType feedbackType){
        sendMessage(new SimpleMessageReceiver(commandSender), field, feedbackType, null);
    }

    public void sendMessage(CommandSender commandSender, LanguageFileFields field, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        sendMessage(new SimpleMessageReceiver(commandSender), field, defaultFeedbackType, builder);
    }

    public void sendMultilineMessage(CommandSender commandSender, LanguageFileFields field){
        sendMultilineMessage(new SimpleMessageReceiver(commandSender), field);
    }

    public void sendMultilineMessage(CommandSender commandSender, LanguageFileFields field, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        sendMultilineMessage(new SimpleMessageReceiver(commandSender), get(field), defaultFeedbackType, builder);
    }

    public void sendMultilineMessage(CommandSender commandSender, String content){
        sendMultilineMessage(new SimpleMessageReceiver(commandSender), content);
    }

    public void sendMultilineMessage(CommandSender commandSender, String content, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        sendMultilineMessage(new SimpleMessageReceiver(commandSender), content, defaultFeedbackType, builder);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, LanguageFileFields field){
        sendMultilineMessage(messageReceiver, field, defaultFeedbackType);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, LanguageFileFields field, FeedbackType feedbackType){
        sendMultilineMessage(messageReceiver, get(field), feedbackType, null);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, LanguageFileFields field, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        sendMultilineMessage(messageReceiver, get(field), defaultFeedbackType, builder);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, String content){
        sendMultilineMessage(messageReceiver, content, defaultFeedbackType);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, String content, FeedbackType feedbackType){
        sendMultilineMessage(messageReceiver, content, feedbackType, null);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, String content, FeedbackType feedbackType, Function<StringReplacementBuilder, StringReplacementBuilder> builder){
        String prefix = getChatFeedbackColor(feedbackType);
        sendMessage(messageReceiver, prefix, content, builder);
    }

    public void sendMessage(MessageReceiver messageReceiver, String prefix, String content, Function<StringReplacementBuilder, StringReplacementBuilder> builderFunction){
        StringReplacementBuilder builder = new StringReplacementBuilder();
        if(builderFunction != null) builder = builderFunction.apply(builder);

        content = builder.apply(content);

        messageReceiver.sendMessage(prefix + content);
    }

    private String getChatFeedbackColor(FeedbackType feedbackType){
        String chatFeedbackColor;
        if(feedbackType.equals(FeedbackType.INFO)) chatFeedbackColor = get(LanguageFileFields.CHAT_FEEDBACK_INFO_COLOR);
        else if(feedbackType.equals(FeedbackType.WARNING)) chatFeedbackColor = get(LanguageFileFields.CHAT_FEEDBACK_WARNING_COLOR);
        else if(feedbackType.equals(FeedbackType.SEVERE)) chatFeedbackColor = get(LanguageFileFields.CHAT_FEEDBACK_SEVERE_COLOR);
        else if(feedbackType.equals(FeedbackType.CONFLICT)) chatFeedbackColor = get(LanguageFileFields.CHAT_FEEDBACK_WARNING_COLOR);
        else throw new RuntimeException("Unsupported feedback type");

        return chatFeedbackColor;
    }
}
