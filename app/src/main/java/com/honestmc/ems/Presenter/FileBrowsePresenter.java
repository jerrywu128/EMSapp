package com.honestmc.ems.Presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.honestmc.ems.R;
import com.honestmc.ems.Tools.EMSProgressDialog;
import com.honestmc.ems.View.Activity.FileBrowseActivity;
import com.honestmc.ems.View.Activity.LaunchActivity;
import com.honestmc.ems.View.Activity.VideoPlayActivity;
import com.honestmc.ems.View.Interface.UserBrowseView;
import com.honestmc.ems.connectedTools.FTPClientTool;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
    private String user_name;
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

    public List<String> getfiles(String s){

        List<String> n = new ArrayList<>();


        try {
            user_name = s;
            ftpClient.enterLocalPassiveMode();
            boolean change = ftpClient.changeWorkingDirectory("/home/pi/report"+s);
            FTPFile[] files = ftpClient.listFiles();
            n.add("PDF");
            for(FTPFile file : files){
                if(file.getName().contains(".pdf")) {
                    Log.i("fileis", file.toString());
                    n.add(file.getName());
                }
            }
            n.add("MP4");
            for(FTPFile file : files){
                if(file.getName().contains(".mp4")) {
                    Log.i("fileis", file.toString());
                    n.add(file.getName());
                }
            }
        }catch (Exception e){
            Log.e("fileis",e.toString());
        }

        return n;
    }

    public void browse_files(String i){
        EMSProgressDialog.showProgressDialog(activity, R.string.action_processing);
        if(i.toString().contains(".mp4")){

            Log.i("user_report&video","this is mp4!");
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                        ftpClient.changeWorkingDirectory("/home/pi/report"+user_name);
                        InputStream input = ftpClient.retrieveFileStream(i);
                        File file = new File(activity.getCacheDir(), "/" + i);
                        FileOutputStream fos = null;
                        fos = new FileOutputStream(file);

                        BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
                        byte[] data = new byte[1024];
                        int count  = 0;
                        while ((count = input.read(data,0,1024)) >=0) {
                            bout.write(data, 0, count);
                        }

                        input.close();
                        bout.flush();
                        bout.close();


                        fos.flush();
                        fos.close();


                        if(ftpClient.completePendingCommand()) {

                            if (true) {
                                Log.i("download_vdo", "success");
                                Intent mainIntent = new Intent(activity, VideoPlayActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("cache_path", activity.getCacheDir() + "/" + i);
                                bundle.putString("file_name",i);
                                mainIntent.putExtras(bundle);
                                activity.startActivity(mainIntent);
                            } else {
                                Log.e("download_vdo", "error");
                            }
                            Thread.sleep(500);
                            EMSProgressDialog.closeProgressDialog();
                        }
                    }catch (Exception e){
                        Log.e("download_vdo",e.toString());
                    }

                }
            }).start();

        }else if(i.toString().contains(".pdf")){
            Log.i("user_report&video","this is pdf!");
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                        ftpClient.changeWorkingDirectory("/home/pi/report"+user_name);
                        InputStream input = ftpClient.retrieveFileStream(i);
                        File file = new File(activity.getCacheDir(), "/" + i);
                        FileOutputStream fos = null;
                        fos = new FileOutputStream(file);

                        BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
                        byte[] data = new byte[1024];
                        int count  = 0;
                        while ((count = input.read(data,0,1024)) >=0) {
                            bout.write(data, 0, count);
                        }

                        input.close();
                        bout.flush();
                        bout.close();


                        fos.flush();
                        fos.close();


                        if(ftpClient.completePendingCommand()) {

                            if (true) {
                                Log.i("download_pdf", "success");


                                    File pdffile = new File(activity.getCacheDir(), "/" + i);
                                    if (pdffile.exists()) {

                                        Uri path = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", pdffile);

                                        Log.i("pdf",path.getPath());
                                        Log.i("cachepdf",activity.getCacheDir()+"/" + i);
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(path, "application/pdf");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                        try {
                                            activity.startActivity(intent);
                                        } catch (ActivityNotFoundException e) {

                                        }

                                    }

                            } else {
                                Log.e("download_pdf", "error");
                            }
                        }
                        Thread.sleep(500);
                        EMSProgressDialog.closeProgressDialog();
                    }catch (Exception e){
                        Log.e("download_pdf",e.toString());
                    }

                }
            }).start();
            EMSProgressDialog.closeProgressDialog();
        }else{
            Log.i("user_report&video","this is other");
            EMSProgressDialog.closeProgressDialog();
        }

    }


}
