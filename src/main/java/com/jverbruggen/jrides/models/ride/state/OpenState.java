package com.jverbruggen.jrides.models.ride.state;

import com.jverbruggen.jrides.animator.RideHandle;

public enum OpenState {
    /**
     * Open state
     */
    OPEN{
        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public boolean isOpening() {
            return false;
        }

        @Override
        public boolean isClosing() {
            return false;
        }

        @Override
        public OpenState open(RideHandle rideHandle) {
            return this;
        }

        @Override
        public OpenState close(RideHandle rideHandle) {
            rideHandle.broadcastRideClose();

            return TRANSITION_TO_CLOSE;
        }

        @Override
        public boolean canUnload() {
            return false;
        }

        @Override
        public OpenState getSaveState() {
            return this;
        }
    },
    /**
     * Closed state
     */
    CLOSED {
        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean isOpening() {
            return false;
        }

        @Override
        public boolean isClosing() {
            return false;
        }

        @Override
        public OpenState open(RideHandle rideHandle) {
            rideHandle.broadcastRideOpen();
            return OPEN;
        }

        @Override
        public OpenState close(RideHandle rideHandle) {
            return this;
        }

        @Override
        public boolean canUnload() {
            return true;
        }

        @Override
        public OpenState getSaveState() {
            return this;
        }
    },
    /**
     * Maintenance state, ride is loaded but not available to the public. It does appear in the ride overview menu
     */
    MAINTENANCE {
        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean isOpening() {
            return false;
        }

        @Override
        public boolean isClosing() {
            return false;
        }

        @Override
        public OpenState open(RideHandle rideHandle) {
            return this;
        }

        @Override
        public OpenState close(RideHandle rideHandle) {
            return this;
        }

        @Override
        public boolean canUnload() {
            return true;
        }

        @Override
        public OpenState getSaveState() {
            return this;
        }
    },
    /**
     * Hidden state, ride is loaded but not available to the public. It does not appear in the ride overview menu
     */
    HIDDEN {
        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean isOpening() {
            return false;
        }

        @Override
        public boolean isClosing() {
            return false;
        }

        @Override
        public OpenState open(RideHandle rideHandle) {
            return this;
        }

        @Override
        public OpenState close(RideHandle rideHandle) {
            return this;
        }

        @Override
        public boolean canUnload() {
            return true;
        }

        @Override
        public OpenState getSaveState() {
            return this;
        }
    },
    /**
     * Disabled state, the ride is not loaded at all
     */
    DISABLED {
        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean isOpening() {
            return false;
        }

        @Override
        public boolean isClosing() {
            return false;
        }

        @Override
        public OpenState open(RideHandle rideHandle) {
            return this;
        }

        @Override
        public OpenState close(RideHandle rideHandle) {
            return this;
        }

        @Override
        public boolean canUnload() {
            return true;
        }

        @Override
        public OpenState getSaveState() {
            return this;
        }
    },
    /**
     * Inactive state, ride is partially loaded. Track and trains are not loaded. Does appear in the ride overview menu
     */
    INACTIVE{
        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean isOpening() {
            return false;
        }

        @Override
        public boolean isClosing() {
            return false;
        }

        @Override
        public OpenState open(RideHandle rideHandle) {
            return this;
        }

        @Override
        public OpenState close(RideHandle rideHandle) {
            return this;
        }

        @Override
        public boolean canUnload() {
            return true;
        }

        @Override
        public OpenState getSaveState() {
            return this;
        }
    },
    TRANSITION_TO_CLOSE {
        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean isOpening() {
            return false;
        }

        @Override
        public boolean isClosing() {
            return true;
        }

        @Override
        public OpenState open(RideHandle rideHandle) {
            return this;
        }

        @Override
        public OpenState close(RideHandle rideHandle) {
            return this;
        }

        @Override
        public boolean canUnload() {
            return false;
        }

        @Override
        public OpenState getSaveState() {
            return CLOSED;
        }
    },
    TRANSITION_TO_OPEN {
        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean isOpening() {
            return true;
        }

        @Override
        public boolean isClosing() {
            return false;
        }

        @Override
        public OpenState open(RideHandle rideHandle) {
            return this;
        }

        @Override
        public OpenState close(RideHandle rideHandle) {
            return this;
        }

        @Override
        public boolean canUnload() {
            return false;
        }

        @Override
        public OpenState getSaveState() {
            return OPEN;
        }
    };

    public abstract boolean isOpen();
    public abstract boolean isOpening();
    public abstract boolean isClosing();
    public abstract OpenState open(RideHandle rideHandle);
    public abstract OpenState close(RideHandle rideHandle);
    public abstract boolean canUnload();
    public abstract OpenState getSaveState();
}
