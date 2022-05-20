package com.honestmc.ems.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.honestmc.ems.View.Activity.ConnectEMSActivity;
import com.honestmc.ems.View.Activity.LaunchActivity;
import com.honestmc.ems.View.Interface.LaunchView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LaunchPresenter extends BasePresenter {
    private LaunchView launchView;
    private Activity activity;
    public LaunchPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
    }
    public void setView(LaunchView launchView) {
        this.launchView = launchView;
        initCfg();
    }

    public void setWifiwithQrcode(LaunchActivity LA, String qrdata){

        Intent mainIntent = new Intent(LA, ConnectEMSActivity.class);
        Bundle bundle = new Bundle();

        String password;
        String passwordTemp = qrdata.substring(qrdata
                .indexOf("P:"));
        password = passwordTemp.substring(2,
                passwordTemp.indexOf(";"));

        String ssid;
        String ssidTemp = qrdata.substring(qrdata
                .indexOf("S:"));
        ssid = ssidTemp.substring(2,
                ssidTemp.indexOf(";"));

        String type;
        String typeTemp = qrdata.substring(qrdata
                .indexOf("T:"));
        type = typeTemp.substring(2,
                typeTemp.indexOf(";"));


        bundle.putString("ssid",ssid);
        bundle.putString("pw",password);
        bundle.putString("tp",type);
        mainIntent.putExtras(bundle);

        LA.startActivity(mainIntent);
    }
    public boolean isWifienabled(){
        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()){
            return true;
        }
        else
            return false;
    }



}
