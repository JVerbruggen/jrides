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

package com.jverbruggen.jrides.common;

import com.jverbruggen.jrides.models.entity.agent.MessageAgent;

import java.util.ArrayList;
import java.util.List;

public record Result(boolean ok, List<Reason> reasons) {
    public void sendMessageTo(MessageAgent messageAgent){
        for(Reason reason : reasons){
            reason.sendAsMessage(messageAgent);
        }
    }

    public void concat(Result other){
        reasons.addAll(other.reasons);
    }

    public static Result isOk(){
        return new Result(true, new ArrayList<>());
    }

    public static Result isNotOk() {
        return new Result(false, new ArrayList<>());
    }

    public static Result isNotOk(String title){
        return isNotOk(title, List.of());
    }

    public static Result isNotOk(List<String> reasons){
        return isNotOk(null, reasons);
    }

    public static Result isNotOk(String title, List<String> reasons){
        List<Reason> reasonList = new ArrayList<>();

        if(title != null)
            reasonList.add(Reason.conflict(title));

        for(String reasonString : reasons){
            reasonList.add(Reason.simple(reasonString));
        }

        return new Result(false, reasonList);
    }
}
