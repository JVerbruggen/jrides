package com.jverbruggen.jrides.control.trigger;

import com.jverbruggen.jrides.control.DispatchLockCollection;

public interface DispatchTrigger extends Trigger {
    void reset();
    boolean isActive();
    DispatchLockCollection getDispatchLockCollection();
}
