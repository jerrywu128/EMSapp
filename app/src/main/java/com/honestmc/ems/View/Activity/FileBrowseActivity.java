package com.honestmc.ems.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.honestmc.ems.Presenter.FileBrowsePresenter;
import com.honestmc.ems.R;
import com.honestmc.ems.View.Adapter.user_file_Adapter;
import com.honestmc.ems.View.Adapter.user_name_Adapter;

import java.util.ArrayList;
import java.util.List;

public class FileBrowseActivity extends BaseActivity{
    private String TAG = "FileBrowseActivity";

    private FileBrowsePresenter presenter;
    private  user_name_Adapter adapter;


    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ftoolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String user_name = intent.getStringExtra("user_name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        listView = (ListView) findViewById(R.id.ems_file_list_view);


        presenter = new FileBrowsePresenter(this);
        presenter.setView();
        presenter.FTPConnect();

        Handler handler;
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    List<String> name = new ArrayList<>();
                    name = presenter.getUsers("/"+user_name);
                    List<String> finalName = name;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            adapter= new user_name_Adapter(FileBrowseActivity.this, finalName);
                            listView.setAdapter(adapter);


                        }
                    });


                } catch (Exception e) {
                    Log.e("fileis", e.toString());
                }

            }
        }).start();
        List<String> name = new ArrayList<>();
        user_file_Adapter adapter= new user_file_Adapter(this,name);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(FileBrowseActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}