package com.dev334.blood.util.fcm;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dev334.blood.R;
import com.dev334.blood.ui.bank.BloodBankActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessage extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("Refreshed token:",s);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage==null)
            return;
        if(remoteMessage.getNotification()!=null)
        {
            handleMessage(remoteMessage.getNotification().getBody());
        }
    }

    private void handleMessage(String body) {

        Log.e("MessageBody: ",body);

        Intent intent=new Intent(this, BloodBankActivity.class);
        TaskStackBuilder builder=TaskStackBuilder.create(this);
        builder.addNextIntentWithParentStack(intent);

        PendingIntent result=builder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builderCompat=new NotificationCompat.Builder(this,"hemo")
                .setSmallIcon(R.drawable.ic_app_logo);
        builderCompat.setContentIntent(result);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(100,builderCompat.build());

    }
}
