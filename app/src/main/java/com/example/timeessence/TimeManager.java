package com.example.timeessence;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TimeManager {

    private static final String TAG = TimeManager.class.getSimpleName();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");

    private static final String[] trackedApps = {"com.vkontakte.android", "com.instagram.android",
            "com.twitter.android", "com.zhiliaoapp.musically, com.facebook.katana, ru.ok.android",
            "com.perm.kate_new_6", "com.android.chrome"
    };

    private static long totalTrackedAppsTime = 0;

    public static long getTotalTrackedAppsTime() {
        return totalTrackedAppsTime;
    }

    public static List<UsageStats> getUsageStatsList(Context context, long startTime, long endTime) {
        UsageStatsManager usm = getUsageStatsManager(context);

        Log.d(TAG, "Range start:" + dateFormat.format(startTime));
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        assert usm != null;
        return usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
    }

    public static void printUsageStats(List<UsageStats> usageStatsList) {
        for (UsageStats u : usageStatsList) {
            if (u.getTotalTimeInForeground() != 0)
                Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                        + TimeUnit.MILLISECONDS.toMinutes(u.getTotalTimeInForeground()));
        }
    }

    public static HashMap<String, AppUsageInfo> getStats(Context context) {
        UsageStatsManager usm = getUsageStatsManager(context);
        totalTrackedAppsTime = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        long endTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime));
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        UsageEvents uEvents = usm.queryEvents(startTime, endTime);
        HashMap<String, AppUsageInfo> map = new HashMap<>();

        boolean firstEvent = true;
        UsageEvents.Event previousE = new UsageEvents.Event();
        while (uEvents.hasNextEvent()) {
            UsageEvents.Event e = new UsageEvents.Event();
            uEvents.getNextEvent(e);
            String pkgName = e.getPackageName();

            if (!Arrays.asList(trackedApps).contains(pkgName)) {
                continue;
            }

            Log.d(TAG, "Event: " + e.getPackageName() + "\t" + dateFormat.format(e.getTimeStamp()));

            if (map.get(pkgName) == null)
                map.put(pkgName, new AppUsageInfo(pkgName));

            if (firstEvent) {
                previousE = e;
                firstEvent = false;
                continue;
            }

            boolean pForeground = previousE.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND;
            boolean background = e.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND;

            if (pForeground && background && e.getClassName().equals(previousE.getClassName())) {
                long diff = e.getTimeStamp() - previousE.getTimeStamp();
                Objects.requireNonNull(map.get(pkgName)).timeInForeground += diff;
                totalTrackedAppsTime += diff;
            }

            previousE = e;
        }

        return map;

    }

    private static UsageStatsManager getUsageStatsManager(Context context) {
        return (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }
}

class AppUsageInfo {
    String packageName;
    long timeInForeground;

    AppUsageInfo(String pkgName) {
        this.packageName = pkgName;
        this.timeInForeground = 0;
    }

    public String getPackageName() {
        return packageName;
    }

    public long getTimeInForeground() {
        return timeInForeground;
    }
}
