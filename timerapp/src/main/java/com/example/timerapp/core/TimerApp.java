package com.example.timerapp.core;


import android.util.Log;

/**
 * Created by robert on 25/03/15.
 */
public class TimerApp{
    private static final String TAG = TimerApp.class.getCanonicalName();

    public static final int TIMER_RUN = 1;
    public static final int TIMER_STOP = 2;
    public static final int TIMER_PAUSE = 3;

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
        if(mStatus == TIMER_PAUSE){
            mStatus = TIMER_RUN;
            mOffset += getTime();
        }else {
            mStatus = TIMER_RUN;
            mStartTime = System.currentTimeMillis();
        }
    }

    public void stop(){
        mStatus = TIMER_PAUSE;
        mStopTime = System.currentTimeMillis();
        mOffset = getTime();
    }

    public long getTime() {
        if (mStatus == TIMER_RUN) {
//            Log.i(TAG, "gettingTime when running");
            return System.currentTimeMillis() - mStartTime + mOffset;
//        } else if(mStatus == TIMER_PAUSE){
//            return mStopTime -mStartTime + mOffset;
        } else {
//            Log.i(TAG, "gettingTime in different cases");
            return mStopTime - mStartTime;
        }
    }

    public int getStatus(){
        return mStatus;
    }
}
