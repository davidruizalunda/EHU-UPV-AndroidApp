package com.example.tfgprueba2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "dbname";
    private static final String TABLE_LABELS = "table_name";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "column_name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);

        onCreate(db);
    }

    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LABELS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

}