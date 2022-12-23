package com.jverbruggen.jrides.serviceprovider;

import java.util.HashMap;
import java.util.function.Function;

public class HashMapServiceProvider implements ServiceProvider {

    private HashMap<Class, Object> instances;

    public HashMapServiceProvider() {
        this.instances = new HashMap<>();
    }

    @Override
    public <T> T getSingleton(Class type) {
        Object instance = instances.get(type);
        return (T) instance;
    }

    @Override
    public void register(Class type, Object instance) {
        instances.put(type, instance);
    }

    @Override
    public <T extends ServiceProvider, R> void register(Class<R> type, Function<T, R> function) {
        function.apply((T) this);
    }
}
