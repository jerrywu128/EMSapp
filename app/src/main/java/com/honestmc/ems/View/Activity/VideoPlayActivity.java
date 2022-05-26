package com.honestmc.ems.View.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private ImageButton download;
    private RelativeLayout topBar;
    private TextView VideoNameTxv;


    private boolean isShowBar =true;
    private VideoPlayPresenter presenter;
    private String videoPath,videoName;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ems_video);
        back = (ImageButton) findViewById(R.id.local_pb_back);
        download = (ImageButton) findViewById(R.id.downloadBtn);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        videoPath = data.getString("cache_path");
        videoName = data.getString("file_name");
        topBar = (RelativeLayout) findViewById(R.id.local_pb_top_layout);

        VideoNameTxv = (TextView)findViewById(R.id.video_name);
        VideoNameTxv.setText(videoName);
        presenter = new VideoPlayPresenter(this,videoPath);
        presenter.setView();

        videoView = (VideoView) findViewById(R.id.locail_videoview);

        final MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);


        videoView.setMediaController(mediacontroller);
        videoView.setVideoPath(videoPath);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
        });

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


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("video", videoPath);
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
                builder.setTitle(R.string.downloadMp4);
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.Sure, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.downloadfile(videoPath,videoName);
                    }
                });
                builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                dialog = builder.create();
                dialog.show();

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
