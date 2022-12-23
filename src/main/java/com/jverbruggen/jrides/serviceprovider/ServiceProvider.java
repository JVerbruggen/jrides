package com.jverbruggen.jrides.serviceprovider;

import java.util.function.Function;

public interface ServiceProvider {
    <T> T getSingleton(Class type);

    void register(Class type, Object instance);
    <T extends ServiceProvider, R> void register(Class<R> type, Function<T, R> function);

    ServiceProvider instance = new HashMapServiceProvider();

    static <T> T GetSingleton(Class type){
        return instance.getSingleton(type);
    }

    static void Register(Class type, Object o){
        instance.register(type, o);
    }

    static <T extends ServiceProvider, R> void Register(Class<R> type, Function<T, R> function){
        instance.register(type, function);
    }
}
