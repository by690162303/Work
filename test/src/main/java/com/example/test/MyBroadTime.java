package com.example.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by 白杨 on 2015/12/16.
 */
public class MyBroadTime extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("aa","时间进行了改变");
    }
}
