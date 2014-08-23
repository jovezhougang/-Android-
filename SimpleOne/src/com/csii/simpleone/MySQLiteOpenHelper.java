package com.csii.simpleone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public MySQLiteOpenHelper(Context context, String name,
            CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constant.CRAETE_BOOKS_TABLE_SQL);
        System.out.println("MySQLiteOpenHelper 数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("MySQLiteOpenHelper onUpgrade() oldVersion:"
                + oldVersion + "newVersion:" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS books");
        db.execSQL(Constant.CRAETE_BOOKS_TABLE_SQL);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("MySQLiteOpenHelper onDowngrade() oldVersion:"
                + oldVersion + "newVersion:" + newVersion);
    }

}
