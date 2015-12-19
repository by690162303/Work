package com.example.test.utils;

/**
 * Created by 白杨 on 2015/10/23.
 */

import android.util.Log;

/**
 * LOG日志弹出工具类
 */
public class L {
    /**
     * 是否发布程序，如果发布 true(不会弹出Log日志) 测试使用false
     */
    public static boolean release = false;
    public static void e(String tag,String msg){
        if(release){
            return ;
        }
        Log.e(tag,msg);
    }
    public static void v(String tag,String msg){
        if(release){
            return ;
        }
        Log.v(tag, msg);
    }
    public static void i(String tag,String msg){
        if(release){
            return ;
        }
        Log.i(tag, msg);
    }    public static void d(String tag,String msg){
        if(release){
            return ;
        }
        Log.d(tag, msg);
    }
    public static void w(String tag,String msg){
        if(release){
            return ;
        }
        Log.w(tag, msg);
    }

}
