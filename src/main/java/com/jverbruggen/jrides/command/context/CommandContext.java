package com.jverbruggen.jrides.command.context;

import java.util.HashMap;
import java.util.Map;

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
        return (T) objects.get(clazz);
    }
}
