package it.centotrenta.expridge;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by michelangelomecozzi on 19/07/2017.
 * <p>
 * 130 si volaa! Martin G
 */

public class Database {

    public ArrayList<String> itemsName = new ArrayList<>();
    public ArrayList<String> itemsDate = new ArrayList<>(); // Todo want to save the LONG value, so we can sort
    public String TAG = "Db";

    public ArrayList<String> getItemsName(){

        return itemsName;

    }

    public ArrayList<String> getItemsDate(){ //

        return itemsDate;

    }


    public void addItem(Items item){

        if(!itemsName.isEmpty() && !itemsDate.isEmpty()){

            itemsName.add(item.get_itemName());
            itemsDate.add(item.get_date());
            Log.d(TAG,"First Choice");
        }
        else{

            itemsName = new ArrayList<>();
            itemsName.add(item.get_itemName());
            itemsDate = new ArrayList<>();
            itemsDate.add(item.get_date());
            Log.d(TAG,"Second Choice");

        }

    }

    public String toDate(long milliSeconds, String format){

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        return formatter.format(calendar.getTime());
    }

    public boolean isNil(){

        if(this.getItemsName().isEmpty() || this.getItemsDate().isEmpty() ) //|| Database.getItemsImage().isEmpty())
            return true;
        else
            return false;

    }

    public Database() {

        itemsName = new ArrayList<>();
        itemsDate = new ArrayList<>();

    }

}
