/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.effect.handle.cart;

import com.jverbruggen.jrides.effect.cart.CartEffectTrigger;
import com.jverbruggen.jrides.effect.handle.EffectTriggerHandle;
import com.jverbruggen.jrides.models.properties.frame.Frame;
import com.jverbruggen.jrides.models.ride.coaster.train.CoasterCart;

public interface CartEffectTriggerHandle extends EffectTriggerHandle {
    CartEffectTrigger getCartEffectTrigger();

    /**
     *
     * @param cart
     * @return true if can process more effects
     */
    boolean executeForCart(CoasterCart cart);

    void setNext(CartEffectTriggerHandle cartEffectTriggerHandle);
    CartEffectTriggerHandle next();
}
