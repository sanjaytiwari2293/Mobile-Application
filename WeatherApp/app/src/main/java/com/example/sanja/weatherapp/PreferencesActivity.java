package com.example.sanja.weatherapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PreferencesActivity extends AppCompatActivity implements FetchCityKeyAsync.IData {

    public static final String MyPREFERENCES = "MyPrefs" ;
    static SharedPreferences sharedpreferences;
    static String city;
    static String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.ThemeOverlay_Material_Dark_ActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        /*setTitle("Preferences");*/
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + "Preferences" + "</font>")));
        //////////////////////////////
      /*  Spannable text = new SpannableString(getSupportActionBar().getTitle());
        Log.d("demo","text"+text);
        text.setSpan(new BackgroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);*/

        getSupportActionBar().setDisplayShowHomeEnabled(true);
   /*     getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);*/
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Fragment fragment = new SettingsScreen();
                /*FragmentManager manager = fragment.getActivity().getFragmentManager();*/

        getFragmentManager().beginTransaction().add(R.id.container12, fragment , "settings" )
                .commit();


    }




    @Override
    public void setUpData(String s) {

        try {
            JSONArray jsonArray = new JSONArray(s);

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String key = jsonObject.getString("Key");
            Log.d("demo","KEY  "+key);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Key",key);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Context getContext() {
        return null;
    }


    @SuppressLint("ValidFragment")
    public  class  SettingsScreen extends PreferenceFragment {
        String sel;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
            Preference preferenceTemp = findPreference("tempUnit");
            Preference preferenceCity = findPreference("changeCity");

            preferenceCity.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    final LinearLayout linearLayout = new LinearLayout(getActivity());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    final EditText cityAlert = new EditText(getActivity());

                    cityAlert.setText(sharedpreferences.getString("City",""));
                    final EditText countryAlert = new EditText(getActivity());
                    countryAlert.setText(sharedpreferences.getString("CountryId",""));
                    if(sharedpreferences.getAll().size()==0){
                        cityAlert.setHint("Enter Your City");
                        countryAlert.setHint("Enter Your Country");
                        alert.setTitle("Enter City Details");
                    }
                    else {
                        alert.setTitle("Update City Details");
                    }
                    linearLayout.addView(cityAlert);
                    linearLayout.addView(countryAlert);
                    alert.setView(linearLayout)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    city = cityAlert.getText().toString().trim();
                                    country = countryAlert.getText().toString().trim();
                                    if(city!=null &&country!=null && city.length()!=0 &&country.length()!=0){
                                        /*Toast.makeText(getActivity(), city + " " + country,
                                                Toast.LENGTH_LONG).show();*/

                                        callAsync();
                                        /*SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("City",city);
                                        editor.putString("CountryId", country);
                                        editor.commit();*/
                                        

                                        /*String url = "http://dataservice.accuweather.com/locations/v1/"+country+"/search?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www&q="+city;
                                        //http://dataservice.accuweather.com/locations/v1/US/search?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www&q=Charlotte
                                        new FetchDataAsync().execute(url);*/
                                        Log.d("demo","DONE");
                                    }
                                    else{
                                        //Toast.makeText(getActivity(),"Enter Correct Details",Toast.LENGTH_LONG).show();
                                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                                        View layout = inflater.inflate(R.layout.custom_toast,
                                                (ViewGroup) getActivity().findViewById(R.id.custom_toast_container));

                                        TextView text = (TextView) layout.findViewById(R.id.text_toast);
                                        text.setText("Enter Correct Details");

                                        Toast toast = new Toast(getActivity());
                                        toast.setGravity(Gravity.BOTTOM, -20, 100);
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setView(layout);
                                        toast.show();
                                    }

                                }
                            });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();



                    return true;
                }
            });

            preferenceTemp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Log.d("demo","clicked");
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
                    //alt_bld.setIcon(R.drawable.icon);
                    final String[] grpname = new String[2];
                    grpname[0] = "Celsius";
                    grpname[1] = "Fahrenheit";
                    int pos;
                    if(sharedpreferences.getString("TempUnit","Celsius").equals("Celsius")){
                        pos = 0;

                    }
                    else{
                        pos = 1;
                    }

                    sel = grpname[pos];

                    alt_bld.setTitle("Choose a Temperature Unit");
                    alt_bld.setSingleChoiceItems(grpname, pos, new DialogInterface
                            .OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            sel = grpname[item];
                            Log.d("demo","string "+sel);
                        }
                    });
                    alt_bld.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(sel.equals("Celsius")){
                                //Toast.makeText(getActivity(), "Temperature changed from Fahrenheit to "+sel, Toast.LENGTH_SHORT).show();
                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                                View layout = inflater.inflate(R.layout.custom_toast,
                                        (ViewGroup) getActivity().findViewById(R.id.custom_toast_container));

                                TextView text = (TextView) layout.findViewById(R.id.text_toast);
                                text.setText("Temperature changed from Fahrenheit to "+sel);

                                Toast toast = new Toast(getActivity());
                                toast.setGravity(Gravity.BOTTOM, -20, 100);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                            }
                            else{
                                //Toast.makeText(getActivity(), "Temperature changed from Celsius to "+sel, Toast.LENGTH_SHORT).show();
                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                                View layout = inflater.inflate(R.layout.custom_toast,
                                        (ViewGroup) getActivity().findViewById(R.id.custom_toast_container));

                                TextView text = (TextView) layout.findViewById(R.id.text_toast);
                                text.setText("Temperature changed from Celsius to "+sel);

                                Toast toast = new Toast(getActivity());
                                toast.setGravity(Gravity.BOTTOM, -20, 100);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                            }


                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("TempUnit",sel);
                            editor.commit();
                            Log.d("demo","shared pref "+sharedpreferences.getAll());
                            dialog.dismiss();// dismiss the alertbox after chose option
                        }
                    });
                    AlertDialog alert = alt_bld.create();
                    alert.show();


                    return true;
                }
            });


        }
        public void callAsync(){

            String url = "http://dataservice.accuweather.com/locations/v1/"+country+"/search?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www&q="+city;
            new FetchDataAsync().execute(url);

        }
    }
    public class FetchDataAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            Log.d("demo","url121  "+url);

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
            Log.d("demo","response  onPOST "+s);
            if(s!=null){
                if(s.equals("[]")){
                    Log.d("demo","Invalid entry!!");

                    ////////////////////////
                    //////
                    runOnUiThread(new Runnable() {
                        public void run() {

                            LayoutInflater inflater = (LayoutInflater) PreferencesActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.custom_toast,
                                    (ViewGroup) findViewById(R.id.custom_toast_container));

                            TextView text = (TextView) layout.findViewById(R.id.text_toast);
                            text.setText("City Not Found");

                            Toast toast = new Toast(PreferencesActivity.this);
                            toast.setGravity(Gravity.BOTTOM, -20, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }
                    });


                }
                else{
                    try {
                        JSONArray jsonArray = new JSONArray(s);

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String key = jsonObject.getString("Key");
                        Log.d("demo","KEY  "+key);
                        String cityName = jsonObject.getString("LocalizedName");
                        Log.d("demo","City Name  "+cityName);

                        JSONObject jsonObject1 = jsonObject.getJSONObject("Country");
                        String countryName = jsonObject1.getString("LocalizedName");
                        Log.d("demo","Country Name  "+countryName);
                        String countryId = jsonObject1.getString("ID");
                        Log.d("demo","Country ID  "+countryId);

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Key",key);
                        editor.putString("City", cityName);
                        editor.putString("Country", countryName);
                        editor.putString("CountryId", countryId);
                        editor.commit();

                        LayoutInflater inflater = (LayoutInflater) PreferencesActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.custom_toast,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView text = (TextView) layout.findViewById(R.id.text_toast);
                        text.setText("Current City Saved");

                        Toast toast = new Toast(PreferencesActivity.this);
                        toast.setGravity(Gravity.BOTTOM, -20, 100);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        Log.d("demo","Shared Pref "+sharedpreferences.getAll());



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            }else{
                Log.d("demo","NULL STRING");
            }

        }




    }
}
