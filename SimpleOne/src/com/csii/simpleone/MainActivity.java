package com.csii.simpleone;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    final static Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);
    MyBroadcastReceiver mMyBroadcastReceiver = new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LOGGER.info((getWindow().getDecorView() instanceof FrameLayout) + "");
        LOGGER.info(((ViewGroup) getWindow().getDecorView()).getChildAt(0) + "");
        LOGGER.info(findViewById(R.id.content).getParent() + "");
        LOGGER.info(findViewById(R.id.content).getParent().getParent() + "");
        Toast.makeText(getApplicationContext(),
                getResources().getDisplayMetrics().densityDpi + "",
                Toast.LENGTH_LONG).show();
        AnimationDrawable ad = (AnimationDrawable) findViewById(R.id.content)
                .getBackground();
        ad.start();
        getWindow().setBackgroundDrawableResource(R.drawable.ic_launcher);
        findViewById(R.id.button).setOnClickListener(this);
        getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        filter = new IntentFilter(Intent.ACTION_TIME_TICK);
    }

    public void sendBroadcast(View v) {
        Intent intent = new Intent(
                "com.csii.simpleone.MyStaticBroadcastReceiver");
        sendBroadcast(intent);
        // ComponentName componentName = new
        // ComponentName(getApplicationContext()
        // .getPackageName(),
        // "com.csii.simpleone.MyStaticBroadcastReceiver");
        // getPackageManager().setComponentEnabledSetting(componentName,
        // PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
    }

    public void startService(View v) {
        // 根据Action 来‘隐式’启动Service
        Intent intent = new Intent("com.csii.simpleone.MyService");
        // 根据class 来‘显示’启动Service
        // Intent intent = new Intent(this,MyService.class);
        startService(intent);
    }

    public void stopService(View v) {
        Intent intent = new Intent("com.csii.simpleone.MyService");
        stopService(intent);
    }
    public void sendOrderedBroadcast(View v) {
        HandlerThread ht = new HandlerThread("MyHandlerThread");
        ht.start();
        Handler handler = new Handler(ht.getLooper());
        Intent intent = new Intent("com.csii.simpleone.order");
        sendOrderedBroadcast(intent, null, new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println(Thread.currentThread().getName());
                System.out.println(getResultExtras(true).getString("data"));
            }
        }, handler/* 最後的OnReceiver 是在哪個綫程裏面執行 如果爲空就在main綫程裏面執行 */,
                Activity.RESULT_OK, null, null);
    }

    /**
     * System 一直会保存该广播最后一次发送的状态 （直到系统重启为止） 如果在某处有人注册了该广播的Receiver那么该Receiver
     * 会收到该广播最后一次发送的状态
     * 
     * @param v
     */
    public void sendStickyBroadcast(View v) {
        Intent intent = new Intent(getPackageName() + ".StickyBroadcast");
        intent.putExtra("sendTime", SystemClock.uptimeMillis());
        sendStickyBroadcast(intent);
    }

    BroadcastReceiver mStickyBroadcastReceiver;
    public void registerStickyBroadcastReceiver(View v) {

        if (null != mStickyBroadcastReceiver) {
            unregisterReceiver(mStickyBroadcastReceiver);
            mStickyBroadcastReceiver = null;
        }
        mStickyBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("有人发送了一个粘性广播"
                        + intent.getLongExtra("sendTime", 0));
            }
        };
        IntentFilter mIntentFilter = new IntentFilter(getPackageName()
                + ".StickyBroadcast");
        registerReceiver(mStickyBroadcastReceiver, mIntentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    static class MyAsynTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return null;
        }

    }

    IntentFilter filter;
    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mMyBroadcastReceiver, filter);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(Intent.ACTION_TIME_TICK, intent.getAction())) {
                System.out.println(intent + ":Hello 时间改变了");
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMyBroadcastReceiver);
        if (null != mStickyBroadcastReceiver) {
            unregisterReceiver(mStickyBroadcastReceiver);
            mStickyBroadcastReceiver = null;
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello");
        List<ResolveInfo> resolveInfos = getPackageManager()
                .queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            System.out.println(resolveInfo);
        }
        startActivity(Intent.createChooser(intent, "我的共享"));
        // AlertDialog.Builder builder = new AlertDialog.Builder(this)
        // .setMessage("是否要退出应用").setTitle("提醒")
        // .setIcon(android.R.drawable.ic_dialog_alert)
        // .setPositiveButton("是", new DialogInterface.OnClickListener() {
        //
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        // // TODO Auto-generated method stub
        //
        // MainActivity.this.finish();
        // overridePendingTransition(android.R.anim.fade_in,
        // android.R.anim.fade_out);
        //
        // }
        // }).setNegativeButton("否", null);
        //
        // Dialog dialog = builder.create();
        // dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        // dialog.show();
    }

    boolean isBind = false;
    public void unBindService(View v) {
        if (isBind) {
            unbindService(conn);
            isBind = false;
        }
    }

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("onServiceDisconnected-->" + name);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("onServiceConnected-->" + name);
            isBind = true;
            demo demoInstance = demo.Stub.asInterface(service);
            try {
                System.out.println("demoInstance.add(10, 23)--->"
                        + (demoInstance.add(10, 23)));
                System.out.println("demoInstance.add(10, 23)--->"
                        + (demoInstance.add(10, 33)));
                System.out.println("demoInstance.add(10, 23)--->"
                        + (demoInstance.add(10, 43)));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    public void bindService(View v) {
        bindService(new Intent(this, MyService.class), conn,
                Service.BIND_AUTO_CREATE);
    }

    static final Uri URI_ONE = new Uri.Builder().scheme("content")
            .authority(Constant.authorities).appendPath("books")
            .appendPath("1").build();
    static final Uri URI_TWO = new Uri.Builder().scheme("content")
            .authority(Constant.authorities).appendPath("books")
            .appendPath("name").build();
    static final Uri URI_BATCH = new Uri.Builder().scheme("content")
            .authority(Constant.authorities).appendPath("books").build();
    int flag = 0;
    public void checkUriMatcher(View view) {
        System.out.println(URI_ONE);
        System.out.println(URI_TWO);
        System.out.println(getContentResolver().getType(
                (++flag % 2 == 0) ? URI_ONE : URI_TWO)
                + "");
    }

    public void insertBatchDatas(View v) {
        HashMap<String, List<HashMap<String, String>>> datas = new HashMap<String, List<HashMap<String, String>>>();
        List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
        datas.put("List", dataList);
        for (int i = 0; i < 100; ++i) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("name", "name" + i);
            item.put("author", "author" + i);
            item.put("publish_time", new Date().toString());
            item.put("modifiy", new Date().toString());
            dataList.add(item);
        }

        ContentValues cv = new ContentValues();
        try {
            cv.put("datas",
                    new Gson().toJson(datas).toString().getBytes("UTF-8"));
            Uri uri = getContentResolver().insert(URI_BATCH, cv);
            System.out.println(uri);
            Toast.makeText(getApplicationContext(), uri.toString(),
                    Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind) {
            unbindService(conn);
            isBind = false;
        }
    }
}
