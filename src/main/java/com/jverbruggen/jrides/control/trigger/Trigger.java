package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.models.entity.agent.MessageAgent;

public interface Trigger {
    boolean execute(MessageAgent messageReceiver);
}
