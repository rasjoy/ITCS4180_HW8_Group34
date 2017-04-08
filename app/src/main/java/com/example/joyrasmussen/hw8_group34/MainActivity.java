package com.example.joyrasmussen.hw8_group34;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements EditCityDialogFragment.NoticeDialogListener {
    static final String API_Key = "zzAnkMX16zZhnHzpY2LvaJFp69R7JDM4";
    static final String Location_API = "http://dataservice.accuweather.com/locations/v1/" +
            "{COUNTRY_CODE}/search?apikey={YOUR_API_KEY}&q={CITY_NAME}";
    static final String CURRENT_FORCAST = "http://dataservice.accuweather.com/" +
            "currentconditions/v1/{CITY_UNIQUE_KEY}?apikey={YOUR_API_KEY}";
    static final String FIVE_DAY_FORCAST = " http://dataservice.accuweather.com/forecasts/v1/"+
            "daily/5day/{CITY_UNIQUE_KEY}?apikey={YOUR_API_KEY}";
    static final String ICON = " http://developer.accuweather.com/sites/default/files/{Image_ID}-" +
            "s.png";

    static String current_city = "currCity";
    static String current_country = "currentCountry";
    static String current_city_key = "";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if current city/country is set in shared preferences

        sharedPreferences = this.getSharedPreferences("com.example.joyrasmussen.hw8_group34", Context.MODE_PRIVATE);
        current_city = sharedPreferences.getString("currentCity", "");
        current_country = sharedPreferences.getString("currentCountry", "");
        current_city_key = sharedPreferences.getString("currentCityKey", "");

        if(!current_city.equals("") && !current_country.equals("")){

            //Hide button & textview
            Button setCurrentCityButton = (Button) findViewById(R.id.setCurrentButton);
            TextView citynotSetTextView = (TextView) findViewById(R.id.noCurrent);
            citynotSetTextView.setVisibility(View.GONE);
            setCurrentCityButton.setVisibility(View.GONE);

            //display weather widgets
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settingsItem){
            //preference activity here
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void setCurrentCityListener(View V){

        EditCityDialogFragment alert = new EditCityDialogFragment();
        alert.show(getFragmentManager(), "EditCityDialog");
    }

    public void searchCityListener(View v){


    }

    //This method gets the city/country when the user sets the current one
    public void onDialogPositiveClick(DialogFragment dialog, String city, String country) {

        current_city = city;
        current_country = country;

        //get city key
        //store country, city, key in shared prefs:

        sharedPreferences.edit().putString("currentCity", city).apply();
        sharedPreferences.edit().putString("currentCountry", country).apply();


        Toast.makeText(this, "Current city details saved", Toast.LENGTH_SHORT).show();

    }
}
