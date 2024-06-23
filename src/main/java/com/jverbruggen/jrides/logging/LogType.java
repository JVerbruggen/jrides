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

package com.jverbruggen.jrides.logging;

public class LogType {
    private final String name;

    public LogType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof LogType)
                && ((LogType) obj).getName().equalsIgnoreCase(getName());
    }

    public static final LogType SECTIONS = new LogType("SECTIONS");
    public static final LogType SECTIONS_DETAIL = new LogType("SECTIONS_DETAIL");
    public static final LogType CRASH = new LogType("CRASH");
    public static final LogType PACKET = new LogType("PACKET");
}
