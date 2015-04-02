package com.example.timerapp;

import android.widget.TextView;

import com.example.timerapp.activity.MainActivity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(emulateSdk = 19)
@RunWith(RobolectricTestRunner.class)
public class ApplicationTest {

    @Before
    public void setup(){

    }

    @Test
    public void clickTest_shouldSetHelloText() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        activity.findViewById(R.id.button_test).performClick();

        TextView tv =  (TextView)activity.findViewById(R.id.test_result);
        Assert.assertTrue(tv!=null);

        String expected = activity.getString(R.string.hello);
        String real = tv.getText().toString();

        Assert.assertEquals(expected, real);
    }

}