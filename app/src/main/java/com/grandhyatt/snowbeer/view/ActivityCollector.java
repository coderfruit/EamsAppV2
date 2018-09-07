package com.grandhyatt.snowbeer.view;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * ActivityCollector
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/10 09:09
 */
public class ActivityCollector {

    private static List<Activity> mActivityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : mActivityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
