package it.centotrenta.expridge;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by michelangelomecozzi on 19/07/2017.
 * <p>
 * 130 si volaa! Martin G
 */

public class Database implements Serializable{

    public ArrayList<String> itemsName;
    public ArrayList<Long> itemsDate;
    public ArrayList<Integer> itemsImage;

    public ArrayList<String> getItemsName(){

        return itemsName;

    }

    public ArrayList<Long> getItemsDate(){

        return itemsDate;

    }

    public ArrayList<Integer> getItemsImage(){

        return itemsImage;

    }

    public void setItemsName(ArrayList<String> arrayList){

        itemsName = arrayList;

    }

    public void setItemsDate(ArrayList<Long> arrayList){

        itemsDate = arrayList;

    }

    public void setItemsImage(ArrayList<Integer> arrayList){

        itemsImage = arrayList;

    }



    public void addItem(Items item){

        itemsName.add(item.get_itemName());
        itemsDate.add(item.get_date());
        itemsImage.add(R.drawable.dairy);
    }

    public String toDate(long milliSeconds, String format){

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        return formatter.format(calendar.getTime());
    }

    public ArrayList<String> getItemsDateFormatted() {

        return toArrayDate(itemsDate, "dd/MM/yyyy");
    }


    public ArrayList<String> toArrayDate(ArrayList<Long> milliSeconds, String format){

        ArrayList<String> string = new ArrayList<>();

        for(int i = 0; i<milliSeconds.size();i++) {

            SimpleDateFormat formatter = new SimpleDateFormat(format);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds.get(i));

            string.add(formatter.format(calendar.getTime()));

        }
        return string;
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
