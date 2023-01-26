package com.jverbruggen.jrides.models.properties;

public class DebounceCall {
    private final int allowEveryXCalls;
    private int callState;

    public DebounceCall(int allowEveryXCalls) {
        this.allowEveryXCalls = allowEveryXCalls;
    }

    public boolean run(Runnable runnable){
        if(callState < allowEveryXCalls){
            callState++;
            return false;
        }
        runnable.run();
        reset();
        return true;
    }

    public boolean hasDebounceProgress(){
        return callState > 0;
    }

    public void reset(){
        this.callState = 0;
    }
}
