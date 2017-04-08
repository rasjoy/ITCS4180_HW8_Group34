package com.example.joyrasmussen.hw8_group34;

/**
 * Created by joyrasmussen on 4/7/17.
 */

public class SavedCity {
    String _id,  name,country;
    boolean isFav;
    String temperature;
    String tempFar;

    public SavedCity(String _id, String name, String country, boolean isFav, String temperature, String tempFar, String time) {
        this._id = _id;
        this.name = name;
        this.country = country;
        this.isFav = isFav;
        this.temperature = temperature;
        this.tempFar = tempFar;
        this.time = time;
    }

    public String getTempFar() {

        return tempFar;
    }

    public void setTempFar(String tempFar) {
        this.tempFar = tempFar;
    }

    String time;

    public SavedCity(String _id, String name, String country, boolean isFav, String temperature, String time) {
        this._id = _id;
        this.name = name;
        this.country = country;
        this.isFav = isFav;
        this.temperature = temperature;
        this.time = time;
        tempFar = "0";
    }

    public SavedCity(String _id, String name, String country, boolean isFav) {
        this._id = _id;
        this.name = name;
        this.country = country;
        this.isFav = isFav;
        temperature = "0";
        tempFar = "0";
        time = "491672900";
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public SavedCity(String _id, String country, boolean isFav, String name ) {
        this._id = _id;
        this.name = name;
        this.country = country;
        this.isFav = isFav;
        temperature = "0";
        time = "491672900";
        tempFar = "0";

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public SavedCity() {
    }

    public String displayName(){
        return name + ", " + country;


    }
}
