package com.jverbruggen.jrides.animator.flatride;

public class FlatRideComponentSpeed {
    private float speed;
    private float minSpeed;
    private float maxSpeed;

    public FlatRideComponentSpeed(float speed, float minSpeed, float maxSpeed) {
        if(minSpeed > maxSpeed) throw new RuntimeException("Min speed has to be lower than max speed");
        else if(speed > maxSpeed) throw new RuntimeException("Speed has to be lower than max speed");
        else if(speed < minSpeed) throw new RuntimeException("Speed has to be higher than min speed");

        this.speed = speed;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    public FlatRideComponentSpeed(float speed){
        this.speed = 0;
        this.minSpeed = 0;
        this.maxSpeed = speed;
    }

    /**
     * Discouraged. This is an abrupt speed change.
     * Only use when absolutely necessary.
     */
    public void setHard(float speed){
        this.speed = speed;
    }

    public void accelerate(float acceleration){
        this.speed += acceleration;
        if(this.speed > this.maxSpeed) this.speed = this.maxSpeed;
        else if(this.speed < this.minSpeed) this.speed = this.minSpeed;
    }

    public void accelerateTowards(float acceleration, float towards){
        if(this.speed > towards){
            this.speed -= acceleration;
            if(this.speed < towards) this.speed = towards;
        }else if(this.speed < towards){
            this.speed += acceleration;
            if(this.speed > towards) this.speed = towards;
        }

        if(this.speed > this.maxSpeed) this.speed = this.maxSpeed;
        else if(this.speed < this.minSpeed) this.speed = this.minSpeed;
    }

    public float getSpeed() {
        return speed;
    }

    public float getMinSpeed() {
        return minSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public FlatRideComponentSpeed clone(){
        return new FlatRideComponentSpeed(this.speed, this.minSpeed, this.maxSpeed);
    }
}
