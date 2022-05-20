package com.honestmc.ems.Presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.honestmc.ems.View.Activity.LaunchActivity;
import com.honestmc.ems.View.Interface.UserBrowseView;
import com.honestmc.ems.connectedTools.FTPClientTool;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileBrowsePresenter extends BasePresenter{
    private static final String TAG = "FileBrowsePresenter";
    private FTPClientTool ftpClientTool;
    private FTPClient ftpClient;
    private Activity activity;
    private FragmentManager fragmentManager;
    private int offset = 0;
    UserBrowseView userBrowseView;


    private Executor executor;
    private boolean curSelectAll = false;
    private boolean isBitmapRecycled = false;
    public FileBrowsePresenter(Activity activity) {
        super(activity);
        this.activity = activity;
        this.fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        this.executor = Executors.newSingleThreadExecutor();

    }

    public void setView() {

    }

    public void browse_file_files(int position){
        Intent mainIntent = new Intent(activity, LaunchActivity.class);
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

    public List<String> getUsers(String s){

        List<String> n = new ArrayList<>();


        try {

            ftpClient.enterLocalPassiveMode();
            boolean change = ftpClient.changeWorkingDirectory("/home/pi/report"+s);
            FTPFile[] files = ftpClient.listFiles();

            for(FTPFile file : files){
                Log.i("fileis",file.toString());

                    n.add(file.getName());

            }
        }catch (Exception e){
            Log.e("fileis",e.toString());
        }




        return n;
    }

}
