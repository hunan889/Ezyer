package com.niuan.common.ezyer.util.system;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Carlos on 2015/9/25.
 */
public class SystemApp {
    public static void startExternalActivity(Activity activity, String uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startExternalActivity(activity, intent);
        } catch (Exception e) {
//            L.error(activity, "can not open uri(%s) : %s", uri, e);
        }
    }

    public static void startExternalActivity(Activity activity, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

}
