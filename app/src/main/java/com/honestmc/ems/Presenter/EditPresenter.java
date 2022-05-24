package com.honestmc.ems.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.honestmc.ems.View.Activity.EditActivity;
import com.honestmc.ems.View.Activity.UserBrowseActivity;
import com.honestmc.ems.View.Interface.EditView;
import com.honestmc.ems.connectedTools.FTPClientTool;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditPresenter extends BasePresenter{
    private EditView editView;
    private Activity activity;
    private FTPClientTool ftpClientTool;
    private FTPClient ftpClient;
    public EditPresenter(Activity activity) {
        super(activity);
        this.activity = activity;
    }
    public void setView(EditView editView) {
        this.editView = editView;
        initCfg();
    }

    public boolean check_edit_have_null(){
        return editView.check_edit_have_null();
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
        userInfo.append("Job_Number:"+job_number+"\n");
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

    public void launchEMS(){

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                try{
                    FileInputStream in = new FileInputStream(new File(activity.getFilesDir().toString()+"/USER_INFO"));
                    String encoding = System.getProperty("file.encoding");
                    boolean change = ftpClient.changeWorkingDirectory("/home/pi/report");
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    if (change) {
                        boolean result = false;
                        result = ftpClient.storeFile(new String("USER_INFO".getBytes(encoding), "iso-8859-1"), in);
                        if (result) {
                            Toast.makeText(activity,"上傳成功!",Toast.LENGTH_LONG).show();
                            Log.i("ftp-file","上傳成功!");
                        }else {
                            Log.e("ftp-file","上傳失敗!");
                        }
                    }
                    int x = ftpClientTool.check_Info();

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



                }catch (Exception e){
                    Log.e("ftp-file","上傳error!");
                }
            }
        });

        thread.start();


    }

    public void FTPConnect(){

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    ftpClientTool = new FTPClientTool(activity);
                    ftpClientTool.iniFTPClient();
                    ftpClient = ftpClientTool.getFTPClient();
                    Log.i("ftp","connect success!");
                    Looper.prepare();
                    Toast.makeText(activity,"ftp connect success",Toast.LENGTH_LONG).show();
                    Looper.loop();
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

    public void openFileBrowse(){
        Intent mainIntent = new Intent(activity, UserBrowseActivity.class);
        activity.startActivity(mainIntent);
    }

}
