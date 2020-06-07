package com.example.timeessence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent demoIntentForBroadcast =
                    new Intent(context, NotificationRequestsReceiver.class);
            demoIntentForBroadcast
                    .setAction(NotificationRequestsReceiver.NOTIFICATION_PERFORM);
            context.sendBroadcast(demoIntentForBroadcast);
        }

    }
}
