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
        this.speed = speed;
        this.minSpeed = speed;
        this.maxSpeed = speed;
    }

    public void accelerate(float acceleration){
        this.speed += acceleration;
        if(this.speed > this.maxSpeed) this.speed = this.maxSpeed;
        else if(this.speed < this.minSpeed) this.speed = this.minSpeed;
    }

    public float getSpeed() {
        return speed;
    }

    public FlatRideComponentSpeed clone(){
        return new FlatRideComponentSpeed(this.speed, this.minSpeed, this.maxSpeed);
    }
}