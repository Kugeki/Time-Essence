package com.example.timeessence;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class NotificationIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    private static final String NOTIFICATION_ACTION =
            "NOTIFICATION_ACTION";

    public static void startNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(NOTIFICATION_ACTION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
