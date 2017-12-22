package com.example.sanja.tripbookfinal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sanja on 5/24/2017.
 */

public class Deals implements Serializable {

    private String Duration, Place;
    private HashMap<String,Double> Location = new HashMap<>();
    private Long Cost;

    public Long getcost() {
        return Cost;
    }

    public void setcost(Long Cost) {
        this.Cost = Cost;
    }

    public String getduration() {
        return Duration;
    }

    public void setduration(String Duration) {
        this.Duration = Duration;
    }

    public String getplace() {
        return Place;
    }

    public void setplace(String Place) {
        this.Place = Place;
    }


    public HashMap<String, Double> getlocation() {
        return Location;
    }

    public void setlocation(HashMap<String, Double> location) {
        Location = location;
    }

    @Override
    public String toString() {
        return "Deals{" +
                "Cost='" + Cost + '\'' +
                ", Duration='" + Duration + '\'' +
                ", Place='" + Place + '\'' +
                ", Location=" + Location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deals)) return false;

        Deals deals = (Deals) o;

        if (!Duration.equals(deals.Duration)) return false;
        if (!Place.equals(deals.Place)) return false;
        if (!Location.equals(deals.Location)) return false;
        return Cost.equals(deals.Cost);

    }

    @Override
    public int hashCode() {
        int result = Duration.hashCode();
        result = 31 * result + Place.hashCode();
        result = 31 * result + Location.hashCode();
        result = 31 * result + Cost.hashCode();
        return result;
    }
}
