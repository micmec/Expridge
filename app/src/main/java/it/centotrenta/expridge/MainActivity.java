package it.centotrenta.expridge;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity implements ListItemsAdapter.ListItemsAdapterClickHandler {


    private static final String TAG = "MainActivity";
    private static final int RC_OCR_CAPTURE = 9003;

    public static DBHandler dataBaseHandler;
    private RecyclerView mRecyclerView;
    private static ListItemsAdapter mAdapter;
    private TextView mErrorMessageView;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab,nFab,fFab;
    private TextView mNoItems;
    private Animation FabOpen,FabClose,FabRotClock,FabRotAnti;
    boolean isOpen = false;

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

        // Animations
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRotClock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate);
        FabRotAnti = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate_back);

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
        mAdapter.addItem();
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
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        // Get the item id
//        int id = item.getItemId();
//
//        if(id == R.id.add_item_menu){
//            //TODO
//        }
//
//        // Safe case
//        return super.onOptionsItemSelected(item);
//
//    }

    // Click handling
    @Override
    public void onClick(View view, String itemInformationName, final int id) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
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
        builder.setMessage("Are you sure you want to delete " + itemInformationName + "?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

        //TODO the dialog does not execute the method below yet

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

        }
        else{
            showItemsDataView();
        }

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
}

