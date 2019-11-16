package com.tonly.walnut.gesturedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
    public static final String TAG = "BootCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action = " + action);
        Log.d(TAG, "action = " + action);
        Log.d(TAG, "action = " + action);
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "BootCompleteReceiver  ACTION_BOOT_COMPLETED.");
            Intent intent1 = new Intent();
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.setClass(context, MainActivity.class);
            context.startActivity(intent1);
        }
    }
}
