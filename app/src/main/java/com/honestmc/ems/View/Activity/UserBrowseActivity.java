package com.honestmc.ems.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import androidx.appcompat.widget.Toolbar;

import com.honestmc.ems.Presenter.UserBrowsePresenter;
import com.honestmc.ems.R;
import com.honestmc.ems.View.Adapter.user_name_Adapter;
import com.honestmc.ems.View.Interface.UserBrowseView;
import com.honestmc.ems.connectedTools.FTPClientTool;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.util.ArrayList;
import java.util.List;

public class UserBrowseActivity extends BaseActivity implements UserBrowseView {
    private String TAG = "UserBrowseActivity";

    private UserBrowsePresenter presenter;
    private  user_name_Adapter adapter;



    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_browse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        presenter = new UserBrowsePresenter(this);
        presenter.setView(this);
        presenter.FTPConnect();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        listView = (ListView) findViewById(R.id.ems_user_list_view);




        Handler handler;
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    List<String> name = new ArrayList<>();
                    name = presenter.getUsers();
                    List<String> finalName = name;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            adapter= new user_name_Adapter(UserBrowseActivity.this, finalName);
                            listView.setAdapter(adapter);


                        }
                    });


                } catch (Exception e) {
                    Log.e("fileis", e.toString());
                }

            }
        }).start();







         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                presenter.browse_user_files(adapter.getItem(position));
             }
         });

    }

    @Override
    protected void onResume() {
        super.onResume();


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

    @Override
    public void close_listView(){
        listView.setVisibility(View.GONE);
    }

}
