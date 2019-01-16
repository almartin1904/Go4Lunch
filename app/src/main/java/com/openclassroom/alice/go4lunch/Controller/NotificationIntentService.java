package com.openclassroom.alice.go4lunch.Controller;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.openclassroom.alice.go4lunch.Controller.Activities.MainActivity;
import com.openclassroom.alice.go4lunch.Model.NotificationRestaurant;
import com.openclassroom.alice.go4lunch.R;

/**
 * Created by Alice on 15 January 2019.
 */
public class NotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID=1;

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationRestaurant notif= (NotificationRestaurant) intent.getSerializableExtra("notif");

        Notification.Builder builder = new Notification.Builder(this)
            .setContentTitle(getResources().getString(R.string.notification_title))
            .setSmallIcon(R.drawable.check_circle_black_24x24);

        Notification notification = new Notification.BigTextStyle(builder)
                .bigText("You are lunching at "+notif.getRestaurantName()+ ", "+notif.getRestaurantAddress()+" with "+notif.getStringWorkmatesJoining()).build();


        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }



}
