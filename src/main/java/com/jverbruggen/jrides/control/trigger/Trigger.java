package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.models.entity.MessageReceiver;

public interface Trigger {
    boolean execute(MessageReceiver messageReceiver);
}
