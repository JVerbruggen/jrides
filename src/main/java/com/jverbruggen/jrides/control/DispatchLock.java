package com.jverbruggen.jrides.control;

import java.util.List;
import java.util.function.Consumer;

/**
 * A dispatch lock is used to see if a ride is ready for dispatch.
 * Several factors can be at play when dispatching a ride, each
 * of them able to prevent the vehicle from being dispatched.
 * I.e. restraints, gates, or if a minimum waiting time has passed
 * <br/>
 * If the DispatchLock is in the locked state, it prevents the ride from dispatching.
 * If the DispatchLock is in the unlocked state, it is OK with the ride dispatching.
 */
public interface DispatchLock {
    String getDescription();

    /**
     * Set lock to the locked state, preventing dispatch from happening.
     */
    void lock();

    /**
     * Set lock to unlocked state, allowing dispatch to happen.
     */
    void unlock();

    /**
     * Set status of the lock, which is visible to the player
     * and use as communication of what is preventing the ride from being dispatched.
     * @param status Explanation of why the lock is in the locked state.
     */
    void setStatus(String status);
    void setDebugStatus(String status);

    /**
     * Lock has no objection to dispatching.
     * @return True if it allows for dispatch, False if it should prevent the ride from dispatching.
     */
    boolean isUnlocked();

    /**
     * Set DispatchLock to explicit state
     * @param locked True if the ride should be prevented from being dispatched.
     */
    void setLocked(boolean locked);

    /**
     * Get a problem list of everything that is preventing the ride from being dispatched.
     * In a simple DispatchLock implementation, this list would be of length 1.
     * @param detailLevel Depth of how detailed the message should be (used in nested locks)
     * @param debug Debug mode = True should be used for administrative or debugging purposes.
     * @return A list of all problems
     */
    List<String> getProblems(int detailLevel, boolean debug);

    /**
     * Register a consumer to updates to the lock.
     * @param eventListener A consumer function that receives the lock with updated state, whenever a state change happens.
     */
    void addEventListener(Consumer<DispatchLock> eventListener);
}
