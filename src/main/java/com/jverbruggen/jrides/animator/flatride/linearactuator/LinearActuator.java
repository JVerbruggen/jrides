package com.jverbruggen.jrides.animator.flatride.linearactuator;

import com.jverbruggen.jrides.animator.flatride.AbstractInterconnectedFlatRideComponent;
import com.jverbruggen.jrides.animator.flatride.FlatRideComponentSpeed;
import com.jverbruggen.jrides.animator.flatride.attachment.Attachment;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.models.math.Vector3;

import java.util.List;

public class LinearActuator extends AbstractInterconnectedFlatRideComponent {
    private static final float PI = 3.1415926535f;
    private static final float PI2 = PI*2;

    private final FlatRideComponentSpeed flatRideComponentSpeed;
    private final Vector3 actuatorState;
    private final float sineArm;

    private float sineState;

    public LinearActuator(String identifier, String groupIdentifier, boolean root, List<FlatRideModel> flatRideModels, FlatRideComponentSpeed flatRideComponentSpeed) {
        super(identifier, groupIdentifier, root, flatRideModels);
        this.flatRideComponentSpeed = flatRideComponentSpeed;
        this.sineArm = 2f;
        this.actuatorState = new Vector3(0,0,0);
        this.sineState = 0f;
    }

    private void increaseSineState(){
        sineState = (sineState + flatRideComponentSpeed.getSpeed()/360f*PI) % PI2;
        updateActuatorState();
    }

    private void updateActuatorState(){
        actuatorState.y = Math.sin(sineState)*sineArm;
    }

    @Override
    public Vector3 getPosition() {
        return Vector3.add(super.getPosition(), actuatorState);
    }

    @Override
    public void tick() {
        increaseSineState();

        for(Attachment attachment : getChildren()){
            attachment.update();
            attachment.getChild().tick();
        }

        updateFlatRideModels();
    }
}
