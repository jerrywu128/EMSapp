package com.honestmc.ems.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honestmc.ems.Presenter.BasePresenter;
import com.honestmc.ems.R;

import java.util.List;

public class user_name_Adapter extends BaseAdapter {
    private Context context;
    List<String> user_names;

    public user_name_Adapter(Context context, List<String> user_names){

        this.context = context;
        this.user_names = user_names;
    }

    @Override
    public int getCount() {
        return user_names.size();
    }

    @Override
    public Object getItem(int position) {
        return user_names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_user_folder, null);
        } else {
            view = convertView;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.user_img);
        TextView mTextView = (TextView) view.findViewById(R.id.user_name);

        mTextView.setText(user_names.get(position));

        return view;
    }

}
