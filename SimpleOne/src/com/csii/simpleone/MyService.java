package com.csii.simpleone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("MyService--->onBind");
        return new Demo();
    }

    class Demo extends demo.Stub {
        @Override
        public int add(int i, int j) throws RemoteException {
            return i + j;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("MyService--->onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("MyService--->onStartCommand" + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("MyService--->onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("MyService--->onUnbind");
        return super.onUnbind(intent);

    }

    @Override
    public void onRebind(Intent intent) {
        System.out.println("MyService--->onUnbind");
        super.onRebind(intent);
    }

}
