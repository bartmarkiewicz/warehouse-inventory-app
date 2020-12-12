package com.androidprojects.bartek.warehouseinventoryapp.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    public static final String CONTENT_AUTHORITY = "fit2081.app.Bartosz";

    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    ItemDatabase db;
    //private static final UriMatcher uriMatcherStatic = createUriMatcher();


    public MyContentProvider() {
    }

    /*
    private static UriMatcher createUriMatcher() {
        //todo
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, "items", MUl);

        return uriMatcher;
    } */



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleteCount;

        deleteCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .delete("items", selection,selectionArgs);

        return deleteCount;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rId = db
                .getOpenHelper()
                .getWritableDatabase()
                .insert("items", 0, values);
        return ContentUris.withAppendedId(CONTENT_URI, rId);
    }

    @Override
    public boolean onCreate() {
        db = ItemDatabase.getDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("items");

        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);

        final Cursor cursor = db
                .getOpenHelper()
                .getReadableDatabase()
                .query(query, selectionArgs);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updateCount;
        updateCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .update("items", 0, values, selection,selectionArgs);

        return updateCount;
    }
}
