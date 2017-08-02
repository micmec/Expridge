package it.centotrenta.expridge.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String name = intent.getStringExtra("itemName");
        String date = intent.getStringExtra("itemDate");
        Intent intent1 = new Intent(context,ExpridgeIntentService.class);
        intent1.putExtra("itemName",name);
        intent1.putExtra("itemDate",date);
        ser
    }

}
