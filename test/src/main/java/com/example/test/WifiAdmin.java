package com.example.test;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.test.db.Impl;

import java.util.List;

public class WifiAdmin {
    // 定义WifiManager对象   
    private WifiManager mWifiManager;
    // 定义WifiInfo对象   
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表   
    private List<ScanResult> mWifiList;
    // 网络连接列表   
    private List<WifiConfiguration> mWifiConfiguration;
    // 定义一个WifiLock   
    WifiLock mWifiLock;
    private Context context;
    private Impl impl ;
    String TAG = "aaa";
    private WifiHostBiz mWifiHostBiz;
    private CloseApInterface close;
    int connt = 0;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    connt ++;
                    Log.e("aa","判断网络状态");
                    if(checkNetCardState()==3){
                       close.netWifi();
                    }else{
                        handler.sendEmptyMessageAtTime(1,1000);
                    }
                    break;

            }
        }
    };
    public void setClose(CloseApInterface close) {
        this.close = close;
    }

    // 构造器
    public WifiAdmin(Context context) {
        // 取得WifiManager对象   
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        this.context = context;
        impl = new Impl(context);

        // 取得WifiInfo对象   
        mWifiInfo = mWifiManager.getConnectionInfo();
//        mWifiManager.setWifiEnabled(true);
    }

    // 打开WIFI   
    public void openWifi() {
//        if(PreferencesUtils.getString("Ap",context).equals("1")){
//            Toast.makeText(context,"热点是开启状态，正在关闭。。稍后开启wifi",Toast.LENGTH_SHORT).show();
            close.close();
//        }
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        handler.sendEmptyMessageAtTime(1,1000);
    }

    // 关闭WIFI   
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    // 检查当前WIFI状态   
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    // 锁定WifiLock   
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    // 解锁WifiLock   
    public void releaseWifiLock() {
        // 判断时候锁定   
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    // 创建一个WifiLock   
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    // 得到配置好的网络   
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }

    // 指定配置好的网络进行连接   
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回   
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 连接配置好的指定ID的网络   
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }

    public void startScan() {
        mWifiManager.startScan();
        // 得到扫描结果   
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接   
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }

    // 得到网络列表   
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    // 查看扫描结果   
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包   
            // 其中把包括：BSSID、SSID、capabilities、frequency、level   
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    // 得到MAC地址   
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    // 得到接入点的BSSID   
    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    // 得到IP地址   
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    // 得到连接的ID   
    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    // 得到WifiInfo的所有信息包   
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    /**
     * 检查当前Wifi网卡状态
     */
    public int checkNetCardState() {
        if (mWifiManager.getWifiState() == 0) {
            return 0;
        } else if (mWifiManager.getWifiState() == 1) {
            return 1;
        } else if (mWifiManager.getWifiState() == 2) {
            return 2;
        } else if (mWifiManager.getWifiState() == 3) {
            return 3;
        } else {
            Log.i(TAG, "没有获取到状态-_-");
            return -1;
        }
    }

    // 添加一个网络并连接   
    public void addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b =  mWifiManager.enableNetwork(wcgID, true);
        System.out.println("a--" + wcgID);
        System.out.println("b--" + b);
    }

    // 断开指定ID的网络   
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

//然后是一个实际应用方法，只验证过没有密码的情况： 

    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type)
    {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if(tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if(Type == 1) //WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(Type == 2) //WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+Password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(Type == 3) //WIFICIPHER_WPA
        {
            config.preSharedKey = "\""+Password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    private WifiConfiguration IsExsits(String SSID)
    {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs)
        {
            if (existingConfig.SSID.equals("\""+SSID+"\""))
            {
                return existingConfig;
            }
        }
        return null;
    }
}
