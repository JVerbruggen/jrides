package com.jverbruggen.jrides.control.uiinterface.menu.button;

import com.jverbruggen.jrides.models.menu.ButtonVisual;
import com.jverbruggen.jrides.models.menu.MenuButton;
import org.bukkit.Sound;

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

    @Override
    public Sound getPressedSound() {
        return Sound.UI_BUTTON_CLICK;
    }
}
