package com.jverbruggen.jrides.control.uiinterface.menu.button;

import com.jverbruggen.jrides.control.uiinterface.menu.button.common.ButtonVisual;

public abstract class BaseRideControlButton implements RideControlButton {
    protected void setButtonVisual(ButtonVisual visual){
        visual.clearUpdate();

        if(visual.needsFullItemStackReload()){
            setItemStack(visual.toItemStack());
            return;
        }

        changeMaterial(visual.getButtonMaterial());
        changeDisplayName(visual.getButtonDisplayNameColor() + visual.getValue());
        changeLore(visual.getLore());
    }
}
