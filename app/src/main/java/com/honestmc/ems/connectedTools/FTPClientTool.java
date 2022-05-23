package com.honestmc.ems.connectedTools;

import android.app.Activity;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.util.ArrayList;
import java.util.List;

public class FTPClientTool implements Parcelable {
    private String emsUser_INFO = "USER_INFO";
    //private String ip = "192.168.31.1";
    private String ip = "61.70.133.35";
    private String userName = "Honestmc";
    private String password = "Honestmc12345";
    private String work_path = "/home/pi/report";
    private Activity activity;
    public static FTPClient ftpClient;

    public FTPClientTool(Activity activity){
        this.activity = activity;
    }

    protected FTPClientTool(Parcel in) {
        emsUser_INFO = in.readString();
        ip = in.readString();
        userName = in.readString();
        password = in.readString();
        work_path = in.readString();
    }

    public static final Creator<FTPClientTool> CREATOR = new Creator<FTPClientTool>() {
        @Override
        public FTPClientTool createFromParcel(Parcel in) {
            return new FTPClientTool(in);
        }

        @Override
        public FTPClientTool[] newArray(int size) {
            return new FTPClientTool[size];
        }
    };

    public void iniFTPClient() throws Exception{
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(ip,21);
        ftpClient.enterLocalPassiveMode();
        ftpClient.login(userName, password);
        this.ftpClient = ftpClient;

    }
    public FTPClient getFTPClient(){
        return this.ftpClient;
    }

    public int check_Info() throws Exception{
        int empty_count = 0;
        Thread.sleep(5);
        FTPFile[] files = ftpClient.listFiles();

        for(FTPFile file : files){
            Log.i("fileis",file.toString());
            empty_count++;


            if(file.getName().equals("INFO_OK")) {
                if (ftpClient.deleteFile(work_path + "/" + file.getName())) {
                    return 1;
                }else{
                    Log.i("fileis",file.getName()+"delete failed");
                    return 2;
                }
            }

            if(file.getName().equals("INFO_ERROR")){
                Log.i("fileis",file.getName()+"info_error");
                if (ftpClient.deleteFile(work_path + "/" + file.getName())) {
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




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(emsUser_INFO);
        parcel.writeString(ip);
        parcel.writeString(userName);
        parcel.writeString(password);
        parcel.writeString(work_path);
    }
}
