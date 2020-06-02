package com.example.timeessence;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.concurrent.TimeUnit;

public class NotificationRequestsReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_PERFORM = "NOTIFICATION_PERFORM";
    public static boolean firstLaunch = true;
    private static int sJobId = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        switch(intent.getAction()) {
            case NOTIFICATION_PERFORM:
                scheduleJob(context);
                break;
            default:
                throw new IllegalArgumentException("Unknown action.");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob(Context context)
    {
        ComponentName jobService = new ComponentName(context, NotificationJobService.class);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(sJobId++, jobService)
                .setPeriodic(TimeUnit.MINUTES.toMillis(1));
                //.setOverrideDeadline(TimeUnit.MINUTES.toMillis(2))
        // ;
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        Log.i("TAG", "scheduleJob: adding job to scheduler");

        assert jobScheduler != null;
        jobScheduler.schedule(jobBuilder.build());

    }
}
