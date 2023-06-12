package com.jverbruggen.jrides.control.uiinterface.menu.button;

import com.jverbruggen.jrides.models.menu.ButtonVisual;
import com.jverbruggen.jrides.models.menu.MenuButton;

public abstract class BaseRideControlButton implements MenuButton {
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
