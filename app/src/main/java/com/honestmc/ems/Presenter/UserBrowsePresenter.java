package com.honestmc.ems.Presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.honestmc.ems.R;
import com.honestmc.ems.Tools.EMSProgressDialog;
import com.honestmc.ems.View.Activity.FileBrowseActivity;
import com.honestmc.ems.View.Activity.LaunchActivity;
import com.honestmc.ems.View.Interface.UserBrowseView;
import com.honestmc.ems.connectedTools.FTPClientTool;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserBrowsePresenter extends BasePresenter{
    private static final String TAG = "UserBrowsePresenter";
    private FTPClientTool ftpClientTool;
    private FTPClient ftpClient;
    private Activity activity;
    private FragmentManager fragmentManager;
    private int offset = 0;
    UserBrowseView userBrowseView;


    private Executor executor;
    private boolean curSelectAll = false;
    private boolean isBitmapRecycled = false;
    public UserBrowsePresenter(Activity activity) {
        super(activity);
        this.activity = activity;
        this.fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        this.executor = Executors.newSingleThreadExecutor();

    }

    public void setView(UserBrowseView userBrowseView) {
        this.userBrowseView = userBrowseView;

    }

    public void browse_user_files(Object i){
        Intent mainIntent = new Intent(activity, FileBrowseActivity.class);
        Log.i("user_name",i.toString());
        Bundle bundle = new Bundle();
        bundle.putString("user_name",i.toString());
        mainIntent.putExtras(bundle);
        activity.startActivity(mainIntent);
    }

    public void FTPConnect(){

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    ftpClientTool = new FTPClientTool(activity);
                    //ftpClientTool.iniFTPClient();
                    ftpClient = ftpClientTool.getFTPClient();
                    Log.i("ftp","connect success!");

                }catch (Exception e){
                    Log.e("ftp","connect error!");
                    Looper.prepare();
                    Toast.makeText(activity,e.toString(),Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });

        thread.start();


    }

    public List<String> getUsers(){

        List<String> n = new ArrayList<>();


                try {

                    ftpClient.enterLocalPassiveMode();
                    boolean change = ftpClient.changeWorkingDirectory("/home/pi/report");
                    FTPFile[] files = ftpClient.listFiles();

                    for(FTPFile file : files){
                        Log.i("fileis",file.toString());
                        if(file.isDirectory()) {
                            n.add(file.getName());
                        }
                    }
                }catch (Exception e){
                    Log.e("fileis",e.toString());
                }




        return n;
    }

}
