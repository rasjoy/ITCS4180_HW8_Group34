package com.example.joyrasmussen.hw8_group34;

/**
 * Created by joyrasmussen on 4/7/17.
 */

public class SavedCity {
    String _id,  name,country;
    boolean isFav;

    public SavedCity(String _id, String name, String country, boolean isFav) {
        this._id = _id;
        this.name = name;
        this.country = country;
        this.isFav = isFav;
    }

    public SavedCity(String _id, String country, boolean isFav,  String name ) {
        this._id = _id;
        this.name = name;
        this.country = country;
        this.isFav = isFav;
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
