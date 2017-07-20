package it.centotrenta.expridge;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by michelangelomecozzi on 17/07/2017.
 * <p>
 * 130 si volaa!
 */

public class Items {

    private String _itemName;
    private long _date;

    public Items(String _itemName, long _date) {
        this._itemName = _itemName;
        this._date = _date;
    }


    public String get_itemName() {
        return _itemName;
    }

    public void set_itemName(String _itemName) {
        this._itemName = _itemName;
    }

    public String get_date() {

        return toDate(_date, "dd/MM/yyyy");

    }

    public void set_date(long _date) {
        this._date = _date;
    }



    public int getRelativeImages(String _itemName) {


        if (
                (_itemName.contains("Cheese")) | (_itemName.contains("CHEESE")) | (_itemName.contains("cheese")) |
                        (_itemName.contains("Milk")) | (_itemName.contains("MILK")) | (_itemName.contains("milk")) |
                        (_itemName.contains("Yogurt")) | (_itemName.contains("YOGURT")) | (_itemName.contains("yogurt")) |
                        (_itemName.contains("Yogurt")) | (_itemName.contains("YOGURT")) | (_itemName.contains("yogurt")) |
                        (_itemName.contains("Cream")) | (_itemName.contains("CREAM")) | (_itemName.contains("cream"))
                ) {
            return R.drawable.dairy;
        } else if (
                (_itemName.contains("Meat")) | (_itemName.contains("MEAT")) | (_itemName.contains("meat")) |
                        (_itemName.contains("Steak")) | (_itemName.contains("STEAK")) | (_itemName.contains("steak")) |
                        (_itemName.contains("Ham")) | (_itemName.contains("HAM")) | (_itemName.contains("ham")) |
                        (_itemName.contains("Bacon")) | (_itemName.contains("BACON")) | (_itemName.contains("bacon")) |
                        (_itemName.contains("Chicken")) | (_itemName.contains("CHICKEN")) | (_itemName.contains("chicken")) |
                        (_itemName.contains("Sausage")) | (_itemName.contains("SAUSAGE")) | (_itemName.contains("sausage")) |
                        (_itemName.contains("Beef")) | (_itemName.contains("BEEF")) | (_itemName.contains("beef")) |
                        (_itemName.contains("Chop")) | (_itemName.contains("CHOP")) | (_itemName.contains("chop")) |
                        (_itemName.contains("Lamb")) | (_itemName.contains("LAMB")) | (_itemName.contains("lamb")) |
                        (_itemName.contains("Burger")) | (_itemName.contains("BURGER")) | (_itemName.contains("burger")) |
                        (_itemName.contains("Mutton")) | (_itemName.contains("MUTTON")) | (_itemName.contains("mutton")) |
                        (_itemName.contains("Pork")) | (_itemName.contains("PORK")) | (_itemName.contains("pork")) |
                        (_itemName.contains("Turkey")) | (_itemName.contains("TURKEY")) | (_itemName.contains("turkey")) |
                        (_itemName.contains("Hot dog")) | (_itemName.contains("HOT DOG")) | (_itemName.contains("hot dog"))
                ) {
            return R.drawable.meat;
        } else {
            return R.mipmap.ic_launcher;
        }

    }

    public boolean isNil(){

        return (this._itemName==null || this._date == 0);

    }

    public String toDate(long milliSeconds, String format){

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        return formatter.format(calendar.getTime());
    }

}



