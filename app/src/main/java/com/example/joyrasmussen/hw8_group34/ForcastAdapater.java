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

import java.util.List;

/**
 * Created by joyrasmussen on 4/8/17.
 */

public class ForcastAdapater extends RecyclerView.Adapter<ForcastAdapater.ForcastHolder> {
    private Context mContext;
    private List<OneDayForecast> data;
    private SharedPreferences mPreference;

    public ForcastAdapater(Context mContext, List<OneDayForecast> data) {
        this.mContext = mContext;
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
    }

    @Override
    public int getItemCount() {
        return 0;
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
}
