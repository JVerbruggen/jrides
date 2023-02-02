package com.jverbruggen.jrides.common.startup;

import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.logging.Logger;

public class StartMessage {
    private static String splashLogo = """
                _      _     _          \s
               (_)_ __(_) __| | ___  ___\s
               | | '__| |/ _` |/ _ \\/ __|
               | | |  | | (_| |  __/\\__ \\
              _/ |_|  |_|\\__,_|\\___||___/
             |__/                       \s
            """;
    private static String enabled = "        E N A B L E D";
    private static String disabled = "        D I S A B L E D";

    public static void sendEnabledMessage(String version){
        Logger logger = ServiceProvider.getSingleton(Logger.class);
        sendSplash(logger);
        logger.info("\u001b[32m" + enabled + " - " + version);
    }

    public static void sendDisabledMessage(String version){
        Logger logger = ServiceProvider.getSingleton(Logger.class);
        sendSplash(logger);
        logger.info("\u001b[31m" + disabled + " - " + version);
    }

    private static void sendSplash(Logger logger){
        List.of(splashLogo.split("\n")).forEach(line -> logger.info("\u001b[33m" + line));
    }
}
