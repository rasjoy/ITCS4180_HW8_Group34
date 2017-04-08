package com.example.joyrasmussen.hw8_group34;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SavedCity charlotte = new SavedCity("349818", "Charlotte", "US", false);
        savedCityReference.child("349818").setValue(charlotte);
        savedCityReference.child("121212").setValue(new SavedCity("349818", "Charlotte", "US", true));



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
        savedRecyclerView.setHasFixedSize(false);
        savedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        savedRecyclerView.setAdapter(mAdapter);


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
    public void setCurrentCityListenter(View V){



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
             Log.d( "onDataChange: ", "city " + city.getName());

         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });




    }
}
