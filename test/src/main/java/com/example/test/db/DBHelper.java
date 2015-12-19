package com.example.test.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 白杨 on 2015/12/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "tt", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql ="create table ApState(_id integer primary key autoincrement,name text,pwd text,state text )";
        db.execSQL(sql);
        String sql2 ="insert into ApState(name ,pwd ,state )values(?,?,?)";
        db.execSQL(sql2, new String[]{"xxx", "xxx", "0"});

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
