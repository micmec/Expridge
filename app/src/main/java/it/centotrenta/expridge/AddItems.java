package it.centotrenta.expridge;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;


public class AddItems extends AppCompatActivity implements View.OnClickListener {

    // The variables
    private EditText nameInput;
    private CalendarView dateInput;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        // Get a handle on the views
        nameInput = (EditText) findViewById(R.id.name_input);
        dateInput = (CalendarView) findViewById(R.id.calendarView);
        dateInput.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Calendar c = Calendar.getInstance();

                    c.set(year,month,dayOfMonth);

                    view.setDate(c.getTimeInMillis());

                }
            }
        });
        button = (Button) findViewById(R.id.addButton);

    }

    @Override
    protected void onResume() {

        super.onResume();

        nameInput.clearComposingText();

    }

    @Override
    public void onClick(View v) {

        // Get the values
        String name = nameInput.getText().toString();
        long dateMilli = dateInput.getDate();

        // Change the main activity db
        MainActivity.db.addItem(new Items(name,dateMilli));

        // Get back to the activity
        super.onBackPressed();

    }

}
