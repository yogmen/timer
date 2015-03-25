package com.example.timerapp.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.timerapp.R;
import com.example.timerapp.service.CustomTimerService;

/**
 * Created by robert on 25/03/15.
 */
public class MainActivity extends Activity{
    private final String TAG = getClass().getSimpleName();
    private static final String START_BUTTON_STATE_KEY = "start_button_state_key";
    private Button mButtonStart;
    private Button mButtonStop;
    private TextView mTimeCounter;

    private final int WHAT = 9;
    private final int FREQUENCY = 100; //ms

    private CustomTimerService mCustomTimerService;
    private ServiceConnection mCustomTimerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCustomTimerService = ((CustomTimerService.CustomBinder)service).getCustomTimerService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCustomTimerService = null;
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateTime();
            sendMessageDelayed(Message.obtain(this, WHAT), FREQUENCY);
        }
    };

    private View.OnClickListener mStartButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(!mCustomTimerService.isTimerRunning()) {
                mCustomTimerService.start();
                mButtonStart.setEnabled(false);
            }

        }
    };

    private View.OnClickListener mStopButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mCustomTimerService.isTimerRunning()) {
                mButtonStart.setEnabled(true);
                mCustomTimerService.stop();
            } else{
                mCustomTimerService.reset();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initView();
        if (savedInstanceState != null) {
            mButtonStart.setEnabled(savedInstanceState.getBoolean(START_BUTTON_STATE_KEY, true));
        }
        startService(new Intent(this, CustomTimerService.class));
    }

    private void initView() {
        mButtonStart = (Button) findViewById(R.id.button_start);
        mButtonStop = (Button) findViewById(R.id.button_stop);
        mTimeCounter = (TextView) findViewById(R.id.time_counter_tv);

        mButtonStart.setOnClickListener(mStartButtonListener);
        mButtonStop.setOnClickListener(mStopButtonListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, CustomTimerService.class), mCustomTimerServiceConnection, Context.BIND_AUTO_CREATE);
        mHandler.sendMessageDelayed(Message.obtain(mHandler, WHAT), FREQUENCY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCustomTimerServiceConnection != null) {
            unbindService(mCustomTimerServiceConnection);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(START_BUTTON_STATE_KEY, mButtonStart.isEnabled());
    }

    private void updateTime() {
        if (mCustomTimerService != null) {
            mTimeCounter.setText(mCustomTimerService.getFormatedTime(CustomTimerService.TimerComponent.ACTIVITY));
        }
    }
}
