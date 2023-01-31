package com.jverbruggen.jrides.language;

import java.util.HashMap;
import java.util.Map;

public class StringReplacementBuilder {
    private final Map<String, String> replacements;

    public StringReplacementBuilder() {
        this.replacements = new HashMap<>();
    }

    public StringReplacementBuilder add(String tag, String replacement){
        replacements.put(tag, replacement);
        return this;
    }

    public Map<String, String> collect(){
        return replacements;
    }

    public String apply(String input){
        String output = input;

        if(replacements.size() > 0){
            for(Map.Entry<String, String> replacement : replacements.entrySet()){
                String tag = replacement.getKey();
                output = output.replace("%" + tag + "%", replacement.getValue());
            }
        }

        return output;
    }
}
