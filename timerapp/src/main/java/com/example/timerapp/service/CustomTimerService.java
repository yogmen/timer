package com.example.timerapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.example.timerapp.R;
import com.example.timerapp.activity.MainActivity;
import com.example.timerapp.core.TimerApp;

import java.text.SimpleDateFormat;

/**
 * Created by robert on 25/03/15.
 */
public class CustomTimerService extends Service {
    private final String TAG = getClass().getSimpleName();
    private static final int NOTIFICATION_ID = 123;
    private static final int FREQUENCY = 1000;
    private static final int WHAT = 9;
    private static final String TIME_FORMAT_ACTIVITY = "HH:mm:ss.SSS";
    private static final String TIME_FORMAT_NOTIFICATION = "HH:mm:ss";

    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private SimpleDateFormat mTimeFormat = new SimpleDateFormat(TIME_FORMAT_ACTIVITY);

    private TimerApp mTimerApp;

    private CustomBinder mCustomBinder = new CustomBinder();

    private Handler customHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "msg.what: " + msg.what);
            updateNotification();
            sendMessageDelayed(Message.obtain(this, WHAT), FREQUENCY);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mCustomBinder;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate()");
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mTimerApp = new TimerApp();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");
        return START_STICKY;
    }

    public String getFormatedTime(TimerComponent component) {
        switch (component){
            case NOTIFICATION:
                mTimeFormat.applyPattern(TIME_FORMAT_NOTIFICATION);
                return mTimeFormat.format(mTimerApp.getTime());
            case ACTIVITY:
                mTimeFormat.applyPattern(TIME_FORMAT_ACTIVITY);
                return mTimeFormat.format(mTimerApp.getTime());
            default:
                return "";
        }
    }

    private String formatDigits(long num) {
        return (num < 10) ? "0" + num : new Long(num).toString();
    }

    public void start(){
        mTimerApp.start();
        updateNotification();
        customHandler.sendMessageDelayed(Message.obtain(customHandler, WHAT), FREQUENCY);
    }

    public void stop(){
        mTimerApp.stop();
        removeNotification();
    }

    public void reset(){
        mTimerApp.reset();
    }

    public boolean isTimerRunning() {
        return TimerApp.TIMER_RUN == mTimerApp.getStatus();
    }

    public class CustomBinder extends Binder {
        public CustomTimerService getCustomTimerService() {
            return CustomTimerService.this;
        }
    }

    public void updateNotification() {
        Context context = getApplicationContext();
        CharSequence title = getString(R.string.app_name);
        CharSequence contentText = getFormatedTime(TimerComponent.NOTIFICATION);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        mNotification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .getNotification();

        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotification.flags |= Notification.FLAG_NO_CLEAR;

        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    public void removeNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID);
        customHandler.removeMessages(WHAT);
    }

    public enum TimerComponent{
        NOTIFICATION, ACTIVITY;
    }
}

