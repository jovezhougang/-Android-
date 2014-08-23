package com.example.simpletwo;

import com.csii.simpleone.demo;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            demo instance = demo.Stub.asInterface(service);
            try {
                System.out.println((instance.add(100, 88)));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    public void bindReService(View v) {
        bindService(new Intent("com.csii.simpleone.MyService"), conn,
                Service.BIND_AUTO_CREATE);
    }
}
