package com.example.sanja.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sanja on 4/5/2017.
 */

public class FetchFiveDaysWeatherAsync extends AsyncTask<String, Void, String> {

    IDataFive activity;

    public FetchFiveDaysWeatherAsync(IDataFive activity) {
        this.activity = activity;
    }
    @Override
    protected String doInBackground(String... params) {

        String url = params[0];
        Log.d("demo","FetchFiveDaysWeatherAsync  url "+url);

        OkHttpClient client = new OkHttpClient();
            /*RequestBody formBody = new FormBody.Builder()
                    *//*.add("channel_id", "" + mData.get(position).getId())*//*
                    .build();*/

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String s = response.body().string();
                Log.d("demo","response TAKEN "+s);
                return s;
            }
            else{
                Log.d("demo","no response");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("demo","onPostExecute s "+s);
        activity.setUpFiveDaysData(s);
    }

    static public interface IDataFive
    {
        public void setUpFiveDaysData(String s);
        public Context getContext();
    }
}
