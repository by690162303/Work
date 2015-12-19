package com.example.test.db;

/**
 * Created by 白杨 on 2015/12/15.
 */
public interface ApInterface {


    /**
     * 获取到无线（AP）热点的状态
     * @return 1、标识连接状态 2、标识断开连接
     */
    public int getApState();

    /**
     * 改变状态 0、未打开 1、打开
     */
    public void changeState(int i);
}
