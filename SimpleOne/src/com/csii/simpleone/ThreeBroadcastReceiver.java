package com.csii.simpleone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ThreeBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(getClass().getSimpleName() + ":::Haha!");
        
        Bundle extras = new Bundle();
        extras.putString("data", "ThreeBroadcastReceiver");
        setResultExtras(extras);
    }

}
