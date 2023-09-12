package com.example.roadresq;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my_database.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your database tables here
        db.execSQL("CREATE TABLE IF NOT EXISTS user_data (phoneNumber TEXT PRIMARY KEY, name TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle schema changes, if needed
    }
    public SQLiteDatabase openDatabase() {
        return getWritableDatabase();
    }

    public void closeDatabase(SQLiteDatabase db) {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

}