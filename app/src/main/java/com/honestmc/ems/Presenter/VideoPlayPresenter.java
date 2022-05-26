package com.honestmc.ems.Presenter;

import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.app.usage.ExternalStorageStats;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.honestmc.ems.R;

import org.apache.commons.net.ftp.FTP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class VideoPlayPresenter extends BasePresenter{
    private String TAG = "VideoPlayPresenter";

    private Activity activity;
    private String curLocalVideoPath;

    private int curVideoIdx = 0;

    public VideoPlayPresenter(Activity activity, String videoPath) {
        super(activity);
        this.activity = activity;


    }

    public void setView() {

    }
    public void downloadfile(String videoPath,String videoName){
        try {

            ContentValues value = new ContentValues();
            value.put(MediaStore.Video.Media.DISPLAY_NAME,videoName);
            value.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            value.put(MediaStore.Video.Media.RELATIVE_PATH, "DCIM/");
            value.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis());
            Uri uri;
             ContentResolver contentResolver = activity.getContentResolver();
            if (SDK_INT >= Build.VERSION_CODES.Q) {
                uri = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, value);
            }else {
                uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/" + videoName));
            }


            InputStream input = new FileInputStream(videoPath);
            OutputStream outputStream =null;
            outputStream = contentResolver.openOutputStream(uri);
            byte[] data = new byte[1024];
            int count  = 0;
            while ((count = input.read(data,0,1024)) >=0) {
                outputStream.write(data, 0, count);
            }
            input.close();
            outputStream.close();
            Log.i("downloadMp4","success");
            Toast.makeText(activity, R.string.download_success,Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Log.e("downloadMp4",e.toString());
            Toast.makeText(activity, R.string.download_fail,Toast.LENGTH_LONG).show();
        }

    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.MediaColumns.DATA;
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

}
