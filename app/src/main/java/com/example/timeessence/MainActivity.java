package com.example.timeessence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        if(NotificationRequestsReceiver.firstLaunch) {
            Intent demoIntentForBroadcast =
                    new Intent(context, NotificationRequestsReceiver.class);

            demoIntentForBroadcast
                    .setAction(NotificationRequestsReceiver.NOTIFICATION_PERFORM);

            context.sendBroadcast(demoIntentForBroadcast);
            NotificationRequestsReceiver.firstLaunch = false;
        }


    }
}
