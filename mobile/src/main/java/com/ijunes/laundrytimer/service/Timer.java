package com.ijunes.laundrytimer.service;

import android.content.res.Resources;
import android.os.CountDownTimer;

import com.ijunes.laundrytimer.MainActivity;
import com.ijunes.laundrytimer.MainApplication;
import com.ijunes.laundrytimer.R;

import timber.log.Timber;

/**
 * Timer object for TimerService
 */
public class Timer extends CountDownTimer {

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

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {

    }

    /**
     * States for timer.
     * IDLE means cycle has not started
     * PAUSED means phase 1 complete
     * RUNNING means cycle running
     * STOPPED means cycle complete
     */
    public enum State {
        IDLE, PAUSED, RUNNING, STOPPED, WASH, DRY
    }

    public enum Cycle{}

    public Timer(State state) {
        super(state == State.WASH ? 30000 : 50000, 1000);
        mState = state;
        //reset();
        //startCycle();
        final Resources resources =  MainApplication.get().getResources();
        mDurationWash = resources.getInteger(R.integer.temp_wash_duration_minutes);
        mDurationDry = resources.getInteger(R.integer.temp_dry_duration_minutes);

    }
    /**
     * Start the stopwatch running. If the stopwatch is already running, this
     * does nothing.
     */
    @Deprecated
    public void startCycle() {
        if (mState == State.WASH) {
            mStartTime = mTime.current();
            mStopTime = mStartTime + mDurationWash;
            Timber.d("Starting Wash Phase at" + String.valueOf(mStartTime));
            Timber.d("Expecting to finish Wash Phase at" + String.valueOf(mStopTime));
            mState = State.RUNNING;
        } else if (mState == State.DRY) {
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
