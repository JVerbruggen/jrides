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

package com.jverbruggen.jrides.command.context;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class CommandContext {
    private final Map<Class, Object> objects;

    public CommandContext() {
        this.objects = new HashMap<>();
    }

    public CommandContext add(Class clazz, Object object){
        objects.put(clazz, object);
        return this;
    }

    public <T> T get(Class<T> clazz){
        Object object = objects.get(clazz);
        if(!clazz.isInstance(object)) throw new RuntimeException("Non-matching type in command context for type " + clazz.getTypeName());
        return clazz.cast(object);
    }
}
