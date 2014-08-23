package com.csii.simpleone;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    MySQLiteOpenHelper sqLiteOpenHelper;
    final static int CODE_ONE = 1;
    final static int CODE_TWO = 2;
    final static int CODE_BATCH = 3;
    final static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(Constant.authorities, "books/#", CODE_ONE);
        matcher.addURI(Constant.authorities, "books/*", CODE_TWO);
        matcher.addURI(Constant.authorities, "books", CODE_BATCH);
    }
    @Override
    public boolean onCreate() {
        sqLiteOpenHelper = new MySQLiteOpenHelper(getContext(),
                Constant.DB_NAME, null, Constant.DB_VERSION);

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sd = sqLiteOpenHelper.getReadableDatabase();
        return sd.query(Constant.DB_BOOKS_TABLE_NAME, null, selection,
                selectionArgs, null, null, null);
    }

    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case CODE_ONE :
                return "vnd.android.cursor.item/book";
            case CODE_TWO :
                return "vnd.android.cursor.dir/book";
            default :
                break;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sd = sqLiteOpenHelper.getWritableDatabase();
        long position = -1;
        int code = matcher.match(uri);
        switch (code) {
            case CODE_BATCH :
                // 开始一个事物
                sd.beginTransaction();
                byte[] datas = values.getAsByteArray("datas");
                try {
                    JSONObject object = new JSONObject(new String(datas,
                            "UTF-8"));
                    JSONArray array = object.getJSONArray("List");
                    int length = array.length();
                    for (int i = 0; i < length; ++i) {
                        JSONObject item = array.getJSONObject(i);
                        ContentValues cv = new ContentValues();
                        Iterator<String> keys = item.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            cv.put(key, item.getString(key));
                        }
                        position = sd.insert(Constant.DB_BOOKS_TABLE_NAME,
                                null, cv);
                    }
                    sd.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                    position = -1;
                }
                sd.endTransaction();
                break;

            default :
                break;
        }
        sd.close();
        getContext().getContentResolver().notifyChange(uri, null);
        if (-1 == position) {
            return new Uri.Builder().scheme(uri.getScheme())
                    .authority(uri.getAuthority()).appendPath(uri.getPath())
                    .appendPath("" + position).build();
        }
        return new Uri.Builder().scheme(uri.getScheme())
                .authority(uri.getAuthority()).appendPath(uri.getPath())
                .appendPath("" + position).build();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        return 0;
    }

}
