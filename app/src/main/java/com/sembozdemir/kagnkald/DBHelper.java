package com.sembozdemir.kagnkald;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semih Bozdemir on 23.2.2015.
 */


public class DBHelper extends SQLiteOpenHelper {

    private static final String NAME_DATABASE = "myDatesData7";
    // Contacts table name
    private static final String NAME_TABLE = "Dates";
    // Field names
    private static final String NAME_DATE = "date";
    private static final String NAME_DESCRIPTION = "description";


    public DBHelper(Context context) {
        super(context, NAME_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + NAME_TABLE +"(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT,description TEXT" + ")";
        Log.d("DBHelper", "SQL : " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertDate(DBDate date) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME_DATE, date.getDate());
        values.put(NAME_DESCRIPTION, date.getDescription());

        db.insert(NAME_TABLE, null, values);
        Log.d("DBHelper", "DB'e eklendi: " + values.toString());
        db.close();
    }

    public List<DBDate> getAllDates() {
        List<DBDate> listDates = new ArrayList<DBDate>();
        SQLiteDatabase db = getWritableDatabase();

        // String sqlQuery = "SELECT  * FROM " + NAME_TABLE;
        // Cursor cursor = db.rawQuery(sqlQuery, null);

        Cursor cursor = db.query(NAME_TABLE, new String[]{
                "id",
                NAME_DATE,
                NAME_DESCRIPTION}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            DBDate date = new DBDate();
            date.setId(cursor.getInt(0));
            date.setDate(cursor.getString(1));
            date.setDescription(cursor.getString(2));
            listDates.add(date);
        }

        return listDates;
    }


}
