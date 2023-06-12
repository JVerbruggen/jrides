package com.jverbruggen.jrides.models.ride.state;

public enum OpenState {
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
        public OpenState open() {
            return this;
        }

        @Override
        public OpenState close() {
            return TRANSITION_TO_CLOSE;
        }

        @Override
        public boolean canUnload() {
            return false;
        }
    },
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
        public OpenState open() {
            return TRANSITION_TO_OPEN;
        }

        @Override
        public OpenState close() {
            return this;
        }

        @Override
        public boolean canUnload() {
            return true;
        }
    },
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
        public OpenState open() {
            return this;
        }

        @Override
        public OpenState close() {
            return this;
        }

        @Override
        public boolean canUnload() {
            return true;
        }
    },
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
        public OpenState open() {
            return this;
        }

        @Override
        public OpenState close() {
            return this;
        }

        @Override
        public boolean canUnload() {
            return true;
        }
    },
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
        public OpenState open() {
            return this;
        }

        @Override
        public OpenState close() {
            return this;
        }

        @Override
        public boolean canUnload() {
            return true;
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
        public OpenState open() {
            return this;
        }

        @Override
        public OpenState close() {
            return this;
        }

        @Override
        public boolean canUnload() {
            return false;
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
        public OpenState open() {
            return this;
        }

        @Override
        public OpenState close() {
            return this;
        }

        @Override
        public boolean canUnload() {
            return false;
        }
    };

    public abstract boolean isOpen();
    public abstract boolean isOpening();
    public abstract OpenState open();
    public abstract OpenState close();
    public abstract boolean canUnload();
}
