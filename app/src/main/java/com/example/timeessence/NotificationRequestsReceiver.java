package com.example.timeessence;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class NotificationRequestsReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_PERFORM = "NOTIFICATION_PERFORM";
    private static final String TAG = NotificationRequestsReceiver.class.getSimpleName();
    private static int sJobId = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NOTIFICATION_PERFORM.equals(Objects.requireNonNull(intent.getAction()))) {
            scheduleJob(context);
        } else {
            throw new IllegalArgumentException("Unknown action.");
        }
    }

    private void scheduleJob(Context context) {
        ComponentName jobService = new ComponentName(context, NotificationJobService.class);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(sJobId++, jobService)
                .setPeriodic(TimeUnit.MINUTES.toMillis(1));
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        Log.i(TAG, "scheduleJob: adding job to scheduler");

        assert jobScheduler != null;
        jobScheduler.schedule(jobBuilder.build());

    }
}
