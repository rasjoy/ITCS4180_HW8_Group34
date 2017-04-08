package com.example.joyrasmussen.hw8_group34;

import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by joyrasmussen on 4/8/17.
 */

public class CurrentWeatherParser {



    public HashMap<String, String> parseInput(String in) throws JSONException {
        HashMap<String, String> map = new HashMap<>();
        JSONArray input = new JSONArray(in);

        JSONObject stuff = input.getJSONObject(0);
        JSONObject temp = stuff.getJSONObject("Temperature");
        map.put("Celcius", Integer.toString(temp.getJSONObject("Metric").getInt("Value")));
        map.put("Fari", Integer.toString(temp.getJSONObject("Imperial").getInt("Value")));
        map.put("Time", Long.toString(stuff.getLong("EpochTime")));
        map.put("WeatherIcon", Integer.toString(stuff.getInt("WeatherIcon")));
        map.put("WeatherText", stuff.getString("WeatherText"));
        return  map;

    }

}
