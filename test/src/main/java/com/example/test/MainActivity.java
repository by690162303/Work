package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.db.Impl;
import com.example.test.utils.PreferencesUtils;


public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private String Name;
    private String Passwd;
    private EditText etname;
    private EditText etpwd;
    private WifiAdmin wifiAdmin;
    private WifiHostBiz mWifiHostBiz;
    private Impl impl ;
    private Button bt_open;
    private Button bt_net;
    private TextView tt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etname = (EditText) this.findViewById(R.id.ed_name);
        etpwd = (EditText) this.findViewById(R.id.ed_pwd);
        bt_open = (Button) this.findViewById(R.id.bt_open);
        bt_net = (Button) this.findViewById(R.id.net_wifi);
        tt = (TextView) this.findViewById(R.id.tt);
//        PreferencesUtils.putString("Ap", "0", this);
        WifiHostBiz  wifiHost = new WifiHostBiz(this);
//        wifiHost.setWifiApEnabled(false);
        impl = new Impl(this);
        mWifiHostBiz = new WifiHostBiz(this);
        wifiAdmin = new WifiAdmin(this);
        wifiAdmin.setClose(new CloseApInterface() {
            @Override
            public void close() {
                Log.e("aa","需要操作关闭热点");
                mWifiHostBiz.changeHotState(false);
                PreferencesUtils.putString("Ap", "0",getApplicationContext());
            }

            @Override
            public void netWifi() {
                netWifi2();
                bt_open.setEnabled(false);
            }
        });
        //打开wifi

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {
            Name = bundle.getString("name");
            Passwd = bundle.getString("passwd");
        }
        etname.setText(Name);
        etpwd.setText(Passwd);
        bt_open.performClick();
    }
    public void onclicks(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
                Toast.makeText(getApplicationContext(), "打开无线", Toast.LENGTH_SHORT).show();
                wifiAdmin.openWifi();
                break;
            case R.id.bt_close:
                Toast.makeText(getApplicationContext(), "关闭无线", Toast.LENGTH_SHORT).show();
                wifiAdmin.closeWifi();
                bt_open.setEnabled(true);
                break;
            case R.id.bt_hotopen:
                Toast.makeText(getApplicationContext(), "打开热点", Toast.LENGTH_SHORT).show();
//                impl.changeState(1);
                PreferencesUtils.putString("Ap","1",this);
                mWifiHostBiz.changeWifiApState(true);
                break;
            case R.id.bt_hotclose:
                Log.e("aa","这是："+PreferencesUtils.getString("Ap", this));
                if("0".equals(PreferencesUtils.getString("Ap", this)) || null == PreferencesUtils.getString("Ap", this)){
                    Toast.makeText(getApplicationContext(), "热点还未开启", Toast.LENGTH_SHORT).show();
                    break;
                }
                Toast.makeText(getApplicationContext(), "关闭热点", Toast.LENGTH_SHORT).show();
                mWifiHostBiz.changeWifiApState(false);
                PreferencesUtils.putString("Ap", "0", this);
                break;
            case R.id.net_wifi:
                //连接到无线
                netWifi2();
                break;
            case R.id.net_hot:
                //连接 到热点
//                wifiAdmin.checkNetCardState();
                tt.setText("");
                String type = GetNetworkType();

                tt.setText(tt.getText().toString()+"\n"+type);
                break;
        }

    }

    public void netWifi2() {
        Log.e("aa","连接了没有");
                wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(Name, Passwd, 3));
    }
    public  String GetNetworkType()
    {
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                strNetworkType = "WIFI";
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String _strSubTypeName = networkInfo.getSubtypeName();
                tt.setText(tt.getText().toString()+"\n"+"Network getSubtypeName : " + _strSubTypeName);
                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
                        {
                            strNetworkType = "3G";
                        }
                        else
                        {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
                tt.setText(tt.getText().toString()+"\n"+"Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }
        tt.setText(tt.getText().toString()+"\n"+"Network Type : " + strNetworkType);


        return strNetworkType;
    }
}
