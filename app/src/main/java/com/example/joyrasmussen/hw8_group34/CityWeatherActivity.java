package com.example.joyrasmussen.hw8_group34;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CityWeatherActivity extends AppCompatActivity implements ForcastAdapater.DetailUpdater{
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference savedCityReference = mDatabase.child(MainActivity.CHILD_SAVED);
    SavedCity city;
    String mobileLink;
    String detailUrl;
    String id;
    String cityName;
    String country;
    String headline;
    String maxTemp, minTemp;

    RelativeLayout allStuff;
    LinearLayout loading;
    TextView forcastHeadline;
    TextView forcastOn, detailLink, extendedForast;
    TextView temperature;
    ImageView dayImage, nightImage;
    TextView dayCondition, nightCondition;
    RecyclerView recyclerView;
    ForcastAdapater adapter;

    PrettyTime prettyTime;

    ArrayList<OneDayForecast> forecasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        country = getIntent().getStringExtra("country").trim();
        cityName = getIntent().getStringExtra("city").trim();

        findID();

        forecasts = new ArrayList<OneDayForecast>();
        prettyTime = new PrettyTime();

        allStuff = (RelativeLayout) findViewById(R.id.contentLayout);
        allStuff.setVisibility(View.INVISIBLE);
        loading = (LinearLayout) findViewById(R.id.loadingLayout);
        dayCondition = (TextView) findViewById(R.id.dayConditions);
        nightCondition = (TextView) findViewById(R.id.nightConditions);
        dayImage = (ImageView) findViewById(R.id.dayImage);
        nightImage = (ImageView) findViewById(R.id.imageView2);
        forcastOn = (TextView) findViewById(R.id.forcastOnTextView);
        temperature = (TextView) findViewById(R.id.forcastTemperature);
        forcastHeadline = (TextView) findViewById(R.id.forcastTemperature);
        extendedForast = (TextView) findViewById(R.id.extendedForast);
        detailLink = (TextView) findViewById(R.id.moreDetailsView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewForcast);
        mobileLink = "";
        detailUrl = "";

        listeners();
        setAdapter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_weather_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveCity:
                saveThisCity();
                break;
            case R.id.setCurrent:
                break;
            case R.id.settingsCity:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void getMobileLink(String ml) {
        mobileLink = ml;

    }

    public void findID() {

        OkHttpClient client = new OkHttpClient();

        String url = MainActivity.Location_API;
        url = url.replace("{YOUR_API_KEY}", MainActivity.API_Key)
                .replace("{COUNTRY_CODE}", country)
                .replace("{CITY_NAME}", cityName);
        Log.d("findID: ", url);


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
                    Log.d("onResponse: ", arr.toString());
                    if (arr.length() == 0) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CityWeatherActivity.this, "This is not a valid city name, please try again.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    } else {
                        JSONObject obj = arr.getJSONObject(0);
                        id = obj.getString("Key");
//                        showEverything();
                        Log.d("onResponse: ", id);

                        getForecast();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }

    public void getForecast() {

        OkHttpClient client = new OkHttpClient();

        String url = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/349818?apikey=zzAnkMX16zZhnHzpY2LvaJFp69R7JDM4";
        url = url.replace("349818", id);

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

                Parse5Day parser = new Parse5Day();

                try {
                    forecasts = parser.parseInput(body);


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                           setAdapter();

                        }
                    });


                    headline = parser.getHeadline();
                    showEverything();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void showEverything(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.GONE);
                allStuff.setVisibility(View.VISIBLE);

                forcastHeadline.setText(headline);

                OneDayForecast today = forecasts.get(0);

                String forecastToday = "Forecast on: " + new Date(Long.parseLong(today.getDate()));;
                forcastOn.setText(forecastToday);
                String temperatureString = "Temperature: " + today.getTempMax() + "°/" + today.getTempMin() + "°";
                temperature.setText(temperatureString);

                dayCondition.setText(today.getDayPhrase());
                nightCondition.setText(today.getNightPhrase());



                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {


                       // Picasso.with(getApplicationContext()).load("http://i.imgur.com/DvpvklR.png").into(imageView);
                    }
                });


            }
        });


    }

    public void listeners() {
        detailLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(Uri.parse(detailUrl));
                startActivity(intent);
            }
        });
        extendedForast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(Uri.parse(mobileLink));
                startActivity(intent);
            }
        });
    }

   @Override
    public void detailUpdate(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        OneDayForecast detailFor = forecasts.get(position);
        detailUrl = detailFor.getDetailURL();

       String high = "c".equals(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("temp_unit", "")) ?
               String.format( "%.2f", 32 + ( 1.8 * detailFor.getTempMax())) : String.format( "%.2f", detailFor.getTempMax());
       String low = "c".equals(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("temp_unit", "")) ?
               String.format( "%.2f", 32 + ( 1.8 * detailFor.getTempMin())) : String.format( "%.2f", detailFor.getTempMin());
      temperature.setText("Temperature: " + high + "°/" + low + "°");



    }
    @Override
    protected void onResume() {
        super.onResume();
       if(!forecasts.isEmpty()) {
           adapter.notifyDataSetChanged();
       }
    }

    public void setAdapter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new ForcastAdapater(CityWeatherActivity.this, CityWeatherActivity.this, forecasts);
                LinearLayoutManager manager = new LinearLayoutManager(CityWeatherActivity.this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }
        });

    }

    public void saveThisCity(){
        savedCityReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                city = new SavedCity(id, cityName, country, false,  String.format( "%.2f", 32 + ( 1.8 * forecasts.get(0).getTempMax())), Float.toString(forecasts.get(0).getTempMax()));
                if(dataSnapshot.child(id).getValue() == null) {
                    savedCityReference.child(id).setValue(city);
                    Toast.makeText(CityWeatherActivity.this, cityName + ", " + country + "was added to saved cities.", Toast.LENGTH_SHORT).show();
                }else{
                    savedCityReference.child(id).setValue(city);
                    Toast.makeText(CityWeatherActivity.this, "City Updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
