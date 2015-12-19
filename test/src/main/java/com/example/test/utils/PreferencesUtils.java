package com.example.test.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 白杨 on 2015/11/5.
 */
public class PreferencesUtils {
    /**
     * 存储String在全局共享参数里面
     *
     * @param key     存储的键
     * @param value   存储的值
     * @param context 上下文文本
     *
     */
    public static void putString(String key, String value, Context context) {
        SharedPreferences sharepreferences = context.getSharedPreferences("xxx.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 根据键在共享参数里面取值
     * @param key 键
     * @param context 上下文本
     * @return 值
     */
    public static String getString(String key, Context context) {
        SharedPreferences shp = context.getSharedPreferences("xxx.xml", Context.MODE_PRIVATE);
        return shp.getString(key,"false");
    }
}
