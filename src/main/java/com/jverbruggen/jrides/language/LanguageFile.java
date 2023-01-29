package com.jverbruggen.jrides.language;

import com.jverbruggen.jrides.models.entity.MessageReceiver;
import com.jverbruggen.jrides.models.entity.SimpleMessageReceiver;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class LanguageFile {
    private FeedbackType defaultFeedbackType = FeedbackType.INFO;

    public String chatFeedbackPrefix = "[jrides]";
    public String chatFeedbackInfoColor = ChatColor.GRAY + "";
    public String chatFeedbackWarningColor = ChatColor.YELLOW + "";
    public String chatFeedbackSevereColor = ChatColor.RED + "";

    public String commandRideDispatchedMessage = "Ride %RIDE_IDENTIFIER% was dispatched!";

    public String commandVisualizeAddedViewer = "You are now viewing %RIDE_IDENTIFIER% in visualize mode";
    public String commandVisualizeRemovedViewer = "You are no longer viewing %RIDE_IDENTIFIER% in visualize mode";

    public String errorSmoothCoastersDisabled = "Smoother ride experience is disabled, please install SmoothCoasters.";
    public String errorGeneralNoPermissionMessage = "You do not have permissions to execute this action";
    public String errorPlayerCommandOnlyMessage = "Only players can execute this command";
    public String errorUnknownCommandMessage = "Unknown jrides command. Type /jrides for help";

    public void sendMessage(MessageReceiver messageReceiver, String content){
        sendMessage(messageReceiver, content, defaultFeedbackType, null);
    }

    public void sendMessage(MessageReceiver messageReceiver, String content, Map<String, String> replacements){
        sendMessage(messageReceiver, content, defaultFeedbackType, replacements);
    }

    public void sendMessage(MessageReceiver messageReceiver, String content, FeedbackType feedbackType, Map<String, String> replacements){
        String prefix = getChatFeedbackColor(feedbackType) + chatFeedbackPrefix;
        sendMessage(messageReceiver, prefix, content, replacements);
    }

    public void sendMessage(CommandSender commandSender, String content){
        sendMessage(new SimpleMessageReceiver(commandSender), content);
    }

    public void sendMessage(CommandSender commandSender, String content, FeedbackType feedbackType){
        sendMessage(new SimpleMessageReceiver(commandSender), content, feedbackType, null);
    }

    public void sendMessage(CommandSender commandSender, String content, Map<String, String> replacements){
        sendMessage(new SimpleMessageReceiver(commandSender), content, defaultFeedbackType, replacements);
    }

    public void sendMultilineMessage(CommandSender commandSender, String content){
        sendMultilineMessage(new SimpleMessageReceiver(commandSender), content);
    }

    public void sendMultilineMessage(CommandSender commandSender, String content, Map<String, String> replacements){
        sendMultilineMessage(new SimpleMessageReceiver(commandSender), content);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, String content){
        sendMultilineMessage(messageReceiver, content, defaultFeedbackType);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, String content, FeedbackType feedbackType){
        sendMultilineMessage(messageReceiver, content, feedbackType, null);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, String content, Map<String, String> replacements){
        sendMultilineMessage(messageReceiver, content, defaultFeedbackType, replacements);
    }

    public void sendMultilineMessage(MessageReceiver messageReceiver, String content, FeedbackType feedbackType, Map<String, String> replacements){
        String prefix = getChatFeedbackColor(feedbackType);
        sendMessage(messageReceiver, prefix, content, replacements);
    }

    public void sendMessage(MessageReceiver messageReceiver, String prefix, String content, Map<String, String> replacements){
        if(replacements != null && replacements.size() > 0){
            for(Map.Entry<String, String> replacement : replacements.entrySet()){
                String tag = replacement.getKey();
                content = content.replaceAll("%\\%" + tag + "\\%%gm", replacement.getValue());
            }
        }
        messageReceiver.sendMessage(prefix + content);
    }

    private String getChatFeedbackColor(FeedbackType feedbackType){
        String chatFeedbackColor;
        if(feedbackType.equals(FeedbackType.INFO)) chatFeedbackColor = chatFeedbackInfoColor;
        else if(feedbackType.equals(FeedbackType.WARNING)) chatFeedbackColor = chatFeedbackWarningColor;
        else if(feedbackType.equals(FeedbackType.SEVERE)) chatFeedbackColor = chatFeedbackSevereColor;
        else throw new RuntimeException("Unsupported feedback type");

        return chatFeedbackColor;
    }
}
