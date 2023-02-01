package com.jverbruggen.jrides.serviceprovider;

import java.util.HashMap;
import java.util.function.Function;

@SuppressWarnings("rawtypes")
public class HashMapServiceProvider implements ServiceProvider {

    private HashMap<Class, Object> instances;

    public HashMapServiceProvider() {
        this.instances = new HashMap<>();
    }

    @Override
    public <T> T _getSingleton(Class<T> type) {
        if(!instances.containsKey(type)) throw new RuntimeException("Type " + type.getTypeName() + " was not registered to the ServiceProvider");

        Object object = instances.get(type);
        return (T) object;
    }

    @Override
    public void _register(Class type, Object instance) {
        instances.put(type, instance);
    }

    @Override
    public <T extends ServiceProvider, R> void _register(Class<R> type, Function<T, R> function) {
        function.apply((T) this);
    }
}
