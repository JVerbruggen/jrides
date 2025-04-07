/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.language;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

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

    public @Nonnull String apply(@Nonnull String input){
        String output = input;

        if(replacements.size() > 0){
            for(Map.Entry<String, String> replacement : replacements.entrySet()){
                String tag = replacement.getKey();
                output = output.replace("%" + tag + "%", replacement.getValue());
            }
        }

        if(output == null) throw new RuntimeException("Unexpected output = null when applying input on StringReplacementBuilder");

        return ChatColor.translateAlternateColorCodes('&', output);
    }
}
