package com.forms.floatingwindowsimple.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;

import java.lang.reflect.Method;

/**
 *
 * @author lyj
 * @date 2018/7/3
 */

public class FloatWindowPermissionUtil {

    public static boolean isFloatWindowOpAllowed(Context context) {
        AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            Class clazz = AppOpsManager.class;
            Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
            return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, 24, Binder.getCallingUid(), context.getPackageName());
        } catch (Exception e) {
        }
        return false;
    }

}
