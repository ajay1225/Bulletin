package com.example.lenovo.bulletin;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MyContentProvider extends ContentProvider {
    private static final int FAVOURITE = 100;
    private static final int FAV_WITH_ID = 101;
    public UriMatcher sUriMatcher=buildUriMatcher();
    private DBHelper dbHelper;
    public MyContentProvider() {
    }
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(NewsContentContract.AUTHORITY, NewsContentContract.PATH_TASKS, FAVOURITE);
        uriMatcher.addURI(NewsContentContract.AUTHORITY, NewsContentContract.PATH_TASKS + "/*", FAV_WITH_ID);

        return uriMatcher;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        int deleteValue;
        int id=sUriMatcher.match(uri);
        switch(id){
            case FAVOURITE:
                deleteValue=sqLiteDatabase.delete(NewsContentContract.NewsEntry.Tablename,selection,selectionArgs);
                break;
            case FAV_WITH_ID:
                deleteValue=sqLiteDatabase.delete(NewsContentContract.NewsEntry.Tablename,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");

        }
        if(deleteValue!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return deleteValue;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        int id=sUriMatcher.match(uri);
        Uri inserUri;
        switch(id){
            case FAVOURITE:
                long value=sqLiteDatabase.insert(NewsContentContract.NewsEntry.Tablename,null,values);
                if(value>0){
                    inserUri= ContentUris.withAppendedId(uri,value);
                }else
                {
                    throw new android.database.SQLException("unable to insert");
                }
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return inserUri;

    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        SQLiteDatabase sqLiteDatabase=dbHelper.getReadableDatabase();
        int id=sUriMatcher.match(uri);
        switch (id) {
            case FAVOURITE:
                cursor = sqLiteDatabase.query(NewsContentContract.NewsEntry.Tablename, null, selection, selectionArgs, null, null, null);
                break;
            case FAV_WITH_ID:
                cursor = sqLiteDatabase.query(NewsContentContract.NewsEntry.Tablename, null, selection, selectionArgs, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
