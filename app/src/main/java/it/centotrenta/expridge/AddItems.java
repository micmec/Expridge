package it.centotrenta.expridge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;


public class AddItems extends AppCompatActivity implements View.OnClickListener {

    private EditText nameInput;
    private CalendarView dateInput;
    private Button button;
    private Database db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        nameInput = (EditText) findViewById(R.id.name_input);
        dateInput = (CalendarView) findViewById(R.id.calendarView);
        button = (Button) findViewById(R.id.addButton);

        db = (Database) getIntent().getSerializableExtra("Db");
    }


    @Override
    public void onClick(View v) {

        String name = nameInput.getText().toString();
        long dateMilli = dateInput.getDate();
        Items item = new Items(name,dateMilli);

        db.addItem(item);
        Toast.makeText(AddItems.this, "DATA INSERTED", Toast.LENGTH_LONG).show();

        super.onBackPressed();

    }

//    public DateCustom createDateCustom(String string){
//
//        string = string.replaceAll("[^0-9]+", " ");
//
//        List<String> custom = Arrays.asList(string.trim().split(" "));
//
//        int d = Integer.parseInt(custom.get(1));
//        int m = Integer.parseInt(custom.get(2));
//        int y = Integer.parseInt(custom.get(3));
//
//        return new DateCustom(d,m,y);
//
//    }

}
