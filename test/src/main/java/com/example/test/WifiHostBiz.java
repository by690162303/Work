package com.example.test;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Method;

//开启wifi热点的
public class WifiHostBiz {
    private final String TAG = "WifiHostBiz";
    private WifiManager wifiManager;
    private String WIFI_HOST_SSID = "AndroidAP";
    private String WIFI_HOST_PRESHARED_KEY = "12345678";// 密码必须大于8位数
    private WifiAdmin mWifiAdmin;
    private boolean isWifiOpean = false;
    private Context mContext;
    private boolean isShowDialog = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 6:
                    Log.i(TAG, "通知要关闭 wifi");
                    mWifiAdmin.closeWifi();
                    mHandler.sendEmptyMessageDelayed(7, 1000);
                    break;
                case 7:
                    if(mWifiAdmin.checkNetCardState() == 1){
                        changeHotState(true);
                    }else{
                        mHandler.sendEmptyMessageDelayed(7,1000);
                    }
                    break;
            }
        }
    };

    public WifiHostBiz(Context context) {
        super();
        mContext = context;
        //获取wifi管理服务
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled(false);
        mWifiAdmin = new WifiAdmin(context);
    }

    /**
     * 判断热点开启状态
     */
    public boolean isWifiApEnabled() {
        return getWifiApState() == WIFI_AP_STATE.WIFI_AP_STATE_ENABLED;
    }

    private WIFI_AP_STATE getWifiApState() {
        int tmp;
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApState");
            tmp = ((Integer) method.invoke(wifiManager));
            // Fix for Android 4
            if (tmp > 10) {
                tmp = tmp - 10;
            }
            return WIFI_AP_STATE.class.getEnumConstants()[tmp];
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return WIFI_AP_STATE.WIFI_AP_STATE_FAILED;
        }
    }

    public enum WIFI_AP_STATE {
        //泛型 类
        WIFI_AP_STATE_DISABLING, // 禁止使用
        WIFI_AP_STATE_DISABLED,  // 关闭中
        WIFI_AP_STATE_ENABLING,  //使能够、授权
        WIFI_AP_STATE_ENABLED,   //开启中、激活
        WIFI_AP_STATE_FAILED     //申请
        }

    /**
     * wifi热点开关、若是当前Ap为关闭状态，就将其打开，若是开启状态就将其关闭
     */
    public void changeWifiApState(boolean flage) {
        if (flage) {
            if(mWifiAdmin.checkNetCardState()==1){
                changeHotState(true);//开启热点
            }else{
                mHandler.sendEmptyMessage(6);
            }
        } else {

                Log.e("aa","关闭无线");
                changeHotState(false);


        }
//		if (enabled) { // disable WiFi in any case
        //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
//			wifiManager.setWifiEnabled(false); //关闭wifi
//			mWifiAdmin.closeWifi();
//			System.out.println(TAG + ":关闭wifi");
//		} else {
//			wifiManager.setWifiEnabled(true); //打卡wifi
//			mWifiAdmin.openWifi();
//		}

    }

    /**
     * 改变热点状态
     *
     * @param enabled true 打开、false 关闭
     * @return 打开热点开启状态
     */
    public Boolean changeHotState(boolean enabled) {
        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称(可以在名字后面加点随机数什么的)
            apConfig.SSID = WIFI_HOST_SSID;
            //配置热点的密码
            apConfig.preSharedKey = WIFI_HOST_PRESHARED_KEY;
            //安全：WPA2_PSK
            apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            //通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            //返回热点打开状态
            return (Boolean) method.invoke(wifiManager, apConfig, enabled);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
