package com.example.sanja.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sanja on 4/5/2017.
 */

public class FetchCityKeyAsync extends AsyncTask<String, Void, String> {

    IData activity;

    public FetchCityKeyAsync(IData activity) {
        this.activity = activity;
    }
    @Override
    protected String doInBackground(String... params) {

        String url = params[0];
        Log.d("demo","FetchCityKeyAsync url "+url);

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
                Log.d("demo","response TAKEN "+s.length());
                if(s.length()!=0){
                    return s;
                }
                else{
                    return null;
                }

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
        Log.d("demo","onPostExecute JSON "+s);
        activity.setUpData(s);
    }

    static public interface IData
    {
        public void setUpData(String s);
        public Context getContext();
    }
}
