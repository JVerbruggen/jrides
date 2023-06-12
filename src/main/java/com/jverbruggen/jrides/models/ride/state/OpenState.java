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
        public boolean isClosing() {
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

        @Override
        public OpenState getSaveState() {
            return this;
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
        public boolean isClosing() {
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

        @Override
        public OpenState getSaveState() {
            return this;
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
        public boolean isClosing() {
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

        @Override
        public OpenState getSaveState() {
            return this;
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
        public boolean isClosing() {
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

        @Override
        public OpenState getSaveState() {
            return this;
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
        public boolean isClosing() {
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

        @Override
        public OpenState getSaveState() {
            return OPEN;
        }
    };

    public abstract boolean isOpen();
    public abstract boolean isOpening();
    public abstract boolean isClosing();
    public abstract OpenState open();
    public abstract OpenState close();
    public abstract boolean canUnload();
    public abstract OpenState getSaveState();
}
