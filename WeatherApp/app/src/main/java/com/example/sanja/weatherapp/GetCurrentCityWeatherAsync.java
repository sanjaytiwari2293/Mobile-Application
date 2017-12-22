package com.example.sanja.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sanja on 4/6/2017.
 */

public class GetCurrentCityWeatherAsync extends AsyncTask<String, Void, String> {

    ICityData activity;

    public GetCurrentCityWeatherAsync(ICityData activity) {
        this.activity = activity;
    }

    String url;
    @Override
    protected String doInBackground(String... params) {

        url = params[0];

        OkHttpClient client = new OkHttpClient();
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
        String tempValueInCel =null;
        try {
            JSONArray jsonArray = new JSONArray(s);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            /*locObservDateTime = jsonObject.getString("LocalObservationDateTime");*/
            //String locObDtTimeNew = locObservDateTime.substring(0,10);
           /* weatherTxt = jsonObject.getString("WeatherText");
            weatherIcon = jsonObject.getString("WeatherIcon");*/

            JSONObject  jsonObject1 = jsonObject.getJSONObject("Temperature");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("Metric");
            tempValueInCel = jsonObject2.getString("Value");
            /*unit = jsonObject2.getString("Unit");*/
            Log.d("demo","temp val 1212 "+tempValueInCel);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        activity.setUpCityData(tempValueInCel);
    }

    public static interface ICityData{

        public void setUpCityData(String s);
        public Context getContext();
    }
}
