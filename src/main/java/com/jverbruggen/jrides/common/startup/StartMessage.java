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

package com.jverbruggen.jrides.common.startup;

import com.jverbruggen.jrides.JRidesPlugin;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;

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

    public static void sendEnabledMessage(){
        Logger logger = ServiceProvider.getSingleton(Logger.class);
        sendSplash(logger);
        logger.info("\u001b[32m" + enabled + " - " + JRidesPlugin.getVersion() + "\u001b[0m");
    }

    public static void sendDisabledMessage(){
        Logger logger = ServiceProvider.getSingleton(Logger.class);
        sendSplash(logger);
        logger.info("\u001b[31m" + disabled + " - " + JRidesPlugin.getVersion() + "\u001b[0m");
    }

    private static void sendSplash(Logger logger){
        List.of(splashLogo.split("\n")).forEach(line -> logger.info("\u001b[33m" + line + "\u001b[0m"));
    }
}
