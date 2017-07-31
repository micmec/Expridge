package it.centotrenta.expridge;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import it.centotrenta.expridge.Utilities.DBHandler;

public class AddItems extends AppCompatActivity implements View.OnClickListener {

    private EditText nameInput;
    private CalendarView dateInput;
    private DBHandler dbHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        dbHandler = MainActivity.dataBaseHandler;
        nameInput = (EditText) findViewById(R.id.name_input);
        dateInput = (CalendarView) findViewById(R.id.calendarView);
        dateInput.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Calendar c = Calendar.getInstance();
                    c.set(year, month, dayOfMonth);
                    view.setDate(c.getTimeInMillis());

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        nameInput.clearComposingText();

    }

    @Override
    public void onClick(View v) {
        String name = nameInput.getText().toString();
        long dateMill = dateInput.getDate();
        dbHandler.addItem(name, dateMill);
        MainActivity.mAdapter.addToClickList();
        MainActivity.mAdapter.addAnimationsToItem(getApplicationContext());
        super.onBackPressed();
    }

}
