package com.example.sanja.podcastapp;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by sanja on 3/8/2017.
 */

public class RequestParams {

    String method, url;

    public RequestParams(String method, String url) {
        this.method = method;
        this.url = url;
    }

  /*  public String getEncodedParams()
    {
        StringBuilder stringBuilder=new StringBuilder();
        for(String key:params.keySet())
        {
            try {
                String value= URLEncoder.encode(params.get(key),"UTF-8");
               *//* if(stringBuilder.length()>0)
                {
                    stringBuilder.append("&");

                }*//*
                stringBuilder.append(key+"="+value);
                Log.d("demo","string builder "+stringBuilder.toString());

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }*/
    /*public String getEncodedURL()
    {
        return  url+"GetGamesList.php"+"?"+getEncodedParams();
    }*/

    public HttpURLConnection setUpConnection() throws IOException {
        HttpURLConnection httpURLConnection=null;
        if(method.equals("GET"))
        {
            Log.d("check here","test");
            URL url=new URL(this.url);
            httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            return httpURLConnection;
        }
        else
        {
            URL url=new URL(this.url);
            httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStreamWriter.write(this.url);
            outputStreamWriter.flush();
        }
        return httpURLConnection;
    }
}
