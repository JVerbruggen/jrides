package com.jverbruggen.jrides.common.startup;

import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

import java.util.logging.Logger;

public class StartMessage {
    private static String splashLogo = "\n" +
            "    _      _     _           \n" +
            "   (_)_ __(_) __| | ___  ___ \n" +
            "   | | '__| |/ _` |/ _ \\/ __|\n" +
            "   | | |  | | (_| |  __/\\__ \\\n" +
            "  _/ |_|  |_|\\__,_|\\___||___/\n" +
            " |__/                        \n";
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
