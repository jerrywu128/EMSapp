package com.honestmc.ems.View.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honestmc.ems.R;

import java.util.List;
import java.util.Locale;

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
    public String getItem(int position) {
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
        if(file_names.get(position).contains("PDF")||file_names.get(position).contains("MP4")){
            mTextView.setBackgroundColor(context.getResources().getColor(R.color.half_transparent_grey));
            mTextView.setTextColor(context.getResources().getColor(R.color.white));
            imageView.setVisibility(View.GONE);
            mTextView.setGravity(Gravity.CENTER);
        }else{
            mTextView.setBackgroundColor(context.getResources().getColor(R.color.white));
            mTextView.setTextColor(context.getResources().getColor(R.color.black));
            imageView.setVisibility(View.VISIBLE);
            mTextView.setGravity(Gravity.CENTER_VERTICAL);
        }
        if(file_names.get(position).toLowerCase(Locale.ROOT).contains("pdf")){
            imageView.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24);
        }else{
            imageView.setImageResource(R.drawable.ic_baseline_ondemand_video_24);
        }
        mTextView.setText(file_names.get(position));


        return view;
    }

}
