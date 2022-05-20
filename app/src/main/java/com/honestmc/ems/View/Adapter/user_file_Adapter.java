package com.honestmc.ems.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honestmc.ems.R;

import java.util.List;

public class user_file_Adapter extends BaseAdapter {
    private Context context;
    List<String> file_names;

    public user_file_Adapter(Context context, List<String> file_names){

        this.context = context;
        this.file_names = file_names;
    }

    @Override
    public int getCount() {
        return file_names.size();
    }

    @Override
    public Object getItem(int position) {
        return file_names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_file_folder, null);
        } else {
            view = convertView;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.file_img);
        TextView mTextView = (TextView) view.findViewById(R.id.file_name);

        mTextView.setText(file_names.get(position));

        return view;
    }

}
