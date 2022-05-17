package com.honestmc.ems.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.widget.Toast;

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


    public void launchEMS(){
        try{
            FileInputStream in = new FileInputStream(new File(activity.getFilesDir().toString()+"/USER_INFO"));
            Toast.makeText(activity,activity.getFilesDir().toString(),Toast.LENGTH_LONG).show();
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(MWifiManager.getIp(activity),21);
            ftpClient.enterLocalPassiveMode();
            ftpClient.login("pi", "Honestmc12345");
            //FTPFile[] files = ftpClient.listFiles("/home/pi/report");
            String encoding = System.getProperty("file.encoding");
            boolean change = ftpClient.changeWorkingDirectory("/home/pi/report");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (change) {
                boolean result = false;
                result = ftpClient.storeFile(new String("USER_INFO".getBytes(encoding), "iso-8859-1"), in);
                if (result) {
                    System.out.println("上傳成功!");
                }
            }
            Toast.makeText(activity,"ftp connect success",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(activity,e.toString(),Toast.LENGTH_LONG).show();
        }
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
