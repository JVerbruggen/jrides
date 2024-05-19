package com.jverbruggen.jrides.models.properties;

import org.bukkit.Bukkit;

public class Speed {
    private static int FRACTIONAL_SPEED_COUNTER = 0;
    private static final int FRAME_INCREMENT_FACTOR = 3;

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

    private int getFractionalFrameIncrement(double frameIncrement){ // 0.9
        int enlargedIncrement = (int) (FRACTIONAL_SPEED_COUNTER * frameIncrement * 100d); // 90

        if(enlargedIncrement % 100 > (frameIncrement * 100d))
            return 0;
        else
            return 1;
    }

    public int getFrameIncrement(){
        final double speedPerTick = getSpeedPerTick();

        double frameIncrement = speedPerTick * FRAME_INCREMENT_FACTOR;
        if(0.2 < frameIncrement && frameIncrement < 1){
            frameIncrement = getFractionalFrameIncrement(frameIncrement);
        }else if(-1 < frameIncrement && frameIncrement < -0.2){
            frameIncrement = -getFractionalFrameIncrement(frameIncrement);
        }

        return (int) frameIncrement;
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
//        Bukkit.broadcastMessage("Set SPD inverted " + inverted);
        this.inverted = inverted;
    }

    public boolean isInverted() {
        return inverted;
    }

    public boolean isGoingForwards(){
        return isPositive() && !isInverted();
    }

    @Override
    public String toString() {
        String invertedFlag = inverted ? " [I]" : "";
        return speedPerTick + "b/t" + invertedFlag;
    }

    public static void incrementFractionalSpeedCounter(){
        FRACTIONAL_SPEED_COUNTER++;
        if(FRACTIONAL_SPEED_COUNTER >= 1000)
            FRACTIONAL_SPEED_COUNTER = 0;
    }
}
