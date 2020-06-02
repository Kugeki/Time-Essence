package com.example.timeessence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            /*
            Log.i("BOOTRECEIVER", "HELLO!");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, NotificationService.class));
            } else {
                //context.startService(new Intent(context, NotificationService.class));
            }
             */

            /*
            Intent demoIntentForBroadcast =
                    new Intent(context, NotificationRequestsReceiver.class);

            demoIntentForBroadcast
                    .setAction(NotificationRequestsReceiver.NOTIFICATION_PERFORM);

            context.sendBroadcast(demoIntentForBroadcast);
             */
        }

    }
}
