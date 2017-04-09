package com.example.joyrasmussen.hw8_group34;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;

/**
 * Created by joyrasmussen on 4/7/17.
 */

public class CurrentCityPreference extends DialogPreference implements DialogInterface.OnClickListener{

    EditText city;
    EditText country;
    SharedPreferences pref;

    public CurrentCityPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(true);
        setDialogLayoutResource(R.layout.current_city_dialog);

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
         pref = getSharedPreferences();

        city = (EditText) view.findViewById(R.id.cityTextDialog);
        country = (EditText) view.findViewById(R.id.countryEditDialog);


        if(pref.getString("currentCity", null) != null){

            city.setText(pref.getString("currentCity", null));
            country.setText(pref.getString("currentCountry", null));
            setPositiveButtonText(R.string.update);
            setNegativeButtonText(R.string.cancel);
        }


    }

    @Override
    public void onClick(DialogInterface dialog, int which){
        if(which == DialogInterface.BUTTON_POSITIVE) {
            // do your stuff to handle positive button

            String newCity = city.getText().toString();
            String newCountry = country.getText().toString();

            pref.edit().putString("currentCity", newCity).apply();
            pref.edit().putString("currentCountry", newCountry).apply();


        }else if(which == DialogInterface.BUTTON_NEGATIVE){
            // do your stuff to handle negative button

        }
    }



}
