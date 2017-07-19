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

public class ListItems extends AppCompatActivity implements ListItemsAdapter.ListItemsAdapterClickHandler {

    // Our variables
    private RecyclerView listRecyclerView;
    private static ListItemsAdapter listAdapter;
    private TextView listErrorMessageView;
    private ProgressBar listProgressBar;
    private FloatingActionButton redButton;
    public Database db = new Database();

    /*
    TODO create the AsyncTask process for getting information on the items (idea) and so use the showErrorMessage/showListItems
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        // Reference to the objects in the layouts/other
        listRecyclerView = (RecyclerView) findViewById(R.id.list_activity_recyclerView);
        listErrorMessageView = (TextView) findViewById(R.id.list_activity_errorMessage);
        listProgressBar = (ProgressBar) findViewById(R.id.list_activity_progressBar);
        redButton = (FloatingActionButton) findViewById(R.id.red_button);

        // LayoutManager handling
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        listRecyclerView.setLayoutManager(layoutManager);
        listRecyclerView.setHasFixedSize(true);

        // Adapter
        listAdapter = new ListItemsAdapter(this,db);
        listRecyclerView.setAdapter(listAdapter);

        loadDB();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        loadDB();

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
            Intent intent = new Intent(this, AddItems.class);
            startActivity(intent);
        }

        // Safe case
        return super.onOptionsItemSelected(item);

    }

    // Click handling
    @Override
    public void onClick(String itemInformation) {

        Intent intent = new Intent(this, AddItems.class);
        intent.putExtra("Db",db);
        startActivity(intent);

    }

    public void redButtonAction(View view) {

        loadDB();

    }

    public void loadDB(){

        showItemsDataView();
//        if(!Database.isNil())
//        {
//            itemsNameArray = Database.getItemsName();
//            itemsDateArray = Database.getItemsDateFormatted();
//            itemsImageArray = Database.getItemsImage();
//        }

    }



    private void showItemsDataView() {
        /* First, make sure the error is invisible */
        listErrorMessageView.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        listRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        listRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        listErrorMessageView.setVisibility(View.VISIBLE);
    }

    public Database getDb(){

        return db;

    }

}

