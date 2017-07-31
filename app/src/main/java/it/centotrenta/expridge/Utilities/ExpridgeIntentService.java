package it.centotrenta.expridge.Utilities;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import it.centotrenta.expridge.MainActivity;
import it.centotrenta.expridge.R;

public class ExpridgeIntentService extends IntentService {

    private static int NOTIFICATION_ID = 0;

    public ExpridgeIntentService() {
        super("ExpridgeIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO the getIntent part sends null
        Intent notifyIntent = new Intent(this, MainActivity.class);
        String itemName = intent.getStringExtra("itemName");
        String dateFormatted = intent.getStringExtra("itemDate");
        Notification.Builder notification = new Notification.Builder(this);
        notification.setContentTitle(itemName + " will expire soon!");
        notification.setContentText("You should eat it before " + dateFormatted);
        notification.setSmallIcon(R.drawable.notifications);
        notifyIntent.getIntExtra("id",0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);
        Notification notificationCompat = notification.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
        NOTIFICATION_ID++;
    }

    public static int getId(){
        return NOTIFICATION_ID;

    }

}
