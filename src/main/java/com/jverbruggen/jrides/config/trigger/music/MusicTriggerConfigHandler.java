package com.jverbruggen.jrides.config.trigger.music;

public enum MusicTriggerConfigHandler {
    EVENT,
    RESOURCE_PACK,
    SOME_AUDIO_PLUGIN;

    public static MusicTriggerConfigHandler fromString(String value){
       if(value.equalsIgnoreCase("event")) return EVENT;
       else if(value.equalsIgnoreCase("resource_pack")) return RESOURCE_PACK;

       throw new RuntimeException("Music trigger type " + value + " is not supported");
    }
}
