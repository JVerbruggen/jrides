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
