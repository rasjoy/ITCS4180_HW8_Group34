package com.example.joyrasmussen.hw8_group34;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;

/**
 * Created by joyrasmussen on 4/7/17.
 */

public class CurrentCityPreference extends DialogPreference {

    EditText city;
    EditText country;


    public CurrentCityPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(true);
        setDialogLayoutResource(R.layout.current_city_dialog);

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        SharedPreferences pref = getSharedPreferences();

        city = (EditText) view.findViewById(R.id.cityTextDialog);
        country = (EditText) view.findViewById(R.id.countryEditDialog);

        if(pref.getString(getKey()+ MainActivity.CURRENT_CITY, null) != null){
            city.setText(pref.getString(getKey()+ MainActivity.CURRENT_CITY, null));
            country.setText(getKey()+ MainActivity.CURRENT_COUNTRY, null);
            setPositiveButtonText(R.string.update);
            setNegativeButtonText(R.string.cancel);
        }
    }

}
