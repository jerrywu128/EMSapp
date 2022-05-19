package com.honestmc.ems.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.honestmc.ems.View.Activity.ConnectEMSActivity;
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

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                try{
                    FileInputStream in = new FileInputStream(new File(activity.getFilesDir().toString()+"/USER_INFO"));

                    FTPClient ftpClient = new FTPClient();
                    ftpClient.connect(MWifiManager.getIp(activity),21);
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.login("pi", "Honestmc12345");

                    String encoding = System.getProperty("file.encoding");
                    boolean change = ftpClient.changeWorkingDirectory("/home/pi/report");
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    if (change) {
                        boolean result = false;
                        result = ftpClient.storeFile(new String("USER_INFO".getBytes(encoding), "iso-8859-1"), in);
                        if (result) {
                            Log.i("ftp-file","上傳成功!");
                        }else {
                            Log.e("ftp-file","上傳失敗!");
                        }
                    }
                    int x = check_Info(ftpClient,"/home/pi/report");

                    switch (x){
                        case 1:
                            Log.i("ftp-file","have INFO-OK!");
                            break;
                        case 2:
                            Log.e("ftp-file","have INFO-OK but delete failed!");
                            break;
                        case 3:
                            Log.i("ftp-file","have INFO-ERROR!");
                            break;
                        case 4:
                            Log.i("ftp-file","have INFO-ERROR but delete failed!");
                            break;
                        case 5:
                            Log.i("ftp-file","folder is empty!");
                            break;
                        case 6:
                            Log.i("ftp-file","don't have OK or ERROR files!");
                            break;
                    }

                    Looper.prepare();
                    Toast.makeText(activity,"ftp connect success",Toast.LENGTH_LONG).show();
                    Looper.loop();

                }catch (Exception e){
                    Log.e("ftp-file","上傳error!");
                    Looper.prepare();
                    Toast.makeText(activity,e.toString(),Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
                    }
        });

        thread.start();


    }

    public int check_Info(FTPClient ftpClient,String pathname) throws Exception{
        int empty_count = 0;
        Thread.sleep(5);
        FTPFile[] files = ftpClient.listFiles();

        for(FTPFile file : files){
            Log.i("fileis",file.toString());
            empty_count++;


            if(file.getName().equals("INFO_OK")) {
                if (ftpClient.deleteFile(pathname + "/" + file.getName())) {
                    return 1;
                }else{
                    Log.i("fileis",file.getName()+"delete failed");
                    return 2;
                }
            }

            if(file.getName().equals("INFO_ERROR")){
                Log.i("fileis",file.getName()+"info_error");
                if (ftpClient.deleteFile(pathname + "/" + file.getName())) {
                    return 3;
                }else{
                    Log.i("fileis",file.getName()+"delete failed");
                    return 4;
                }
            }

            Thread.sleep(5);
        }
        if(empty_count==0){
            return 5;
        }else{
            return 6;
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
