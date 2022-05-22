package com.honestmc.ems.View.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.honestmc.ems.Presenter.VideoPlayPresenter;
import com.honestmc.ems.R;

public class VideoPlayActivity  extends BaseActivity{
    private String TAG = "VideoPlayActivity";
    private ImageButton back;
    private ImageButton delete;
    private RelativeLayout topBar;
    private TextView localVideoNameTxv;

    private boolean isShowBar =true;
    private VideoPlayPresenter presenter;
    private String videoPath;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ems_video);
        back = (ImageButton) findViewById(R.id.local_pb_back);
        delete = (ImageButton) findViewById(R.id.deleteBtn);

        topBar = (RelativeLayout) findViewById(R.id.local_pb_top_layout);
        //tlocalPbView = (MPreview) findViewById(R.id.local_pb_view);
        localVideoNameTxv = (TextView)findViewById(R.id.local_pb_video_name);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        videoPath = data.getString("cache_path");

        presenter = new VideoPlayPresenter(this,videoPath);
        presenter.setView();

        videoView = (VideoView) findViewById(R.id.locail_videoview);

        final MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);


        videoView.setMediaController(mediacontroller);
        videoView.setVideoPath(videoPath);
        videoView.requestFocus();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Toast.makeText(getApplicationContext(), "Video over", Toast.LENGTH_SHORT).show();

            }
        });
        videoView.start();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // do not display menu bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //JIRA BUG ICOM-3698 end ADD by b.jiang 20160920
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.submitAppInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        presenter.isAppBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.removeActivity();
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                Log.d("AppStart", "home");
                break;
            case KeyEvent.KEYCODE_BACK:
                Log.d("AppStart", "back");
                finish();
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }


}
