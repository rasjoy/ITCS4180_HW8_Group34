package com.example.joyrasmussen.hw8_group34;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by joyrasmussen on 4/8/17.
 */

public class ForcastAdapater extends RecyclerView.Adapter<ForcastAdapater.ForcastHolder> {
    private Context mContext;
    private List<OneDayForast>;
    private SharedPreference preference;

    @Override
    public ForcastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ForcastHolder holder, int position) {

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
