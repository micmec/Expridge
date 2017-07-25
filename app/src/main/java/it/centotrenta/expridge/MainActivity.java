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


//TODO install notifications when an item is expiring in the next 2 days
//TODO consider creating a dialog when setting notifications to let the user choose
//TODO find better icons since these suck
//TODO add more settings or fill in the space of the setting part
//TODO BUG, moving icon for notifications
//TODO BUG, notifications cancel each other
//TODO dialog if he puts an item with double keyword
//TODO make the button for adding go back and the two buttons on items go back after 5 seconds

public class MainActivity extends AppCompatActivity implements ListItemsAdapter.ListItemsAdapterClickHandler {

    // Our variables
    public static DBHandler dataBaseHandler;
    private RecyclerView mRecyclerView;
    private static ListItemsAdapter mAdapter;
    private TextView mErrorMessageView;
    private ProgressBar mProgressBar;
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

        // Reference to the objects in the layouts/other
        mRecyclerView = (RecyclerView) findViewById(R.id.list_activity_recyclerView);
        mErrorMessageView = (TextView) findViewById(R.id.list_activity_errorMessage);
        mProgressBar = (ProgressBar) findViewById(R.id.list_activity_progressBar);
        mNoItems = (TextView) findViewById(R.id.no_items_view);
        dataBaseHandler = new DBHandler(this);
        arrowNoItems = (ImageView) findViewById(R.id.arrow_no_items);

        // Animations
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRotClock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate);
        FabRotAnti = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate_back);

        SharedPreferences pref = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        Toast.makeText(this,pref.getInt("alarmValue",0)+ " is the value",Toast.LENGTH_SHORT).show();

        // FAB
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

        // LayoutManager handling
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // Adapter
        mAdapter = new ListItemsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // Check for the items
        noItemsMethod();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // We reconstruct the adapter from the new data
        mAdapter.addItem(this);
        mRecyclerView.setAdapter(mAdapter);

        // Check for the items
        noItemsMethod();

    }

    // Create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater
        MenuInflater inflater = getMenuInflater();

        // Inflate it (layout and menu destination as params)
        inflater.inflate(R.menu.add_item,menu);

        return true;

    }

    // When the item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Get the item id
        int id = item.getItemId();

        if(id == R.id.add_item_menu){
            Intent intent = new Intent(this, AlarmActivity.class);
            startActivity(intent);
        }

        // Safe case
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
                                    //Yes button clicked
                                    deletePartOfTheMethod(id);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
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

        // Action of the floating red Button
        Intent intent = new Intent(this, AddItems.class);
        startActivity(intent);

    }

    private void showItemsDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageView.setVisibility(View.INVISIBLE);
        mNoItems.setVisibility(View.INVISIBLE);
        arrowNoItems.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageView.setVisibility(View.VISIBLE);
    }

    private void noItemsMethod(){

        if(dataBaseHandler.isDBNil()){

            mRecyclerView.setVisibility(View.INVISIBLE);
            mNoItems.setVisibility(View.VISIBLE);
            arrowNoItems.setVisibility(View.VISIBLE);

        }
        else{
            showItemsDataView();
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