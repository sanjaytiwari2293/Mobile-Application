package com.example.sanja.podcastapp;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by sanja on 3/8/2017.
 */

public class XmlUtil {

    static public class PullParser{

        static public ArrayList<Episodes> parseGame(InputStream in) throws XmlPullParserException, IOException {
            ArrayList<Episodes> episodesList = new ArrayList<>();
            Episodes episode = null;

            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(in, "UTF-8");
            int event = xmlPullParser.getEventType();

            boolean ctitle = true;
            boolean cimg = true;
            boolean cdes = true;

            while(event != XmlPullParser.END_DOCUMENT){

                switch (event){
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("item")){
                            Log.d("demo","item....");
                            episode = new Episodes();
                        }
                        /*else if(xmlPullParser.getName().equals("title") && episode != null && ctitle){
                            Log.d("demo","title....");
                            ctitle = false;

                        }*/
                        else if (xmlPullParser.getName().equals("title") && episode != null){
                            String title = xmlPullParser.nextText().toString().trim();
                            episode.setTitle(title);
                            Log.d("demo","title1...."+title);

                        }
                        else if (xmlPullParser.getName().equals("pubDate") && episode != null){
                            String r_date = xmlPullParser.nextText().toString().trim();
                            episode.setRel_date(r_date);
                            Log.d("demo","r_date...."+r_date);

                        }
                        /*else if (xmlPullParser.getName().equals("itunes:image") && episode != null && cimg ){
                            cimg = false;
                        }*/
                        else if (xmlPullParser.getName().equals("itunes:image") && episode != null ){
                            String image = xmlPullParser.getAttributeValue(null,"href");
                            episode.setImg_url(image);
                            Log.d("demo","image....");
                        }
                        else if (xmlPullParser.getName().equals("itunes:duration") && episode != null){
                            String duration = xmlPullParser.nextText().toString().trim();
                            episode.setDuration(duration);
                        }
                        else if (xmlPullParser.getName().equals("description") && episode != null ){
                            String desc = xmlPullParser.nextText().toString().trim();
                            episode.setDescription(desc);
                        }
                        else if (xmlPullParser.getName().equals("enclosure") && episode != null ){

                            String a_url =  xmlPullParser.getAttributeValue(null,"url");
                            episode.setAudio_url(a_url);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(xmlPullParser.getName().equals("item")){
                            episodesList.add(episode);
                            episode = null;
                        }

                }
                event=xmlPullParser.next();
            }


            return episodesList;
        }
    }


}
