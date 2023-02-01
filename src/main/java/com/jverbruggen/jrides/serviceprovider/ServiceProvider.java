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
