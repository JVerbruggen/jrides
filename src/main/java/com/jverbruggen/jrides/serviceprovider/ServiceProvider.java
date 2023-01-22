package com.jverbruggen.jrides.serviceprovider;

import java.util.function.Function;

public interface ServiceProvider {
    <T> T getSingleton(Class<T> type);

    void register(Class type, Object instance);
    <T extends ServiceProvider, R> void register(Class<R> type, Function<T, R> function);

    ServiceProvider instance = new HashMapServiceProvider();

    static <T> T GetSingleton(Class<T> type){
        return instance.getSingleton(type);
    }

    static <R> R Register(Class<R> type, R o){
        instance.register(type, o);
        return o;
    }

    static <T extends ServiceProvider, R> void RegisterWith(Class<R> type, Function<T, R> function){
        instance.register(type, function);
    }
}
