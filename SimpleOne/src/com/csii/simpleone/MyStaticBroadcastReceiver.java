package com.csii.simpleone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

public class MyStaticBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(),
                "com.csii.simpleone.MyStaticBroadcastReceiver")) {
            Toast.makeText(context, "MyStaticBroadcastReceiver我被调用了" + context,
                    Toast.LENGTH_LONG).show();
            System.out.println("MyStaticBroadcastReceiver我被调用了" + context);
        }
    }

}
