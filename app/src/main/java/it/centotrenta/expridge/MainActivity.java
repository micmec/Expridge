package it.centotrenta.expridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ListItemsAdapter.ListItemsAdapterClickHandler {

    // Our variables
    private RecyclerView mRecyclerView;
    private static ListItemsAdapter mAdapter;
    private TextView mErrorMessageView;
    private ProgressBar mProgressBar;
    private FloatingActionButton mButton;
    public static Database db = new Database();

    /*
    TODO create the AsyncTask process for getting information on the items (idea) and so use the showErrorMessage/showListItems
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference to the objects in the layouts/other
        mRecyclerView = (RecyclerView) findViewById(R.id.list_activity_recyclerView);
        mErrorMessageView = (TextView) findViewById(R.id.list_activity_errorMessage);
        mProgressBar = (ProgressBar) findViewById(R.id.list_activity_progressBar);
        mButton = (FloatingActionButton) findViewById(R.id.red_button);

        // LayoutManager handling
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // Adapter
        mAdapter = new ListItemsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

            // We reconstruct the adapter from the new data
            mAdapter.addItem();
            mRecyclerView.setAdapter(mAdapter);

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
            Toast.makeText(this,"Menu Clicked",Toast.LENGTH_SHORT).show(); //Todo use it or delete it
        }

        // Safe case
        return super.onOptionsItemSelected(item);

    }

    // Click handling
    @Override
    public void onClick(String itemInformationName,String itemInformationDate) {

        Toast.makeText(this,"Clicked",Toast.LENGTH_LONG).show();
        //todo

    }

    public void redButtonAction(View view) {

        // Action of the floating red Button
        Intent intent = new Intent(this, AddItems.class);
        startActivity(intent);

    }

    private void showItemsDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageView.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageView.setVisibility(View.VISIBLE);
    }

}

