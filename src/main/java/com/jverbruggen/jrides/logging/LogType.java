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
    public static final LogType CRASH = new LogType("CRASH");
    public static final LogType DISPATCH = new LogType("DISPATCH");
}
