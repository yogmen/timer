package com.example.timerapp.core;


/**
 * Created by robert on 25/03/15.
 */
public class TimerApp{
    public static final int TIMER_RUN = 1;
    public static final int TIMER_STOP = 2;

    private long mStartTime;
    private long mStopTime;
    private long mOffset;
    private int mStatus;

    public TimerApp(){
        reset();
    }

    public void reset(){
        mStatus = TIMER_STOP;
        mStartTime = 0;
        mStopTime = 0;
        mOffset = 0;
    }

    public void start(){
        mStatus = TIMER_RUN;
        mStartTime = System.currentTimeMillis();
    }

    public void stop(){
        mStatus = TIMER_STOP;
        mStopTime = System.currentTimeMillis();
        mOffset += getTime();
    }

    public long getTime() {
        if (mStatus == TIMER_RUN) {
            return System.currentTimeMillis() - mStartTime + mOffset;
        } else{
            return mStopTime - mStartTime;
        }
    }

    public int getStatus(){
        return mStatus;
    }
}
