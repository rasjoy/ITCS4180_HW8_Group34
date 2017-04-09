package com.example.joyrasmussen.hw8_group34;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;

import java.util.List;

/**
 * Created by joyrasmussen on 4/8/17.
 */

public class ForcastAdapater extends RecyclerView.Adapter<ForcastAdapater.ForcastHolder> implements View.OnClickListener{
    private Context mContext;
    private List<OneDayForecast> data;
    private SharedPreferences mPreference;
    private DetailUpdater detail;

    public ForcastAdapater(Context mContext, DetailUpdater detailUpdater,  List<OneDayForecast> data) {
        this.mContext = mContext;
        this.detail = detailUpdater;
        this.data = data;
        this.mPreference = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public ForcastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.five_days_layout, parent, false);
        return new ForcastHolder(view);
    }

    @Override
    public void onBindViewHolder(ForcastHolder holder, int position) {
        OneDayForecast forecast = data.get(position);
        String imageID = forecast.getDayPicture();
        if(!imageID.isEmpty() || imageID != null) {
            imageID = imageID.length() == 1 ? "0" + imageID : imageID;
            Picasso.with(mContext).load(MainActivity.ICON.replace("{Image_ID}", imageID)).into(holder.image);
        }
        //2017-04-08T07:00:00-04:0
        SimpleDateFormat formater =new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat newFormater = new SimpleDateFormat("dd, mmm'yy");


        String date = null;
        try {
            date = newFormater.format(formater.parse(forecast.getDate().substring(0,9)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.date.setText(date);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        detail.detailUpdate(v);
    }

    static class ForcastHolder extends RecycViewHolder{
        TextView date;
        ImageView image;

        public ForcastHolder(View itemView) {

            super(itemView);
            date = (TextView) itemView.findViewById(R.id.dateRecyclerView) ;
            image = (ImageView) itemView.findViewById(R.id.imageRecycler);

        }
    }
    interface DetailUpdater{
        void detailUpdate(View v);
    }
}
