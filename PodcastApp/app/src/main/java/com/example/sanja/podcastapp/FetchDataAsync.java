package com.example.sanja.podcastapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by sanja on 3/8/2017.
 */

public class FetchDataAsync extends AsyncTask<String, Void, ArrayList<Episodes>> {

    IData activity;

    public FetchDataAsync(IData activity) {
        this.activity = activity;
    }

    ArrayList<Episodes> episodesArrayList = new ArrayList<>();
    @Override
    protected ArrayList<Episodes> doInBackground(String... params) {
        String url = params[0];
        RequestParams requestParams = new RequestParams("GET", url);

        try {
            HttpURLConnection con = requestParams.setUpConnection();
            con.connect();

            int status_code = con.getResponseCode();
            Log.d("^^^^11111status",""+status_code);
            if(status_code==HttpURLConnection.HTTP_OK)
            {
                InputStream inputStream=con.getInputStream();
                episodesArrayList = XmlUtil.PullParser.parseGame(inputStream);
                Log.d("demo","array...."+episodesArrayList.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return episodesArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Episodes> episodes) {
        super.onPostExecute(episodes);
        activity.setUpData(episodes);
    }

    static  public interface IData{

        public void setUpData(ArrayList<Episodes> episodes);
        public Context getContext();
    }
}
