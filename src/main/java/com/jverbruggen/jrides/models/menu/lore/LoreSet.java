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

package com.jverbruggen.jrides.models.menu.lore;

import com.jverbruggen.jrides.api.JRidesPlayer;

import java.util.ArrayList;
import java.util.List;

public class LoreSet {
    private final List<Lore> lores;

    public LoreSet(List<Lore> lores) {
        this.lores = lores;
    }

    public LoreSet(){
        this.lores = new ArrayList<>();
    }

    public void add(Lore lore){
        lores.add(lore);
    }

    public LoreSet clone(){
        return new LoreSet(new ArrayList<>(lores));
    }

    public static LoreSet fromStringList(List<String> stringList){
        return new LoreSet(new ArrayList<>(stringList.stream().map(s -> (Lore) new TextLore(s)).toList()));
    }

    public static LoreSet empty(){
        return new LoreSet();
    }

    public List<String> resolve(JRidesPlayer player){
        return new ArrayList<>(lores.stream().map(l -> l.resolveLore(player)).toList());
    }

    public int size(){
        return lores.size();
    }
}
