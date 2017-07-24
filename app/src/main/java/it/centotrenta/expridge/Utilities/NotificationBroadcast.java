package it.centotrenta.expridge.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by michelangelomecozzi on 22/07/2017.
 * <p>
 */

public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //TODO send the two parameters for the notification

        Intent intent1 = new Intent(context,ExpridgeIntentService.class);

        context.startService(intent1);

    }
}
