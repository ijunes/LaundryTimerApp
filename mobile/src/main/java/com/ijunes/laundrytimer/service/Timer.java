package com.ijunes.laundrytimer.service;

import timber.log.Timber;

/**
 * Timer object for TimerService
 */
public class Timer {

    /**
     * Default method for retreiving currentTimeMillis
     */
    private GetTime systemTime = new GetTime() {
        @Override
        public long current() {
            return System.currentTimeMillis();
        }
    };
    private GetTime mTime;
    private long mStartTime;

    private long mStopTime;
    private long mPauseOffset;
    private long mDurationWash;
    private long mDurationDry;
    private State mState;
    /**
     * States for timer.
     * IDLE means cycle has not started
     * PAUSED means phase 1 complete
     * RUNNING means cycle running
     * STOPPED means cycle complete
     */
    public enum State {
        IDLE, PAUSED, RUNNING, STOPPED
    }

    public Timer() {
        mTime = systemTime;
        reset();
    }
    public Timer(GetTime time) {
        mTime = time;
        reset();
    }
    /**
     * Start the stopwatch running. If the stopwatch is already running, this
     * does nothing.
     */
    public void startCycle() {
        if (mState == State.IDLE) {
            mStartTime = mTime.current();
            mStopTime = mStartTime + mDurationWash;
            Timber.d("Starting Wash Phase at" + String.valueOf(mStartTime));
            Timber.d("Expecting to finish Wash Phase at" + String.valueOf(mStopTime));
            mState = State.RUNNING;
        } else if (mState == State.PAUSED) {
            mPauseOffset = getElapsedTime();
            mStartTime = mTime.current();
            mStopTime = mStartTime + mDurationDry;
            Timber.d("Starting Dry Phase at" + String.valueOf(mStartTime));
            Timber.d("Expecting to finish Dry Phase at" + String.valueOf(mStopTime));
            mState = State.RUNNING;
        }
    }

    /**
     * Executed after wash phase complete.
     */
    public void pause() {
        if (mState == State.RUNNING) {
            mStopTime = mTime.current();
            Timber.d("Finishing Wash Phase at" + String.valueOf(mStopTime));
            mState = State.PAUSED;
        }
    }

    /**
     * Executed after dry phase complete.
     */
    public void finish() {
        if (mState == State.RUNNING) {
            mStopTime = mTime.current();
            Timber.d("Finishing Dry Phase at" + String.valueOf(mStopTime));
            mState = State.STOPPED;
        }
    }

    /**
     * Reset Cycle
     */
    public void reset() {
        mState = State.IDLE;
        mStartTime = 0;
        mStopTime = 0;
        mPauseOffset = 0;
    }

    /**
     * @return Time since cycle first started
     */
    public long getElapsedTime() {
        if (mState == State.PAUSED) {
            return (mStopTime - mStartTime) + mPauseOffset;
        } else {
            return (mTime.current() - mStartTime) + mPauseOffset;
        }
    }

    /**
     * Default interface to implement a method to get current time
     */
    public interface GetTime {
        long current();
    }
}
