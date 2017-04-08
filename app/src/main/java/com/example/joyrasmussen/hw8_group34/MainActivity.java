package com.example.joyrasmussen.hw8_group34;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements EditCityDialogFragment.NoticeDialogListener {

    static final String API_Key = "zzAnkMX16zZhnHzpY2LvaJFp69R7JDM4";
    static final String Location_API = "http://dataservice.accuweather.com/locations/v1/" +
            "{COUNTRY_CODE}/search?apikey={YOUR_API_KEY}&q={CITY_NAME}";
    static final String CURRENT_FORCAST = "http://dataservice.accuweather.com/" +
            "currentconditions/v1/{CITY_UNIQUE_KEY}?apikey={YOUR_API_KEY}";
    static final String PREFERENCES = "preferences";
    static final String FIVE_DAY_FORCAST = " http://dataservice.accuweather.com/forecasts/v1/" +
            "daily/5day/{CITY_UNIQUE_KEY}?apikey={YOUR_API_KEY}";
    static final String ICON = "https://developer.accuweather.com/sites/default/files/{Image_ID}-" +
            "s.png";

    static final String CURRENT_CITY = "currCity";
    static final String CURRENT_COUNTRY = "currentCountry";
    static final String CHILD_SAVED = "savedCity";

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseRecyclerAdapter<SavedCity, RecycViewHolder> mAdapter;
    DatabaseReference savedCityReference = mDatabase.child(CHILD_SAVED);
    RecyclerView savedRecyclerView;
    Query query = savedCityReference.orderByChild("isFav");

    static String current_city = "currCity";

    static String current_country = "currentCountry";
    static String current_city_key = "";
    String currentWeatherIcon;

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


        SavedCity charlotte = new SavedCity("349818", "Charlotte", "US", true);
        savedCityReference.child("349818").setValue(charlotte);


        savedRecyclerView = (RecyclerView) findViewById(R.id.savedCityRecycler);

        mAdapter = new FirebaseRecyclerAdapter<SavedCity, RecycViewHolder>(SavedCity.class, R.layout.saved_city_layout, RecycViewHolder.class, query) {

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
                        if (model.isFav()) {
                            model.setFav(false);
                        } else {
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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        current_city = sharedPreferences.getString("currentCity", "");
        current_country = sharedPreferences.getString("currentCountry", "");
        current_city_key = sharedPreferences.getString("currentCityKey", "");

        if (!current_city.equals("") && !current_country.equals("")) {
            populateRecyclerView();
            prefListener();


        }
        if(!current_city.equals("") && !current_country.equals("")){

            //Hide button & textview, show weather widgets
            alternateDisplay(current_city_key);
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settingsItem) {
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

    public void setCurrentCityListener(View V) {

        EditCityDialogFragment alert = new EditCityDialogFragment();
        alert.show(getFragmentManager(), "EditCityDialog");
    }

    public void searchCityListener(View v) {


    }

    @Override
    protected void onStart() {
        super.onStart();

        savedCityReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SavedCity city = dataSnapshot.getValue(SavedCity.class);



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

        sharedPreferences.edit().putString("currentCity", city).apply();
        sharedPreferences.edit().putString("currentCountry", country).apply();

        Toast.makeText(this, "Current city details saved", Toast.LENGTH_SHORT).show();

        getCityCode(city, country);

    }

    public void getCityCode(String city, String countryCode) {

        OkHttpClient client = new OkHttpClient();

        String url = Location_API;
        url = url.replace("{YOUR_API_KEY}", API_Key)
                .replace("{COUNTRY_CODE}", countryCode)
                .replace("{CITY_NAME}", city);


        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                try {
                    String body = response.body().string();

                    JSONArray arr = new JSONArray(body);
                    JSONObject obj = arr.getJSONObject(0);
                    current_city_key = obj.getString("Key");

                    sharedPreferences.edit().putString("currentCityKey", current_city_key).apply();
                    alternateDisplay(current_city_key);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void getCurrentWeatherDetails(final String id) throws IOException {
        final HashMap<String, String>[] tempAndTime = new HashMap[]{new HashMap<>()};
        String searchString = CURRENT_FORCAST.replace("{CITY_UNIQUE_KEY}", id).replace("{YOUR_API_KEY}", API_Key);
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
                    savedCityReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SavedCity savedCity = dataSnapshot.getValue(SavedCity.class);

                                savedCity.setTemperature(tempAndTime[0].get("Celcius"));

                            savedCity.setTime(tempAndTime[0].get("Time"));
                            savedCity.setTempFar(tempAndTime[0].get("Fari"));

                            savedCityReference.child(id).setValue(savedCity);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        return tempAndTime[0];


    }

    public void alternateDisplay(String key) {

        OkHttpClient client = new OkHttpClient();

        String url = CURRENT_FORCAST;
        url = url.replace("{CITY_UNIQUE_KEY}", key)
                .replace("{YOUR_API_KEY}", API_Key);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String body = response.body().string();

                CurrentWeatherParser parser = new CurrentWeatherParser();
                HashMap<String, String> values;

                try {
                    values = parser.parseInput(body);

                    currentWeatherIcon = values.get("WeatherIcon");
                    updateUI(values.get("WeatherIcon"),
                            values.get("Fari"),
                            values.get("Time"),
                            values.get("WeatherText"),
                            values.get("Celcius"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

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

    public void updateUI(String icon, String fahrenheit, final String time, final String weather, final String celcius) {

        String imageURL = ICON;

        if (currentWeatherIcon.length() == 1) {
            currentWeatherIcon = "0" + currentWeatherIcon;
        }

        final String imageUrl = imageURL.replace("{Image_ID}", currentWeatherIcon);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                currentCity.setText(current_city);
                currentTemp.setText(celcius + "°C");
                currentWeather.setText(weather);
                currentUpdatedLast.setText(time);

                Picasso.Builder builder = new Picasso.Builder(MainActivity.this);
                builder.listener(new Picasso.Listener()
                {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                    {
                        exception.printStackTrace();
                        Log.i("uri: ", uri + "");
                    }
                });
                builder.build().load(imageUrl).into(currentWeatherImage);
            }
        });
    }

    public HashMap<String, String> getTempTime(String response) throws JSONException {

        HashMap<String, String> tempAndTime = new HashMap<>();
        tempAndTime = new CurrentWeatherParser().parseInput(response);
        return tempAndTime;
    }

public void  populateRecyclerView(){

    savedRecyclerView = (RecyclerView) findViewById(R.id.savedCityRecycler);


    mAdapter = new FirebaseRecyclerAdapter<SavedCity, RecycViewHolder>( SavedCity.class, R.layout.saved_city_layout, RecycViewHolder.class, query) {

        @Override
        protected void populateViewHolder(final RecycViewHolder viewHolder, final SavedCity model, final int position) {
            final DatabaseReference savedRef = getRef(position);
            final String key = savedRef.getKey();
            Log.d("populateViewHolder: ", model.get_id() + "getKey " + key + "name " + model.getName());
            viewHolder.setCityName(model.getName() + ", " + model.getCountry());
            viewHolder.setFavorite(model.isFav());
            HashMap<String, String> populateMap = new HashMap<>();

            try {
                getCurrentWeatherDetails(model.get_id());
            } catch (IOException e) {
                e.printStackTrace();
            }


            String unit = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("temp_unit", "");
            Log.d( "populateViewHolder: ", "temp " +unit);


            if (unit.equals("c")) {
                viewHolder.setTemp(model.getTemperature(), "C");
            }else{
                viewHolder.setTemp(model.getTempFar(), "f");

            }
            try {
                viewHolder.setUpdate(model.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            viewHolder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.isFav()){
                        model.setFav(false);
                        viewHolder.setFavorite(false);
                    }else{
                        model.setFav(true);
                        viewHolder.setFavorite(true);
                    }
                    savedCityReference.child(model.get_id()).setValue(model);
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    savedCityReference.child(key).removeValue();
                    return false;
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
    savedRecyclerView.setLongClickable(true);

    savedRecyclerView.setHasFixedSize(false);
    savedRecyclerView.setLayoutManager(mLayoutManager);
    savedRecyclerView.setAdapter(mAdapter);
}
public void prefListener(){
    sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("temp_unit")){
                populateRecyclerView();

            }
        }
    });


}
}

