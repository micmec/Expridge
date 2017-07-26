package it.centotrenta.expridge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import it.centotrenta.expridge.Utilities.DBHandler;
import it.centotrenta.expridge.Utilities.NotificationBroadcast;

import com.google.android.gms.common.api.CommonStatusCodes;

import it.centotrenta.expridge.OcrUtils.OcrCaptureActivity;


//TODO install notifications when an item is expiring in the next 2 days
//TODO Automatic keyboard on add item
//TODO Name is too big font and bold, inconsistent with typing
//TODO Name of app to be changes
//TODO Add item screen shows settings label
//TODO Manual add icon is wrong, no chiave inglese
//TODO Filter no empty text on manual add item
//TODO General UI Improvements
//TODO Color scheme for float buttons
//TODO Good design = simplistic fridge with shelves?
//TODO Apache License for Vision Library
//TODO consider creating a dialog when setting notifications to let the user choose
//TODO find better icons since these suck
//TODO add more settings or fill in the space of the setting part
//TODO BUG, moving icon for notifications
//TODO BUG, notifications cancel each other
//TODO dialog if he puts an item with double keyword
//TODO make the button for adding go back and the two buttons on items go back after 5 seconds

public class MainActivity extends AppCompatActivity implements ListItemsAdapter.ListItemsAdapterClickHandler {


    private static final String TAG = "MainActivity";
    private static final int RC_OCR_CAPTURE = 9003;

    public static DBHandler dataBaseHandler;
    private RecyclerView mRecyclerView;
    private static ListItemsAdapter mAdapter;
    private TextView mErrorMessageView;
    private FloatingActionButton mFab,nFab,fFab;
    private TextView mNoItems;
    private Animation FabOpen,FabClose,FabRotClock,FabRotAnti;
    boolean isOpen = false;
    static boolean isOpenTwo;
    static boolean isNotificationOn;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private ImageView arrowNoItems;
    static boolean isFirstOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_activity_recyclerView);
        mErrorMessageView = (TextView) findViewById(R.id.list_activity_errorMessage);
        mNoItems = (TextView) findViewById(R.id.no_items_view);
        dataBaseHandler = new DBHandler(this);
        arrowNoItems = (ImageView) findViewById(R.id.arrow_no_items);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRotClock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate);
        FabRotAnti = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate_back);

        mFab = (FloatingActionButton) findViewById(R.id.red_button);
        nFab = (FloatingActionButton) findViewById(R.id.manually_button);
        fFab = (FloatingActionButton) findViewById(R.id.photo_button);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    nFab.startAnimation(FabClose);
                    fFab.startAnimation(FabClose);
                    mFab.startAnimation(FabRotAnti);
                    nFab.setClickable(false);
                    fFab.setClickable(false);
                    isOpen = false;
                }
                else{
                    nFab.startAnimation(FabOpen);
                    fFab.startAnimation(FabOpen);
                    mFab.startAnimation(FabRotClock);
                    nFab.setClickable(true);
                    fFab.setClickable(true);
                    isOpen = true;
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ListItemsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        noItemsMethod();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.addItem(this);
        mRecyclerView.setAdapter(mAdapter);
        noItemsMethod();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_item_menu){
            Intent intent = new Intent(this, AlarmActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(final View view, final int id, final String itemInfoName, final long time,
                        ImageView dButton, final ImageView alarmButton, final String dateFormatted) {

            if (isOpenTwo) {

                dButton.startAnimation(FabClose);
                alarmButton.startAnimation(FabClose);
                isOpenTwo = false;
                if(!isNotificationOn){
                    alarmButton.setImageResource(R.drawable.notifications);
                }
                else {
                    alarmButton.setImageResource(R.drawable.notifications_off);
                }
                dButton.setImageResource(R.drawable.cancel);
                dButton.setClickable(false);
                alarmButton.setClickable(false);

            }
            else {

                dButton.startAnimation(FabOpen);
                alarmButton.startAnimation(FabOpen);
                dButton.setClickable(true);
                alarmButton.setClickable(true);
                isOpenTwo = true;
                if(!isNotificationOn){
                    alarmButton.setImageResource(R.drawable.notifications);
                }
                else {
                    alarmButton.setImageResource(R.drawable.notifications_off);
                }
                dButton.setImageResource(R.drawable.cancel);

            }

        if(isFirstOpen) {

            dButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    deletePartOfTheMethod(id);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Are you sure you want to delete " + itemInfoName + "?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();


                }
            });

            alarmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!isNotificationOn) {

                        alarmButton.setImageResource(R.drawable.notifications_off);
                        setNotification(MainActivity.this, time, id, itemInfoName, dateFormatted);
                        isNotificationOn = true;

                    } else {

                        alarmButton.setImageResource(R.drawable.notifications);
                        deleteNotification(MainActivity.this, id, itemInfoName);
                        isNotificationOn = false;

                    }


                }
            });
            isFirstOpen = false;
        }
    }

    public void deletePartOfTheMethod(int id){
        dataBaseHandler.deleteItem(id);
        mAdapter.loadDB();
        mRecyclerView.setAdapter(mAdapter);
        noItemsMethod();

    }

    public void redButtonAction(View view) {
        Intent intent = new Intent(this, AddItems.class);
        startActivity(intent);

    }

    private void showItemsDataView() {
        mErrorMessageView.setVisibility(View.INVISIBLE);
        mNoItems.setVisibility(View.INVISIBLE);
        arrowNoItems.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void noItemsMethod(){
        if(dataBaseHandler.isDBNil()){
            mRecyclerView.setVisibility(View.INVISIBLE);
            mNoItems.setVisibility(View.VISIBLE);
            arrowNoItems.setVisibility(View.VISIBLE);}
        else{ showItemsDataView(); }

    }

    public void photoFloatClick(View view) {
        Intent intent = new Intent(this, OcrCaptureActivity.class);
        startActivityForResult(intent, RC_OCR_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    Log.d("TEXT THAT I GOT WAS", text);
                } else {
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                Log.d("RESULT NOT FROM CAMERA", "CODE IS " + requestCode);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setNotification(Context context, long time,int id, String itemName,String dateFormatted){

        SharedPreferences pref = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        int howBefore = pref.getInt("alarmValue",8400000);

        Intent notifyIntent = new Intent(context,NotificationBroadcast.class);
        notifyIntent.putExtra("id",id);
        notifyIntent.putExtra("itemName",itemName);
        notifyIntent.putExtra("itemDate",dateFormatted);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context,id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,time-howBefore,pendingIntent);

        Toast.makeText(this,"Notification on for " + itemName, Toast.LENGTH_SHORT).show();

        //TODO bug, create an if statement for avoiding notifications in cases of selecting the same day or the next one

    }

    public void deleteNotification(Context context,int id,String itemName){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notifyIntent = new Intent(context,NotificationBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context,id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this,"Notification off for " + itemName, Toast.LENGTH_SHORT).show();
    }

}