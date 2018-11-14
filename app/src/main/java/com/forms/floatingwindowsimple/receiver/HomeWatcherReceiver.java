package com.forms.floatingwindowsimple.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.forms.floatingwindowsimple.service.TrafficServiceUtils;


/**
 * @author lyj
 *
 */
public class HomeWatcherReceiver extends BroadcastReceiver {

    private static final String TAG = "HomeWatcherReceiver";
    private static final String SYSTEM_DIALOG_FROM_KEY = "reason";
    private static final String SYSTEM_DIALOG_FROM_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_FROM_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_FROM_LOCK = "lock";
    public static boolean IS_SYSTEM_DIALOG_FROM_LOCK = false;


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (IS_SYSTEM_DIALOG_FROM_LOCK) {
            IS_SYSTEM_DIALOG_FROM_LOCK = false;
            return;
        }
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String from = intent.getStringExtra(SYSTEM_DIALOG_FROM_KEY);
            if (SYSTEM_DIALOG_FROM_HOME_KEY.equals(from)) {
                //短按Home键
                TrafficServiceUtils.closeWindow(context);
            } else if (SYSTEM_DIALOG_FROM_RECENT_APPS.equals(from)) {
                //菜单键
                IS_SYSTEM_DIALOG_FROM_LOCK = true;
                TrafficServiceUtils.closeWindow(context);
            } else if (SYSTEM_DIALOG_FROM_LOCK.equals(from)) {
                //锁屏操作
                TrafficServiceUtils.closeWindow(context);
            }
        }
    }
}
