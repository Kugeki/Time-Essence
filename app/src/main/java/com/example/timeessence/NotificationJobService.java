package com.example.timeessence;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        /* TODO: Каждый час использования отслеживаемого приложения слать уведомление
            с мотивацией. Для избежания повторов можно использовать счётчик часов, который пополняется,
            когда отправляется уведомление. А также счётчик дней, чтобы обнулять счётчик часов.
            Если this_day != days_counter, то обнулить hours_counter.

            TODO: Для запросов ко времени приложений сделать TimeManager и создать нужные
             методы.
         */
        //NotificationIntentService.startNotification(getApplicationContext());
        Log.i("NOTIFICATIONSERVICE","Notifying");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "Main channel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Отправка уведомлений.");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(false);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "CHANNEL_ID")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Title")
                        .setContentText("Notification text");

        Notification notification = builder.build();

        assert notificationManager != null;
        notificationManager.notify(1, notification);

        jobFinished(params, true);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
