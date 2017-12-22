package com.example.sanja.myfavoritegames;

import java.io.Serializable;

/**
 * Created by sanja on 1/29/2017.
 */

public class Game implements Serializable {

    String name;
    String desc;
    String genre;
    int rating;
    String year;
    String url;


    public Game(String name, String desc, String genre, int rating, String year, String url) {
        this.name = name;
        this.desc = desc;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", genre='" + genre + '\'' +
                ", rating=" + rating +
                ", year='" + year + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
