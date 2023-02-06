package com.jverbruggen.jrides.config.coaster.objects;

import com.jverbruggen.jrides.config.coaster.objects.cart.CartTypeSpecConfig;
import org.bukkit.configuration.ConfigurationSection;

public class CartSpecConfig {
    private final CartTypeSpecConfig _default;
    private final CartTypeSpecConfig head;
    private final CartTypeSpecConfig tail;

    public CartSpecConfig(CartTypeSpecConfig _default, CartTypeSpecConfig head, CartTypeSpecConfig tail) {
        this._default = _default;
        this.head = head;
        this.tail = tail;
    }

    public CartTypeSpecConfig getDefault() {
        return _default;
    }

    public CartTypeSpecConfig getHead() {
        return head;
    }

    public CartTypeSpecConfig getTail() {
        return tail;
    }

    public boolean hasHead(){
        return head != null;
    }

    public boolean hasTail(){
        return tail != null;
    }

    public static CartSpecConfig fromConfigurationSection(ConfigurationSection configurationSection) {
        CartTypeSpecConfig _default = CartTypeSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("default"));
        CartTypeSpecConfig head = null;
        CartTypeSpecConfig tail = null;

        if(configurationSection.contains("head")){
            head = CartTypeSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("head"));
        }
        if(configurationSection.contains("tail")){
            tail = CartTypeSpecConfig.fromConfigurationSection(configurationSection.getConfigurationSection("tail"));
        }

        return new CartSpecConfig(_default, head, tail);
    }
}

