package com.honestmc.ems.View.Activity;

import android.Manifest;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.honestmc.ems.Presenter.EditPresenter;
import com.honestmc.ems.R;
import com.honestmc.ems.View.Interface.EditView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditActivity extends BaseActivity implements EditView, View.OnClickListener{
    private static String TAG = "EditActivity";
    private EditPresenter presenter;
    private LinearLayout editLayout;
    private Button start_EMS_Btn,browse_Btn;
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
        setContentView(R.layout.activity_edit);

        editLayout = (LinearLayout) findViewById(R.id.launch_edit);

        start_EMS_Btn = (Button) findViewById(R.id.start_ems_btn);
        start_EMS_Btn.setOnClickListener(this);

        browse_Btn = (Button) findViewById(R.id.browse_btn);
        browse_Btn.setOnClickListener(this);

        hospital_edit = (EditText) findViewById(R.id.hospital_title);
        section_edit = (EditText) findViewById(R.id.section_title);
        job_number_edit = (EditText) findViewById(R.id.job_number_title);
        user_name_edit = (EditText) findViewById(R.id.user_name);

        setEditTextInhibitInputSpeChat(hospital_edit);
        setEditTextInhibitInputSpeChat(section_edit);
        setEditTextInhibitInputSpeChat(job_number_edit);
        setEditTextInhibitInputSpeChat(user_name_edit);

        presenter = new EditPresenter(EditActivity.this);
        presenter.setView(this);
        presenter.FTPConnect();



    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_ems_btn:
                if(!presenter.check_edit_have_null()){
                    if(presenter.writeInfoToFile(editGetString(hospital_edit),editGetString(section_edit),
                            editGetString(job_number_edit),editGetString(user_name_edit))) {
                            presenter.launchEMS();
                    }
                    else{
                        Toast.makeText(EditActivity.this,"Info Error Can't Write",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(EditActivity.this, this.getString(R.string.edit_null),Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.browse_btn:
                    presenter.openFileBrowse();
                break;
        }
    }

    private String editGetString(EditText editText){
        return editText.getText().toString();
    }

    @Override
    public boolean check_edit_have_null(){

        if(hospital_edit.getText().toString().matches("")){
            return true;
        }
        if(section_edit.getText().toString().matches("")){
            return true;
        }
        if(job_number_edit.getText().toString().matches("")){
            return true;
        }
        if(user_name_edit.getText().toString().matches("")){
            return true;
        }

        return false;
    }
    public static void setEditTextInhibitInputSpeChat(EditText editText){

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if ( !Character.isLetterOrDigit(source.charAt(i))
                            && !Character.toString(source.charAt(i)) .equals("_")
                            && !Character.toString(source.charAt(i)) .equals("-"))
                    {
                        return "";
                    }
                }
                return null;

            }

        };
        editText.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(15)});
    }


}
