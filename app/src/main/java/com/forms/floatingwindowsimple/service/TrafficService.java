package com.forms.floatingwindowsimple.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.forms.floatingwindowsimple.R;
import com.forms.floatingwindowsimple.receiver.HomeWatcherReceiver;
import com.forms.floatingwindowsimple.view.FloatView;




public class TrafficService extends Service {
    private FloatView floatView;
    private FloatViewStatusReceiver receiver;
    private HomeWatcherReceiver homeWatcherReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册广播
        receiver = new FloatViewStatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TrafficServiceUtils.ACTION);
        registerReceiver(receiver, filter);

        homeWatcherReceiver = new HomeWatcherReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeWatcherReceiver, homeFilter);

        if (floatView == null) {
            floatView = new FloatView(getApplicationContext());
            floatView.setLayout(R.layout.layout_float_window);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && floatView != null) {
            floatView.create();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (floatView != null) {
            floatView.close();
        }
        unregisterReceiver(receiver);
        unregisterReceiver(homeWatcherReceiver);
        super.onDestroy();
    }


    public class FloatViewStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int iod = intent.getIntExtra("status", -1);
            if (iod == TrafficServiceUtils.STATUS_CLOSE) {
                floatView.close();
            } else if (iod == TrafficServiceUtils.STATUS_SHOW) {
                floatView.show();
            }
        }
    }
}