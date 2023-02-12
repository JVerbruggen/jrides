package com.jverbruggen.jrides.models.properties;

import org.bukkit.Bukkit;

public class Speed {
    private double speedPerTick;
    private boolean inverted;

    public Speed(double speed) {
        this.speedPerTick = speed;
        this.inverted = false;
    }

    public Speed(double speed, boolean inverted) {
        this.speedPerTick = speed;
        this.inverted = inverted;
    }

    public void setSpeed(double newSpeedPerTick){
        if(inverted) newSpeedPerTick = -newSpeedPerTick;
        this.speedPerTick = newSpeedPerTick;
    }

    public double getSpeedPerTick() {
        if(inverted) return -speedPerTick;
        return speedPerTick;
    }

    public void add(double speed){
        this.speedPerTick += speed;
    }

    public void add(double speed, double until){
        add(speed);

        if(this.speedPerTick > until)
            this.speedPerTick = until;
    }

    public void approach(double acceleration, double deceleration, double approachSpeed){
        if(this.speedPerTick > approachSpeed){
            minus(deceleration, approachSpeed);
        }else if(this.speedPerTick < approachSpeed){
            add(acceleration, approachSpeed);
        }
    }

    public void minus(double speed){
        add(-speed);
    }

    public void minus(double speed, double until){
        minus(speed);
        if(this.speedPerTick < until) this.speedPerTick = until;
    }

    public void multiply(double speed){
        this.speedPerTick *= speed;
    }

    public int getFrameIncrement(){
        final int frameIncrementFactor = 3;
        final double speedPerTick = getSpeedPerTick();

        return (int) (speedPerTick * frameIncrementFactor);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Speed clone(){
        return new Speed(this.speedPerTick, this.inverted);
    }

    public boolean isZero(){
        return this.speedPerTick == 0;
    }

    public boolean isPositive(){
        return this.speedPerTick >= 0;
    }

    public void setInverted(boolean inverted) {
//        Bukkit.broadcastMessage("Set inverted " + inverted);
        this.inverted = inverted;
    }

    public boolean isInverted() {
        return inverted;
    }

    @Override
    public String toString() {
        return speedPerTick + "b/t";
    }
}
