package com.jverbruggen.jrides.common.startup;

import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

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
        ServiceProvider.getSingleton(Logger.class).info(splashLogo +
                enabled + " - " + version);
    }

    public static void sendDisabledMessage(String version){
        ServiceProvider.getSingleton(Logger.class).info(splashLogo +
                disabled + " - " + version);
    }
}
