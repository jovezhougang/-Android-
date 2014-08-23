package com.csii.simpleone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class TwoBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(getClass().getSimpleName() + ":::Haha!");
        // 可以中段有序Broadcast傳播
        // abortBroadcast();
        Bundle extras = new Bundle();
        extras.putString("data", getResultExtras(true).getString("data")
                + ",TwoBroadcastReceiver");
        System.out.println(getClass().getSimpleName() + ":::Haha!"+getResultExtras(true));
        setResultExtras(extras);
    }

}
