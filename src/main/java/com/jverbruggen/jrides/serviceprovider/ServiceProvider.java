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

package com.jverbruggen.jrides.serviceprovider;

import java.util.function.Function;

@SuppressWarnings("rawtypes")
public interface ServiceProvider {
    <T> T _getSingleton(Class<T> type);

    void _register(Class type, Object instance);
    <T extends ServiceProvider, R> void _register(Class<R> type, Function<T, R> function);

    ServiceProvider instance = new HashMapServiceProvider();

    static <T> T getSingleton(Class<T> type){
        return instance._getSingleton(type);
    }

    static <R> R register(Class<R> type, R o){
        instance._register(type, o);
        return o;
    }

    static <T extends ServiceProvider, R> void registerWith(Class<R> type, Function<T, R> function){
        instance._register(type, function);
    }
}
