package com.example.sanja.weatherapp;

/**
 * Created by sanja on 4/5/2017.
 */

public class FiveDaysWeatherObject {

    String date, unit, dayIconPhrase, nightIconPhrase, mobileLink;
    double minVal, maxVal;
    int dayIcon, nightIcon;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDayIconPhrase() {
        return dayIconPhrase;
    }

    public void setDayIconPhrase(String dayIconPhrase) {
        this.dayIconPhrase = dayIconPhrase;
    }

    public String getNightIconPhrase() {
        return nightIconPhrase;
    }

    public void setNightIconPhrase(String nightIconPhrase) {
        this.nightIconPhrase = nightIconPhrase;
    }

    public double getMinVal() {
        return minVal;
    }

    public void setMinVal(double minVal) {
        this.minVal = minVal;
    }

    public double getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(double maxVal) {
        this.maxVal = maxVal;
    }

    public int getDayIcon() {
        return dayIcon;
    }

    public void setDayIcon(int dayIcon) {
        this.dayIcon = dayIcon;
    }

    public int getNightIcon() {
        return nightIcon;
    }

    public void setNightIcon(int nightIcon) {
        this.nightIcon = nightIcon;
    }

    public String getMobileLink() {
        return mobileLink;
    }

    public void setMobileLink(String mobileLink) {
        this.mobileLink = mobileLink;
    }

    @Override
    public String toString() {
        return "FiveDaysWeatherObject{" +
                "date='" + date + '\'' +
                ", unit='" + unit + '\'' +
                ", dayIconPhrase='" + dayIconPhrase + '\'' +
                ", nightIconPhrase='" + nightIconPhrase + '\'' +
                ", mobileLink='" + mobileLink + '\'' +
                ", minVal=" + minVal +
                ", maxVal=" + maxVal +
                ", dayIcon=" + dayIcon +
                ", nightIcon=" + nightIcon +
                '}';
    }
}
