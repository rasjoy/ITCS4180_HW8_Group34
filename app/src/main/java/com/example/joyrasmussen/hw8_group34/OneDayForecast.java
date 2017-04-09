package com.example.joyrasmussen.hw8_group34;

/**
 * Created by joyrasmussen on 4/8/17.
 */


public class OneDayForecast {
    String dayPicture, nightPicture, dayPhrase, nightPhrase, date, detailURL, mobileLink;
    float tempMin, tempMax;


    public OneDayForecast(String dayPicture, String nightPicture, String dayPhrase, String nightPhrase, String date, String detailURL, float tempMin, float tempMax, String mobileLink) {
        this.dayPicture = dayPicture;
        this.nightPicture = nightPicture;
        this.dayPhrase = dayPhrase;
        this.nightPhrase = nightPhrase;
        this.date = date;
        this.detailURL = detailURL;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.mobileLink = mobileLink;
    }

    public String getDayPicture() {
        return dayPicture;
    }

    public void setDayPicture(String dayPicture) {
        this.dayPicture = dayPicture;
    }

    public String getNightPicture() {
        return nightPicture;
    }

    public void setNightPicture(String nightPicture) {
        this.nightPicture = nightPicture;
    }

    public String getDayPhrase() {
        return dayPhrase;
    }

    public void setDayPhrase(String dayPhrase) {
        this.dayPhrase = dayPhrase;
    }

    public String getNightPhrase() {
        return nightPhrase;
    }

    public void setNightPhrase(String nightPhrase) {
        this.nightPhrase = nightPhrase;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetailURL() {
        return detailURL;
    }

    public void setDetailURL(String detailURL) {
        this.detailURL = detailURL;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public String getMobileLink() {
        return mobileLink;
    }

    public void setMobileLink(String mobileLink) {
        this.mobileLink = mobileLink;
    }

    @Override
    public String toString() {
        return "OneDayForecast{" +
                "dayPicture='" + dayPicture + '\'' +
                ", nightPicture='" + nightPicture + '\'' +
                ", dayPhrase='" + dayPhrase + '\'' +
                ", nightPhrase='" + nightPhrase + '\'' +
                ", date='" + date + '\'' +
                ", detailURL='" + detailURL + '\'' +
                ", mobileLink='" + mobileLink + '\'' +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                '}';
    }
}
