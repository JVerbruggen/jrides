package com.jverbruggen.jrides.animator.flatride;

import com.jverbruggen.jrides.animator.flatride.interfaces.Component6DOFPosition;
import com.jverbruggen.jrides.animator.flatride.rotor.FlatRideModel;
import com.jverbruggen.jrides.config.flatride.structure.attachment.joint.RelativeAttachmentJointConfig;
import com.jverbruggen.jrides.models.math.Quaternion;
import com.jverbruggen.jrides.models.math.Vector3;
import com.jverbruggen.jrides.models.math.VectorQuaternionState;
import com.jverbruggen.jrides.serviceprovider.ServiceProvider;
import com.jverbruggen.jrides.state.ride.flatride.Animation;
import com.jverbruggen.jrides.state.ride.flatride.AnimationHandle;
import com.jverbruggen.jrides.state.ride.flatride.AnimationLoader;

import java.util.List;

public class Limb extends AbstractInterconnectedFlatRideComponent implements Component6DOFPosition {
    private Vector3 position;
    private Quaternion rotation;

    public Limb(String identifier, String groupIdentifier, boolean root, RelativeAttachmentJointConfig joint, List<FlatRideModel> flatRideModels, VectorQuaternionState initialPose) {
        super(identifier, groupIdentifier, root, joint, flatRideModels);
        position = initialPose.getVector();
        rotation = initialPose.getQuaternion();
    }

    @Override
    public Vector3 getPosition() {
        return Vector3.add(super.getPosition(), position);
    }

    @Override
    public Quaternion getRotation() {
        return Quaternion.multiply(super.getRotation(), rotation);
    }

    @Override
    public void setPositionRotation(Vector3 position, Quaternion rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public static VectorQuaternionState getInitialPoseFromAnimation(String preloadAnim, String limbIdentifier, FlatRideHandle rideHandle){
        VectorQuaternionState vectorQuaternionState;
        if(preloadAnim != null){
            AnimationLoader animationLoader = ServiceProvider.getSingleton(AnimationLoader.class);
            AnimationHandle animationHandle = animationLoader.loadFlatRideAnimation(preloadAnim, rideHandle);
            Animation animation = animationHandle.getAnimation(limbIdentifier);
            vectorQuaternionState = animation.getInitialPose();
        }else{
            vectorQuaternionState = VectorQuaternionState.zero();
        }

        return vectorQuaternionState;
    }
}
