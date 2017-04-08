package com.example.joyrasmussen.hw8_group34;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements EditCityDialogFragment.NoticeDialogListener {

    static final String API_Key = "zzAnkMX16zZhnHzpY2LvaJFp69R7JDM4";
    static final String Location_API = "http://dataservice.accuweather.com/locations/v1/" +
            "{COUNTRY_CODE}/search?apikey={YOUR_API_KEY}&q={CITY_NAME}";
    static final String CURRENT_FORCAST = "http://dataservice.accuweather.com/" +
            "currentconditions/v1/{CITY_UNIQUE_KEY}?apikey={YOUR_API_KEY}";
    static final String   PREFERENCES = "preferences";
    static final String FIVE_DAY_FORCAST = " http://dataservice.accuweather.com/forecasts/v1/"+
            "daily/5day/{CITY_UNIQUE_KEY}?apikey={YOUR_API_KEY}";
    static final String ICON = " http://developer.accuweather.com/sites/default/files/{Image_ID}-" +
            "s.png";

    static final String CURRENT_CITY = "currCity";
    static final String CURRENT_COUNTRY = "currentCountry";
    static final String CHILD_SAVED = "savedCity";

    DatabaseReference mDatabase =  FirebaseDatabase.getInstance().getReference();
    FirebaseRecyclerAdapter<SavedCity, RecycViewHolder> mAdapter;
    DatabaseReference savedCityReference =mDatabase.child(CHILD_SAVED);
    RecyclerView savedRecyclerView;
    Query query = savedCityReference.orderByChild("isFav");


    static String current_city = "currCity";
    static String current_country = "currentCountry";
    static String current_city_key = "";

    TextView currentCity;
    TextView currentWeather;
    TextView currentTemp;
    TextView currentTempTV;
    TextView currentUpdatedTV;
    TextView currentUpdatedLast;
    ImageView currentWeatherImage;

    SharedPreferences sharedPreferences;
    LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentCity = (TextView) findViewById(R.id.currentCityName);
        currentWeather = (TextView) findViewById(R.id.currentWeather);
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        currentTempTV = (TextView) findViewById(R.id.currentTempTV);
        currentUpdatedTV = (TextView) findViewById(R.id.currentUpdatedTV);
        currentUpdatedLast = (TextView) findViewById(R.id.currentUpdatedLast);
        currentWeatherImage = (ImageView) findViewById(R.id.currentWeatherImage);


        SavedCity charlotte = new SavedCity("349818", "Charlotte", "US", false);
        savedCityReference.child("349818").setValue(charlotte);


        savedRecyclerView = (RecyclerView) findViewById(R.id.savedCityRecycler);

        mAdapter = new FirebaseRecyclerAdapter<SavedCity, RecycViewHolder>( SavedCity.class, R.layout.saved_city_layout, RecycViewHolder.class, query) {

            @Override
            protected void populateViewHolder(RecycViewHolder viewHolder, final SavedCity model, final int position) {

                DatabaseReference savedRef = getRef(position);
                String key = savedRef.getKey();
                Log.d("populateViewHolder: ", model.get_id() + "getKey " + key + "name " + model.getName());
                viewHolder.setCityName(model.getName() + ", " + model.getCountry());
                viewHolder.setFavorite(model.isFav());

                String unit = getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE).getString("temp_unit", "c");
                viewHolder.setTemp("temp", unit);
                try {
                    viewHolder.setUpdate("1491606900");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                viewHolder.favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.isFav()){
                            model.setFav(false);
                    }else{
                        model.setFav(true);
                    }
                        savedCityReference.child(model.get_id()).setValue(model);
                    }
                });
            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();

            }
        };
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        savedRecyclerView.setHasFixedSize(false);
        savedRecyclerView.setLayoutManager(mLayoutManager);
        savedRecyclerView.setAdapter(mAdapter);


        //Check if current city/country is set in shared preferences

        sharedPreferences = this.getSharedPreferences("com.example.joyrasmussen.hw8_group34", Context.MODE_PRIVATE);
        current_city = sharedPreferences.getString("currentCity", "");
        current_country = sharedPreferences.getString("currentCountry", "");
        current_city_key = sharedPreferences.getString("currentCityKey", "");

        if(!current_city.equals("") && !current_country.equals("")){

            //Hide button & textview, show weather widgets
            alternateDisplay();
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
    
    @Override
    protected void onStart() {
        super.onStart();

        savedCityReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SavedCity city = dataSnapshot.getValue(SavedCity.class);
                Log.d("onDataChange: ", "city " + city.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    public HashMap<String, String> getCurrentWeatherDetails(String id) throws IOException {
        final HashMap<String, String>[] tempAndTime = new HashMap[]{new HashMap<>()};
        String searchString  = CURRENT_FORCAST.replace("{CITY_UNIQUE_KEY}", id).replace("{YOUR_API_KEY}", API_Key);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(searchString)
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "cbefb17a-2d38-8f5a-1e97-21ea76fd4535")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    tempAndTime[0] = getTempTime(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return tempAndTime[0];
        }

    public void alternateDisplay(){

        Button setCurrentCityButton = (Button) findViewById(R.id.setCurrentButton);
        TextView citynotSetTextView = (TextView) findViewById(R.id.noCurrent);
        citynotSetTextView.setVisibility(View.GONE);
        setCurrentCityButton.setVisibility(View.GONE);

        currentCity.setVisibility(View.VISIBLE);
        currentTemp.setVisibility(View.VISIBLE);
        currentTempTV.setVisibility(View.VISIBLE);
        currentUpdatedLast.setVisibility(View.VISIBLE);
        currentUpdatedTV.setVisibility(View.VISIBLE);
        currentWeather.setVisibility(View.VISIBLE);
        currentWeatherImage.setVisibility(View.VISIBLE);



    }

    public HashMap<String, String> getTempTime(String response) throws JSONException {

        HashMap<String, String> tempAndTime = new HashMap<>();
        tempAndTime = new CurrentWeatherParser().parseInput(response);
        return tempAndTime;
    }


}

