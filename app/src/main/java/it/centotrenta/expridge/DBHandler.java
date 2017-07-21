package it.centotrenta.expridge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by michelangelomecozzi on 20/07/2017.
 * <p>
 * 130 si volaa!
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dataBase.db";
    private static final String TABLE_NAME = "itemsTable";
    private static final String ITEM_ID_COLUMN = "ID";
    private static final String ITEM_NAME_COLUMN = "Items";
    private static final String ITEM_DATE_COLUMN = "Dates";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " + ITEM_ID_COLUMN
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM_NAME_COLUMN
                + " TEXT, " + ITEM_DATE_COLUMN + " LONG);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void addItem(String name, long date){

        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_NAME_COLUMN,name);
        values.put(ITEM_DATE_COLUMN,date);
        try {
            database.insert(TABLE_NAME, null, values);
            database.close();
        }
        catch (Exception e){

            e.printStackTrace();
            database.close();

        }

    }

    public void deleteItem(String name, long date){

        SQLiteDatabase database = getWritableDatabase();


        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE "
                        + ITEM_NAME_COLUMN + " =\"" + name + "\";"); //AND " + ITEM_DATE_COLUMN + " =\"" + date + "\";");
        //CONSIDER NOT DELETING BUT CHANGING STATUS TODO this method deletes all the items with that name. Add the ID param.
        database.close();

    }

    public ArrayList<String> getNamesFromColumn(){

        SQLiteDatabase database = getWritableDatabase();


        ArrayList<String> mList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT " + ITEM_NAME_COLUMN + " FROM " + TABLE_NAME + " ORDER BY " + ITEM_DATE_COLUMN + " DESC",null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){

//            if(cursor.getString(cursor.getColumnIndex(ITEM_NAME_COLUMN))!=null){

                mList.add(cursor.getString(cursor.getColumnIndex(ITEM_NAME_COLUMN)));
                cursor.moveToNext();

           // }

        }
            cursor.close();
            database.close();

        return mList;
    }

    public ArrayList<String> getDatesFromColumn(){

        SQLiteDatabase database = getWritableDatabase();

        ArrayList<String> mList = new ArrayList<>();

            Cursor cursor = database.rawQuery("SELECT " + ITEM_DATE_COLUMN + " FROM " + TABLE_NAME + " ORDER BY " + ITEM_DATE_COLUMN
                    + " DESC", null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                    mList.add(toDate(cursor.getLong(cursor.getColumnIndex(ITEM_DATE_COLUMN)), "dd/MM/yyyy"));
                    cursor.moveToNext();

            }
            cursor.close();
            database.close();


        return mList;
    }

    public String toDate(long milliSeconds, String format){

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        return formatter.format(calendar.getTime());
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }
}
