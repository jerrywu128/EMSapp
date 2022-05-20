package com.honestmc.ems.View.Activity;
import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.Manifest;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.zxing.Result;
import com.honestmc.ems.Presenter.LaunchPresenter;
import com.honestmc.ems.R;
import com.honestmc.ems.View.Interface.LaunchView;



public class LaunchActivity extends BaseActivity implements LaunchView {
    private static String TAG = "LaunchActivity";
    private TextView qrScannerText;
    private ListView camSlotListView;
    private LaunchPresenter presenter;
    private LinearLayout launchLayout,editLayout,qrLayout;
    private final String tag = "LaunchActivity";
    private String TEST = "LaunchActivityTEST";
    private CodeScannerView scannerView;
    private CodeScanner mCodeScanner;
    private String result_data;
    private Button go_ScannerBtn;
    private EditText hospital_edit,section_edit,job_number_edit,user_name_edit;
    public static final int PERMISSION_REQUEST_CODE =100;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        launchLayout = (LinearLayout) findViewById(R.id.launch_view);
        qrLayout = (LinearLayout) findViewById(R.id.launch_qr);


        scannerView = (CodeScannerView) findViewById(R.id.qrcode_scanner);
        qrScannerText = (TextView) findViewById(R.id.qrscanner_text);




        presenter = new LaunchPresenter(LaunchActivity.this);
        presenter.setView(this);

        result_data = new String("");
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(result.getText().contains("WIFI:")&&result.getText().contains("P:")&&result.getText().contains("S:")) {
                            if (SDK_INT >= Build.VERSION_CODES.Q) {
                                if(!presenter.isWifienabled()) {
                                    Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                                    someActivityResultLauncher.launch(panelIntent);

                                    result_data = result.getText();

                                }
                                else{
                                    presenter.setWifiwithQrcode(LaunchActivity.this, result.getText());
                                }
                            }else {
                                presenter.setWifiwithQrcode(LaunchActivity.this, result.getText());
                            }
                        }else{
                            Toast.makeText(LaunchActivity.this, R.string.qrcode_tp_error, Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mCodeScanner.startPreview();
                                }
                            }, 2000);

                        }

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
        else
            mCodeScanner.startPreview();

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                    }
                }

            });






}
