package com.example.joyrasmussen.hw8_group34;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by joyrasmussen on 4/7/17.
 */

public class RecycViewHolder extends RecyclerView.ViewHolder{
    TextView city;
    TextView temperature;
    TextView update;
    ImageButton favorite;
    FirebaseRecyclerAdapter<SavedCity, RecycViewHolder> fireAdapter;

    public RecycViewHolder(View itemView) {
        super(itemView);
        city = (TextView) itemView.findViewById(R.id.savedCityNameMain);
        temperature = (TextView) itemView.findViewById(R.id.tempMainSave);
        update = (TextView) itemView.findViewById(R.id.updateMainSave);
        favorite = (ImageButton) itemView.findViewById(R.id.savedButtonsavedMain);

    }
    public void setCityName(String name){
        city.setText(name);

    }
    public void setTemp(String text, String units){

        temperature.setText("Temperature: " + text + "° " + units);

    }
    public void setUpdate(String date) throws ParseException {
            update.setText("Last Update: "+ parseDate(date));


    }
    public void setFavorite(boolean isFav){
        if (isFav){
            favorite.setImageResource(R.drawable.star_gold);
        }else{
            favorite.setImageResource(R.drawable.star_gray);
        }
    }

    public static String parseDate(String date) throws ParseException {
        if(date != null) {
            java.util.Date d = new java.util.Date(Long.parseLong(date) * 1000);

            return getHowLongAgoDescription(d);
        }else{
            return "";
        }


    }
    public static String getHowLongAgoDescription(Date entered) {
        Locale locale = Locale.getDefault();
        PrettyTime p = new PrettyTime(locale);
        return p.format(entered);
    }


}
