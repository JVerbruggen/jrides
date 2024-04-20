package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLockCollection;
import com.jverbruggen.jrides.models.entity.agent.MessageAgent;

import java.util.List;

public class MultiDispatchTrigger implements DispatchTrigger {
    private final List<DispatchTrigger> dispatchTriggerList;
    private final DispatchLockCollection dispatchLockCollection;

    public MultiDispatchTrigger(List<DispatchTrigger> dispatchTriggerList, DispatchLockCollection dispatchLockCollection) {
        this.dispatchTriggerList = dispatchTriggerList;
        this.dispatchLockCollection = dispatchLockCollection;
    }

    @Override
    public boolean execute(MessageAgent messageAgent){
        if(!canExecute(messageAgent))
            return false;

        for(DispatchTrigger dispatchTrigger : dispatchTriggerList){
            dispatchTrigger.execute(messageAgent);
        }

        return true;
    }

    @Override
    public boolean canExecute(MessageAgent messageAgent) {
        boolean canExecute = true;

        for(DispatchTrigger dispatchTrigger : dispatchTriggerList){
            if(!dispatchTrigger.canExecute(messageAgent))
                canExecute = false;
        }

        return canExecute;
    }

    @Override
    public void reset(){
        for(DispatchTrigger dispatchTrigger : dispatchTriggerList){
            dispatchTrigger.reset();
        }
    }

    @Override
    public boolean isActive(){
        return dispatchTriggerList.stream().anyMatch(DispatchTrigger::isActive);
    }

    @Override
    public DispatchLockCollection getDispatchLockCollection() {
        return dispatchLockCollection;
    }
}
