package com.example.joyrasmussen.hw8_group34;

import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Parse5Day {

    public String headline;
    public String mobileLink;
    CityWeatherActivity cityWeatherActivity;

    public ArrayList<OneDayForecast> parseInput(String in)throws JSONException{

        ArrayList<OneDayForecast> forecasts = new ArrayList<OneDayForecast>();

        JSONObject obj = new JSONObject(in);

        JSONObject headlineObj = obj.getJSONObject("Headline");
        headline = headlineObj.getString("Text");
        mobileLink = headlineObj.getString("MobileLink");

        JSONArray dailyForecasts = obj.getJSONArray("DailyForecasts");

        for(int i = 0; i < 5; i++){

            JSONObject forecastObj = dailyForecasts.getJSONObject(i);

            String date = forecastObj.getString("EpochDate");
            String link = forecastObj.getString("Link");
            String mobile = forecastObj.getString("MobileLink");

            JSONObject dayFore = forecastObj.getJSONObject("Day");
            String dayPhrase = dayFore.getString("IconPhrase");
            String dayPicture = dayFore.getString("Icon");

            JSONObject nightFore = forecastObj.getJSONObject("Night");
            String nightPhrase = nightFore.getString("IconPhrase");
            String nightPicture = nightFore.getString("Icon");

            JSONObject tempObj = forecastObj.getJSONObject("Temperature");
            JSONObject minObj = tempObj.getJSONObject("Minimum");
            JSONObject maxObj = tempObj.getJSONObject("Maximum");

            float minTemp = Float.parseFloat(minObj.getString("Value"));
            float maxTemp = Float.parseFloat(maxObj.getString("Value"));

            OneDayForecast forecast = new OneDayForecast(dayPicture, nightPicture, dayPhrase, nightPhrase,
                    date, link, minTemp, maxTemp, mobile);

            forecasts.add(forecast);


        }


        return forecasts;

    }
//
//    public void setCityWeatherActivity(CityWeatherActivity cityWeatherActivity) {
//        this.cityWeatherActivity = cityWeatherActivity;
//
//    }

    public String getHeadline() {
        return headline;

    }
    public String getMobileLink(){
        return  mobileLink;


    }
}
