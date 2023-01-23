package com.jverbruggen.jrides.animator.smoothanimation;

import com.jverbruggen.jrides.models.entity.Player;
import com.jverbruggen.jrides.models.math.Quaternion;

public interface SmoothAnimation {
    boolean isEnabled(Player player);
    void clearRotation(Player player);
    void setRotation(Player player, Quaternion orientation);
}
