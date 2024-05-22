package com.jverbruggen.jrides.effect.handle.cart;

import com.jverbruggen.jrides.effect.cart.CartEffectTrigger;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;
import com.jverbruggen.jrides.models.ride.coaster.train.Train;
import com.jverbruggen.jrides.models.ride.section.Section;

public abstract class BaseCartEffectTriggerHandle implements CartEffectTriggerHandle {
    protected final Frame frame;
    protected final CartEffectTrigger cartEffectTrigger;
    protected CartEffectTriggerHandle nextCartEffectTriggerHandle;

    public BaseCartEffectTriggerHandle(Frame frame, CartEffectTrigger cartEffectTrigger) {
        this.frame = frame;
        this.cartEffectTrigger = cartEffectTrigger;
        this.nextCartEffectTriggerHandle = null;
    }

    @Override
    public boolean shouldPlay(Frame forFrame) {
        Section section = forFrame.getSection();
        return this.getFrame().getSection().equals(section)
                && section.hasPassed(this.getFrame(), forFrame);
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public CartEffectTrigger getCartEffectTrigger() {
        return cartEffectTrigger;
    }

    @Override
    public void setNext(CartEffectTriggerHandle cartEffectTriggerHandle) {
        nextCartEffectTriggerHandle = cartEffectTriggerHandle;
    }

    @Override
    public CartEffectTriggerHandle next() {
        return nextCartEffectTriggerHandle;
    }

    @Override
    public String toString() {
        return "<handle to " + cartEffectTrigger + ">";
    }

    @Override
    public void execute(Train train, CoasterCart cart) {
        executeForCart(cart);
    }
}
