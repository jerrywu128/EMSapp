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

    public boolean check_edit_have_null(){
        return launchView.check_edit_have_null();
    }

    public String getCurrentDate(){
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH/mm/ss", Locale.CHINA);
        return sdf.format(date);
    }

    public String getAppValidationCode(String name){
        String inputString = name + "/honestmc-tw";
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = inputString.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public boolean writeInfoToFile(String hospital,String section,String job_number,String user_name){
        StringBuilder userInfo = new StringBuilder();
        userInfo.append("Hospital:"+hospital+"\n");
        userInfo.append("Section:"+section+"\n");
        userInfo.append("Job Number:"+job_number+"\n");
        userInfo.append("Name:"+user_name+"\n");
        userInfo.append("Date:"+getCurrentDate()+"\n");
        userInfo.append("Validation:"+ getAppValidationCode(user_name)+"\n");

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(activity.openFileOutput("USER_INFO", Context.MODE_PRIVATE));
            outputStreamWriter.write(userInfo.toString());
            outputStreamWriter.close();
            return true;
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }

    }

}
