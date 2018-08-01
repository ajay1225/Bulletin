package com.example.lenovo.bulletin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    Context context;
    private static final String DATABASENAME="NewsDatabase";
    private static String CREATE_QUERY="";
    public DBHelper(Context context) {
        super(context, DATABASENAME, null, 1);
        CREATE_QUERY="create table "+ NewsContentContract.NewsEntry.Tablename+ " ("+NewsContentContract.NewsEntry.COL_1+" text,"+ NewsContentContract.NewsEntry.COL_2+" text);";
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+NewsContentContract.NewsEntry.Tablename);
        onCreate(db);

    }
}
