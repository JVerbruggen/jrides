package com.jverbruggen.jrides.models.entity;

/**
 * Creates random entity id in range from a - b
 */
public class EntityIdFactory {
    private final int upper;
    private int next;

    public EntityIdFactory(int lower, int upper) {
        this.upper = upper;
        this.next = lower;
    }

    private int next(){
        int val = this.next++;
        if(val > upper){
            throw new IllegalStateException("Entity ID factory overflowing, restart the server!");
        }
        return val;
    }

    public int newId(){
        return next();
    }
}
