package com.example.sanja.weatherapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements DisplayCityAdapter.ItemClickListener,DisplayCityAdapter.ItemLongClickListener,FetchCityKeyAsync.IData{

    Button setCity;
    String response1;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    String locObservDateTime;
    String weatherTxt;
    String weatherIcon;
    String tempValueInCel;
    String unit;
    ImageView imageView;
    LinearLayout linearLayout;
    TextView currCity;
    TextView mostly;
    ProgressBar progressBar;
    String searchCity12;
    String searchCountry12;
    TextView temp;
    TextView updated;
    Button search;
    EditText searchCity;
    EditText searchCountry;
    static RecyclerView recyclerView;
    TextView view;
    TextView noData1;

    DatabaseReference rootRef;
    DatabaseReference sRef;
    DisplayCityAdapter cityAdapter;
    static ArrayList<SaveCityObject> savedCityObjectArrayList;


    public final String City = "city";
    public final String Country = "country";
    public final String Json = "json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));


        noData1 = (TextView)findViewById(R.id.textViewNoData1);
        view = (TextView)findViewById(R.id.textViewNoDataMsg);
        setCity = (Button)findViewById(R.id.buttonSetCity);
        imageView = (ImageView)findViewById(R.id.imageViewIcon);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout12);
        currCity = (TextView)findViewById(R.id.textViewCurrentCityMsg);
        mostly = (TextView)findViewById(R.id.textViewMostly);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewVertical);
        progressBar = (ProgressBar)findViewById(R.id.progressBar12);
        temp = (TextView)findViewById(R.id.textViewTemp);
        updated = (TextView)findViewById(R.id.textViewUpdated);
        searchCity = (EditText)findViewById(R.id.editTextCity);
        searchCountry = (EditText)findViewById(R.id.editTextCountry);
        search = (Button)findViewById(R.id.buttonSearch);
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        getRecyclerViewData();

        setCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                final LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                final EditText cityAlert = new EditText(MainActivity.this);
                cityAlert.setHint("Enter Your City");
                final EditText countryAlert = new EditText(MainActivity.this);
                countryAlert.setHint("Enter Your Country");
                linearLayout.addView(cityAlert);
                linearLayout.addView(countryAlert);
                alert.setTitle("Enter City Details").setView(linearLayout)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String setCity = cityAlert.getText().toString().trim();
                        String setCountry = countryAlert.getText().toString().trim();
                        if(setCity!=null &&setCountry!=null && setCity.length()!=0 &&setCountry.length()!=0){
                           /* Toast.makeText(MainActivity.this, "Current city details saved", Toast.LENGTH_SHORT).show();*/

                            String url = "http://dataservice.accuweather.com/locations/v1/"+setCountry+"/search?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www&q="+setCity;
                            //http://dataservice.accuweather.com/locations/v1/US/search?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www&q=Charlotte
                            progressBar.setVisibility(View.VISIBLE);
                            new FetchDataAsync().execute(url);
                            Log.d("demo","DONE");
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Enter Correct Details",Toast.LENGTH_LONG).show();
                        }

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCity12 = searchCity.getText().toString();
                searchCountry12 = searchCountry.getText().toString();
                if(searchCity12 != null && searchCountry12 != null && searchCity12.length()!=0 && searchCity12.length()!=0){

                    String url = "http://dataservice.accuweather.com/locations/v1/"+searchCountry12+"/search?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www&q="+searchCity12;

                    new FetchCityKeyAsync(MainActivity.this).execute(url);


                }
                else{
                    Toast.makeText(MainActivity.this, "Enter correctly!!!!", Toast.LENGTH_SHORT).show();
                }
                searchCity.setText("");
                searchCountry.setText("");
            }
        });
    }

    public void getRecyclerViewData(){

        rootRef = FirebaseDatabase.getInstance().getReference();
        sRef = rootRef.child("Cities");
        sRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                savedCityObjectArrayList = new ArrayList<>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SaveCityObject object12 = snapshot.getValue(SaveCityObject.class);
                    Log.d("demo","OBJ "+object12.toString());
                    savedCityObjectArrayList.add(object12);
                }
                if(savedCityObjectArrayList.size()>=0){
                    if(savedCityObjectArrayList.size()==0){
                        view.setVisibility(View.VISIBLE);
                        noData1.setVisibility(View.VISIBLE);
                        view.setText("There are no cities to display.");
                        noData1.setText("Search the city from the search box and save.");


                    }
                    else {
                        view.setVisibility(View.VISIBLE);
                        view.setText("Saved Cities");
                        noData1.setVisibility(View.INVISIBLE);
                        Log.d("demo","view text SAVED CITIES");

                    }
                    Collections.sort(savedCityObjectArrayList, SaveCityObject.comparatorPrior);

                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    cityAdapter = new DisplayCityAdapter(savedCityObjectArrayList, MainActivity.this);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(cityAdapter);


                    cityAdapter.setItemClickListener(MainActivity.this);
                    cityAdapter.setItemLongClickListener(MainActivity.this);

                }else{
                    Log.d("demo","size zero");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*for clearing shared preferences*/
       /* SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().commit();*/
        Log.d("demo","getALL>>>> "+sharedpreferences.getAll());
        if(sharedpreferences.getAll().size() != 0){
            Log.d("demo","/////////////////////////////////");
            getCurrentConditionData();
        }
        else{
            Log.d("demo","sp is null");
        }

    }
    @Override
    public void onOtherItemClick(int position) {
        Log.d("demo","onOtherItemClick "+position);

    }

    @Override
    public void onImageButtonClick(int postion) {


    }

    @Override
    public void onItemLongClick(int position) {
        Log.d("demo","onItemLONGClick "+position);
        rootRef = FirebaseDatabase.getInstance().getReference();
        sRef = rootRef.child("Cities");
        Log.d("demo1","POSITION "+savedCityObjectArrayList.toString());

        /*DatabaseReference reference = */sRef.child(savedCityObjectArrayList.get(position).getUid1()).removeValue();
        savedCityObjectArrayList.remove(position);
       /* reference.removeValue();*/


    }

    @Override
    public void setUpData(String s) {
        Log.d("ddemo","287361287362638163"+s);
        if(s.equals("[]")){
            Toast.makeText(this, "Invalid Entry", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(),CityWeatherActivity.class);
            intent.putExtra(City, searchCity12);
            intent.putExtra(Country, searchCountry12);
            intent.putExtra(Country, searchCountry12);
            intent.putExtra(Json,s);
            startActivity(intent);
        }
    }

    @Override
    public Context getContext() {
        return null;
    }

    public class FetchDataAsync extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            Log.d("demo","url  "+url);

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

                if (s.equals("[]")){
                    progressBar.setVisibility(View.INVISIBLE);
                    runOnUiThread(new Runnable() {
                        public void run() {

                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast,
                                    (ViewGroup) findViewById(R.id.custom_toast_container));

                            TextView text = (TextView) layout.findViewById(R.id.text_toast);
                            text.setText("City not found");

                            Toast toast = new Toast(getApplicationContext());
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


                        if (sharedpreferences.getAll().size()==0){
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast,
                                    (ViewGroup) findViewById(R.id.custom_toast_container));

                            TextView text = (TextView) layout.findViewById(R.id.text_toast);
                            text.setText("Current city details saved");

                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, -20, 100);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Key",key);
                        editor.putString("City", cityName);
                        editor.putString("Country", countryName);
                        editor.putString("CountryId", countryId);
                        editor.commit();

                        Log.d("demo","Shared Pref "+sharedpreferences.getAll());

                        getCurrentConditionData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }else{
                Log.d("demo","NULL STRING");
            }

        }
    }

    public void getCurrentConditionData(){

        Log.d("demo","INSIDE METHOD");
        new getCurrentCityConditionsAsync().execute("http://dataservice.accuweather.com/currentconditions/v1/"+sharedpreferences.getString("Key",null)+"?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www");
        //http://dataservice.accuweather.com/currentconditions/v1/349818?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www



    }

    public class getCurrentCityConditionsAsync extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... params) {

            String url = params[0];
            Log.d("demo", "getCurrentCityConditionsAsync  DO IN BACK "+url);

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
            Log.d("demo","response TAKEN IN POST METHOD//// "+s);
            if(s!=null){
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    locObservDateTime = jsonObject.getString("EpochTime");
                    //String locObDtTimeNew = locObservDateTime.substring(0,10);
                    weatherTxt = jsonObject.getString("WeatherText");
                    weatherIcon = jsonObject.getString("WeatherIcon");

                    JSONObject  jsonObject1 = jsonObject.getJSONObject("Temperature");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("Metric");
                    tempValueInCel = jsonObject2.getString("Value");
                    unit = jsonObject2.getString("Unit");

                    Log.d("demo","loc "+locObservDateTime);
                    Log.d("demo","weat txt  "+weatherTxt);
                    Log.d("demo","weat icon "+weatherIcon);
                    Log.d("demo","temp val "+tempValueInCel);
                    Log.d("demo","unit "+unit);

                    displayCurrentCityData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                Log.d("demo","NULL 's'  ");
            }


        }
    }

    public void displayCurrentCityData(){
        progressBar.setVisibility(View.INVISIBLE);

        //http://developer.accuweather.com/sites/default/files/01-s.png
        int wId = Integer.parseInt(weatherIcon);
        if(wId<10){
            /*new DisplayCurrentConditionsAsync().execute("http://developer.accuweather.com/sites/default/files/0"+wId+"-s.png");*/
            String url = "http://developer.accuweather.com/sites/default/files/0"+wId+"-s.png";
            Picasso.with(MainActivity.this).load(url).fit().centerCrop().into(imageView);
        }
        else {
           // new DisplayCurrentConditionsAsync().execute("http://developer.accuweather.com/sites/default/files/"+wId+"-s.png");
            String url = "http://developer.accuweather.com/sites/default/files/"+wId+"-s.png";
            Picasso.with(MainActivity.this).load(url).fit().centerCrop().into(imageView);
        }

        linearLayout.removeView(setCity);
        String countryID = sharedpreferences.getString("CountryId", "");
        String city = sharedpreferences.getString("City", "");
        currCity.setText(city+", "+countryID);
        mostly.setText(weatherTxt);
        final String DEGREE  = "\u00b0";
        double tempValue = Double.parseDouble(tempValueInCel);

        if(sharedpreferences.getString("TempUnit","Celsius").equals("Celsius")){
            temp.setText("Temperature : "+tempValueInCel+ DEGREE +unit);
        }
        else {
            double ftemp = ((tempValue*9)/5)+32;
            double newFVal = Math.round((ftemp/10.0)*10.0);
            temp.setText("Temperature : "+newFVal+ DEGREE +"F");
        }
        PrettyTime p = new PrettyTime();
        updated.setText(p.format(new Date(Long.parseLong(locObservDateTime)*1000)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.settings:
                Log.d("demo","settings clicked");
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return  true;


            default:
                Log.d("demo","Something is Wrong!!");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
