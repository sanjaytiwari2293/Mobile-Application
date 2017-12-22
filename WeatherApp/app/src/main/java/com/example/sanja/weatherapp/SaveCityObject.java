package com.example.sanja.weatherapp;

import java.util.Comparator;

/**
 * Created by sanja on 4/6/2017.
 */

public class SaveCityObject {

    String cityKey, cityName, country, uid1;
    String tempInC;
    boolean favorite;
    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static Comparator<SaveCityObject> getComparatorPrior() {
        return comparatorPrior;
    }

    public static void setComparatorPrior(Comparator<SaveCityObject> comparatorPrior) {
        SaveCityObject.comparatorPrior = comparatorPrior;
    }

    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }

    public String getUid1() {
        return uid1;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTempInC() {
        return tempInC;
    }

    public void setTempInC(String tempInC) {
        this.tempInC = tempInC;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "SaveCityObject{" +
                "cityKey='" + cityKey + '\'' +
                ", cityName='" + cityName + '\'' +
                ", country='" + country + '\'' +
                ", uid1='" + uid1 + '\'' +
                ", tempInC='" + tempInC + '\'' +
                ", favorite=" + favorite +
                '}';
    }

    public static Comparator<SaveCityObject> comparatorPrior = new Comparator<SaveCityObject>() {
        @Override
        public int compare(SaveCityObject o1, SaveCityObject o2) {
            return Boolean.compare(o2.isFavorite(),o1.isFavorite());

        }
    };

}
