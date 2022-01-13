package com.dotify.music.dotify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class AlarmDatabase extends SQLiteOpenHelper {

    private static final String TAG = "AlarmDatabase";

    private static final String TABLE_NAME = "Alarm";
    private static final String ID = "AlarmId";
    private static final String COL1 = "Hour";
    private static final String COL2 = "Minute";

    public AlarmDatabase(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME
                + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL1 +" INTEGER NOT NULL, "
                + COL2 + " INTEGER NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addAlarm(int hour, int minute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, hour);
        contentValues.put(COL2, minute);
        Log.d(TAG, "Add new alarm(" + hour + "," + minute + ")");
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean updateAlarm(int id, int hour, int minute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, hour);
        contentValues.put(COL2, minute);
        Log.d(TAG, "Update alarm(" + id + ","+ hour + "," + minute + ")");
        long result = db.update(TABLE_NAME, contentValues,  ID + "=" + id, null);
        return result != -1;
    }

    public boolean dropAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Delete alarm(" + id + ")");
        long result = db.delete(TABLE_NAME, ID + "=" + id, null);
        return result != -1;
    }

    public HashMap<String, Integer> getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL1 + "," + COL2 + " FROM " + TABLE_NAME + " WHERE " + ID + "=" + id , null);
        HashMap<String, Integer> alarm = new HashMap<>();
        if (cursor != null) {
            cursor.moveToFirst();
            alarm.put("hour", cursor.getInt(cursor.getColumnIndex(COL1)));
            alarm.put("minute", cursor.getInt(cursor.getColumnIndex(COL2)));
            cursor.close();
        }
        return alarm;
    }

    public ArrayList<HashMap<String, Integer>> getAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID, COL1, COL2}, null, null,
                null, null, null);
        ArrayList<HashMap<String, Integer>> alarms = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, Integer> alarm = new HashMap<>();
            alarm.put("id", cursor.getInt(cursor.getColumnIndex(ID)));
            alarm.put("hour", cursor.getInt(cursor.getColumnIndex(COL1)));
            alarm.put("minute", cursor.getInt(cursor.getColumnIndex(COL2)));
            alarms.add(alarm);
        }
        cursor.close();
        return alarms;
    }
}
