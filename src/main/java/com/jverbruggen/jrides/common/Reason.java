package com.jverbruggen.jrides.common;

import com.jverbruggen.jrides.language.FeedbackType;
import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

public record Reason(String description, FeedbackType feedbackType) {
    public void sendAsMessage(MessageAgent messageAgent){
        LanguageFile languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        languageFile.sendMessage(messageAgent, description);
    }

    public static Reason simple(String description){
        return new Reason(description, FeedbackType.INFO);
    }

    public static Reason conflict(String description){
        return new Reason(description, FeedbackType.CONFLICT);
    }
}
