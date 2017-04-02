package com.example.joyrasmussen.hw8_group34;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    static final String API_Key = "zzAnkMX16zZhnHzpY2LvaJFp69R7JDM4";
    static final String Location_API = "http://dataservice.accuweather.com/locations/v1/\n" +
            "{COUNTRY_CODE}/search?apikey={YOUR_API_KEY}&q={CITY_NAME}";
    static final String CURRENT_FORCAST = "http://dataservice.accuweather.com/" +
            "currentconditions/v1/{CITY_UNIQUE_KEY}?apikey={YOUR_API_KEY}";
    static final String FIVE_DAY_FORCAST = " http://dataservice.accuweather.com/forecasts/v1/"+
            "daily/5day/{CITY_UNIQUE_KEY}?apikey={YOUR_API_KEY}";
    static final String ICON = " http://developer.accuweather.com/sites/default/files/{Image_ID}-" +
            "s.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settingsItem){
            //preference activity here

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
       return true;
    }
}
