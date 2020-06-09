package com.example.timeessence;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
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

    public static List<UsageStats> getUsageStatsList(Context context) {
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        long endTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime));
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        assert usm != null;
        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime);
        return usageStatsList;
    }

    public static void printUsageStats(List<UsageStats> usageStatsList) {
        for (UsageStats u : usageStatsList) {
            if (u.getTotalTimeInForeground() != 0)
                Log.d(TAG, "Pkg: " + u.getPackageName() + "\t" + "ForegroundTime: "
                        + TimeUnit.MILLISECONDS.toMinutes(u.getTotalTimeInForeground()));
        }
    }

    public static List<UsageStats> getTrackedAppsStats(Context context) {
        List<UsageStats> trackedStats = new ArrayList<UsageStats>();
        totalTrackedAppsTime = 0;
        for (UsageStats u : getUsageStatsList(context)) {
            String pkgName = u.getPackageName();

            if (Arrays.asList(trackedApps).contains(pkgName)) {
                trackedStats.add(u);
                totalTrackedAppsTime += u.getTotalTimeInForeground();
            }
        }

        return trackedStats;
    }

    private static UsageStatsManager getUsageStatsManager(Context context) {
        return (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }
}
