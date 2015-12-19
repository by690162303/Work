package com.example.test.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 白杨 on 2015/12/15.
 */
public class Impl implements ApInterface {
    private Context contex;
    private DBHelper db ;

    public Impl(Context contex) {
        this.contex = contex;
        db = new DBHelper(contex);
    }

    @Override
    public int getApState() {
        SQLiteDatabase data = db.getReadableDatabase();
        String sql ="select * from ApState";
        Cursor c = data.rawQuery(sql, null);
        String state = null;
        while(c.moveToNext()) {
            state = c.getString(c.getColumnIndex("state"));
        }
        c.close();
        return Integer.parseInt(state);
    }

    @Override
    public void changeState(int i) {
        SQLiteDatabase data = db.getReadableDatabase();
        String sql = "update ApState set state ="+i;//修改的SQL语句
        data.execSQL(sql);//执行修改
    }
}
