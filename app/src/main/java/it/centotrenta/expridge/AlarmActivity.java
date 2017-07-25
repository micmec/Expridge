package it.centotrenta.expridge;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AlarmActivity extends AppCompatActivity {

    //TODO image selection {#link} https://stackoverflow.com/questions/3609231/how-is-it-possible-to-create-a-spinner-with-images-instead-of-text

    Spinner spinner;
    public static final String MY_PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.minutes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences pref = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        int id = pref.getInt("idAlarm",0);
        spinner.setSelection(id);

        spinner.getSelectedItem();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences pref = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if(parent.getSelectedItem().toString() != null)
                {
                    try {
                        String selection = parent.getSelectedItem().toString();
                        int hours = Integer.parseInt(selection.replaceAll("[\\D]", ""));
                        editor.putInt("alarmValue",hours);
                        editor.putInt("idAlarm",position);
                        editor.apply();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else
                {
                    editor.putInt("alarmValue",86400000);
                    editor.apply();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                SharedPreferences pref = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("alarmValue",86400000);
                editor.apply();
            }
        });
    }


}
