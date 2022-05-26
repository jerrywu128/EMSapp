package com.honestmc.ems.View.Activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.honestmc.ems.Presenter.ConnectEMSPresenter;
import com.honestmc.ems.R;
import com.honestmc.ems.View.Adapter.user_name_Adapter;

import java.util.ArrayList;
import java.util.List;

public class ConnectEMSActivity extends BaseActivity{
    private ConnectEMSPresenter presenter;
    private TextView ConnectText;
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_connect_wifi);
        ConnectText = (TextView) findViewById(R.id.connecttext);
        flag = true;
        Bundle bundle2=this.getIntent().getExtras();
        String wifissid = bundle2.getString("ssid");
        String wifipw = bundle2.getString("pw");
        String wifitp = bundle2.getString("tp");
        presenter = new ConnectEMSPresenter(ConnectEMSActivity.this);
        this.setWifi_Link(wifissid,wifipw,wifitp);

        changeText(ConnectText);

    }
    public void changeText(TextView textView){
        new Thread(new Runnable(){
            @Override
            public void run() {
                while (true) {
                    try {
                        textView.setText(getApplicationContext().getString(R.string.Connecting));
                        Thread.sleep(800);

                        textView.setText(getApplicationContext().getString(R.string.Connecting) + ".");
                        Thread.sleep(800);

                        textView.setText(getApplicationContext().getString(R.string.Connecting) + "..");
                        Thread.sleep(800);
                    } catch (Exception e) {
                        Log.i("ConnectText", e.toString());
                    }

                }
            }
        }).start();
    }
    @Override
    protected void onStart() {
        super.onStart();


    }



    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onStop() {
        flag=false;
        super.onStop();
    }



    private void setWifi_Link(String wifi_ssid,String wifi_pw,String wifi_tp){
        int delay = 0;



        if(!presenter.isWifienabled()){
            delay=2000;
        }

        presenter.connectWifiQ(this,wifi_ssid,wifi_pw);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.change_launch();
                }
            }, 7500);
        }

    }
}
