package com.forms.floatingwindowsimple.service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.forms.floatingwindowsimple.util.DialogUtil;
import com.forms.floatingwindowsimple.util.FloatWindowPermissionUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author lyj
 * @date 2018/6/27
 */

public class TrafficServiceUtils {
    public static final String ACTION = "com.forms.floatingwindowsimple.service.TrafficService";
    public static final String STATUS = "status";
    public static final int STATUS_CLOSE = 1;
    public static final int STATUS_SHOW = 2;

    public static void showWindow(Context context) {
        if (isServiceRunning(context, ACTION)) {
            if (FloatWindowPermissionUtil.isFloatWindowOpAllowed(context)) {
                Intent intent = new Intent(ACTION);
                intent.putExtra(STATUS, STATUS_SHOW);
                context.sendBroadcast(intent);
            } else {
                requestPermission(context);
            }
        }
    }

    public static void closeWindow(Context context) {
        if (isServiceRunning(context, ACTION)) {
            Intent intent = new Intent(ACTION);
            intent.putExtra(STATUS, STATUS_CLOSE);
            context.sendBroadcast(intent);
        }
    }

    public static void startService(Context context) {
        if (!isServiceRunning(context, ACTION)) {
            if (FloatWindowPermissionUtil.isFloatWindowOpAllowed(context)) {
                Intent intent = new Intent(context, TrafficService.class);
                context.startService(intent);
            } else {
                requestPermission(context);
            }
        }
    }

    public static void stopService(Context context) {
        if (isServiceRunning(context, ACTION)) {
            Intent intent = new Intent(context, TrafficService.class);
            context.stopService(intent);
        }
    }

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName name = serviceInfo.service;
            if (name.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    private static void requestPermission(final Context context) {
        DialogUtil.showWithTwoBtn(context, "您的手机没有授予悬浮窗权限，请开启后再试", "现在去开启", "暂不开启", new DialogUtil.OnClickListener() {
            @Override
            public void onClick() {
                try {
                    Class clazz = Settings.class;
                    Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
                    Intent intent = new Intent(field.get(null).toString());
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                } catch (Exception e) {
                }
            }
        }, null).show();
    }
}
