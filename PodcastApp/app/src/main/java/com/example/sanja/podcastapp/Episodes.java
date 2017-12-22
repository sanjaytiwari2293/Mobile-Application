package com.example.sanja.podcastapp;

import java.io.Serializable;

/**
 * Created by sanja on 3/8/2017.
 */

public class Episodes implements Serializable {

    String title, rel_date, img_url, audio_url , description , duration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRel_date() {
        return rel_date;
    }

    public void setRel_date(String rel_date) {
        this.rel_date = rel_date;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Episodes{" +
                "title='" + title + '\'' +
                ", rel_date='" + rel_date + '\'' +
                ", img_url='" + img_url + '\'' +
                ", audio_url='" + audio_url + '\'' +
                ", description='" + description + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
