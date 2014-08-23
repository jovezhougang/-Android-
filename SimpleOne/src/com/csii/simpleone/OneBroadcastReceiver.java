package com.csii.simpleone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class OneBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(getClass().getSimpleName() + ":::Haha!");
        Bundle extras = new Bundle();
        extras.putString("data", getResultExtras(true).getString("data")
                + ",OneBroadcastReceiver");
        System.out.println(getClass().getSimpleName() + ":::Haha!"+getResultExtras(true));
        setResultExtras(extras);
    }

}
