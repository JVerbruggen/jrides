package com.jverbruggen.jrides.exception;

public class CoasterLoadException extends Exception {
    public CoasterLoadException(){
        super("Coaster could not load because something went wrong during initialization");
    }
}
