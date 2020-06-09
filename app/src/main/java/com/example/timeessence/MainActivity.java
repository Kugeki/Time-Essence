package com.example.timeessence;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        if (TimeManager.getUsageStatsList(this).isEmpty()) {
            Toast.makeText(context, getString(R.string.require_text), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

        updateUsageStatsList(context);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        checkOnFirstLaunch(context, pref);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Context context = getApplicationContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        checkOnFirstLaunch(context, pref);

        updateUsageStatsList(context);
    }

    private void updateUsageStatsList(Context context) {
        List<UsageStats> us = TimeManager.getTrackedAppsStats(context);
        TimeManager.printUsageStats(us);

        PackageManager pm = context.getPackageManager();

        ArrayList<HashMap<String, String>> appsInfo = new ArrayList<>();
        HashMap<String, String> map;

        for (UsageStats u : us) {
            map = new HashMap<>();
            ApplicationInfo ai = null;

            try {
                ai = pm.getApplicationInfo(u.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String appName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
            map.put("name", appName);
            map.put("time", NumeralsEndingConverter.convertMinutes(TimeUnit.MILLISECONDS.toMinutes(u.getTotalTimeInForeground())));
            appsInfo.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, appsInfo, android.R.layout.simple_list_item_2,
                new String[]{"name", "time"},
                new int[]{android.R.id.text1, android.R.id.text2});


        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    private void checkOnFirstLaunch(Context context, SharedPreferences pref) {
        boolean firstLaunch = pref.getBoolean("FIRST_LAUNCH", true);
        Log.d(TAG, Boolean.toString(firstLaunch));
        if (firstLaunch) {
            Intent intentForBroadcast =
                    new Intent(context, NotificationRequestsReceiver.class);
            intentForBroadcast
                    .setAction(NotificationRequestsReceiver.NOTIFICATION_PERFORM);
            context.sendBroadcast(intentForBroadcast);
            pref.edit().putBoolean("FIRST_LAUNCH", false).apply();
        }
    }
}
