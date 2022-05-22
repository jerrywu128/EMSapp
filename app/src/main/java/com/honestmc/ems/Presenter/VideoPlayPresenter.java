package com.honestmc.ems.Presenter;

import android.app.Activity;
import android.os.Bundle;

public class VideoPlayPresenter extends BasePresenter{
    private String TAG = "VideoPlayPresenter";

    private Activity activity;
    private String curLocalVideoPath;

    private int curVideoIdx = 0;

    public VideoPlayPresenter(Activity activity, String videoPath) {
        super(activity);
        this.activity = activity;
       // Bundle data = activity.getIntent().getExtras();
      //  curVideoIdx = data.getInt("curfilePosition");

    }

    public void setView() {

    }

}
