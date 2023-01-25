package com.jverbruggen.jrides.models.properties;

public class Speed {
    private double speedPerTick;

    public Speed(double speed) {
        this.speedPerTick = speed;
    }

    public double getSpeedPerTick() {
        return speedPerTick;
    }

    public void setSpeedPerTick(double speed){
        this.speedPerTick = speed;
    }

    public void add(double speed){
        this.speedPerTick += speed;
    }

    public void add(double speed, double until){
        add(speed);
        if(this.speedPerTick > until) this.speedPerTick = until;
    }

    public void approach(double acceleration, double deceleration, double approachSpeed){
        if(this.speedPerTick > approachSpeed){
            minus(deceleration, approachSpeed);
        }else if(this.speedPerTick < approachSpeed){
            add(acceleration, approachSpeed);
        }
    }

    public void minus(double speed){
        this.speedPerTick -= speed;
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
        return (int) (speedPerTick * frameIncrementFactor);
    }

    public Speed clone(){
        return new Speed(this.speedPerTick);
    }

    public boolean is(double speed){
        return this.speedPerTick == speed;
    }

    @Override
    public String toString() {
        return speedPerTick + "b/t";
    }
}
