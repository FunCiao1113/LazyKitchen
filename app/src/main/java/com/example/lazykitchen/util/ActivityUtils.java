package com.example.lazykitchen.util;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActivityUtils {
    private static Map<String, Activity> destoryMap = new HashMap<>();

    //将Activity添加到队列中
    public static void add(String activityName, Activity activity) {
        destoryMap.put(activityName, activity);
    }

    //根据名字销毁制定Activity
    public static void destory(String activityName) {
        System.out.println(activityName);
        Set<String> keySet = destoryMap.keySet();
        if (keySet.size() > 0) {
            for (String key : keySet) {
                if (activityName.equals(key)) {
                    destoryMap.get(key).finish();
                }
            }
        }
    }
}
