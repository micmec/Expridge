package it.centotrenta.expridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity{

    //On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToAdd(View view) {
        startActivity(new Intent(this, AddItems.class));
    }

    public void goToList(View view) {
        startActivity(new Intent(this, ListItems.class));
    }

}