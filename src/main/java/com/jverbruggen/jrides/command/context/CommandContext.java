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
