package com.jverbruggen.jrides.exception;

public class NoTrackSpecifiedException extends CoasterLoadException {
    public NoTrackSpecifiedException(){
        super("No track was specified in coaster.yml");
    }

    public NoTrackSpecifiedException(String s){
        super("No track specified: " + s);
    }
}
