package com.example.timeessence;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {
    private static final String TAG = NotificationJobService.class.getSimpleName();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy");


    @Override
    public boolean onStartJob(JobParameters params) {

        Context context = getApplicationContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        String nowDate = dateFormat.format(Calendar.getInstance().getTimeInMillis());
        String counterDate = pref.getString("COUNTER_DATE", nowDate);
        int hoursCounter = pref.getInt("HOURS_COUNTER", 0);

        Log.d(TAG, "nowDate: " + nowDate);
        Log.d(TAG, "counterDate: " + counterDate);
        Log.d(TAG, "hoursCounter: " + hoursCounter);

        if (!counterDate.equals(nowDate)) {
            counterDate = nowDate;
            hoursCounter = 0;
        }

        List<UsageStats> us = TimeManager.getUsageStatsList(context);
        TimeManager.printUsageStats(us);

        long trackedAppsTime = TimeManager.getTotalTrackedAppsTime();
        Log.d(TAG, "trackedAppsTime: " +
                (new SimpleDateFormat("M-d-yyyy HH:mm:ss")).format(trackedAppsTime));
        if (TimeUnit.MILLISECONDS.toHours(trackedAppsTime) != hoursCounter) {
            String notificationTitle = "Title";
            String notificationText = "Notification text";
            createNotification(notificationTitle, notificationText);

            hoursCounter++;
        }

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("COUNTER_DATE", counterDate);
        editor.putInt("HOURS_COUNTER", hoursCounter);
        editor.apply();

        jobFinished(params, true);
        return false;
    }

    private void createNotification(String notificationTitle, String notificationText) {
        Log.d(TAG, "Notifying");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "CHANNEL_ID";
        String channelName = "Main channel";
        String channelDescription = "Отправка уведомлений.";

        createNotificationChannel(notificationManager, channelId, channelName, channelDescription);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText);

        Notification notification = builder.build();

        assert notificationManager != null;
        notificationManager.notify(1, notification);
    }

    private void createNotificationChannel(NotificationManager notificationManager, String channelId, String channelName, String channelDescription) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription(channelDescription);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(false);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
