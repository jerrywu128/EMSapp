package com.honestmc.ems.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.honestmc.ems.View.Activity.ConnectEMSActivity;
import com.honestmc.ems.View.Activity.EditActivity;
import com.honestmc.ems.View.Activity.LaunchActivity;
import com.honestmc.ems.connectedTools.MWifiManager;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;

public class ConnectEMSPresenter extends BasePresenter{
    private Activity activity;


    WifiManager wifiManager;
    MWifiManager mWifi;
    IntentFilter filter;
    public ConnectEMSPresenter(Activity activity){
        super(activity);
        this.activity = activity;
    }




    public void change_launch(){
        Intent mainIntent = new Intent(activity, EditActivity.class);
        activity.startActivity(mainIntent);
        activity.finish();
    }



    public void connectWifiQ(Context context, String ssid, String password){
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(activity.WIFI_SERVICE);
        mWifi = new MWifiManager(activity,wifiManager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mWifi.connectWifiQ(context,ssid,password,this);
        } else {
            filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            context.getApplicationContext().registerReceiver(mWifi.wifiBroadcastReceiver,filter);
            mWifi.connectWifi(ssid,password);

        }

    }

    public String getssid(){
        wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(activity.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo ();
        String ssid  = info.getSSID();
        return ssid;

    }

    public boolean isWifienabled(){
        wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(activity.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            return false;
        }
        else
            return true;

    }
}
