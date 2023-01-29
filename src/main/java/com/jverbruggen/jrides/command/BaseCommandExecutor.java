package com.jverbruggen.jrides.command;

import com.jverbruggen.jrides.language.LanguageFile;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.player.PlayerManager;

public abstract class BaseCommandExecutor implements JRidesCommandExecutor {
    protected final LanguageFile languageFile;
    protected final PlayerManager playerManager;

    protected BaseCommandExecutor() {
        this.languageFile = ServiceProvider.getSingleton(LanguageFile.class);
        this.playerManager = ServiceProvider.getSingleton(PlayerManager.class);
    }
}
