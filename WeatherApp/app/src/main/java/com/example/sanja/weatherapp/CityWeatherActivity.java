package com.example.sanja.weatherapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class CityWeatherActivity extends AppCompatActivity implements FetchCityKeyAsync.IData, FetchFiveDaysWeatherAsync.IDataFive, WeatherAdapter.ItemClickListener, GetCurrentCityWeatherAsync.ICityData, DisplayCityAdapter.ItemLongClickListener {

    public final String City = "city";
    public final String Country = "country";
    public final String Json = "json";
    String city;
    String country;
    String stringJsonFile;
    ArrayList<SaveCityObject> saveCityObjectArrayList;

    String key;
    String cityName;
    String countryName;
    String countryId;
    int flag=0;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    String fiveDaysJson;
    String tempInCelsius;
    ProgressDialog dialog;

    RecyclerView recyclerView;

    TextView dailyForecast;
    TextView textviewHeadline;
    TextView forecastOnDate;
    TextView tempMinMax;
    ImageView imageViewDay;
    ImageView imageViewNight;
    TextView dayPhrase;
    TextView nightPhrase;
    TextView moreDetails;
    TextView extForecast;

    DatabaseReference rootRef;
    DatabaseReference sRef;
    DatabaseReference Pcity;

    SaveCityObject object;
    String extMobileLink;
    String moreDetailsLink;


    ArrayList<FiveDaysWeatherObject> weatherObjectArrayList;
    WeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        Log.d("demo11","onCreate");
        dialog  = new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.show();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        moreDetails = (TextView)findViewById(R.id.textViewMoreDetails);
        extForecast = (TextView)findViewById(R.id.textViewExtForecast);

        moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","more details clicked"+moreDetailsLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(moreDetailsLink));
                startActivity(intent);
            }
        });

        extForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","extended forecast clicked"+extMobileLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(extMobileLink));
                startActivity(intent);
            }
        });


        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        rootRef = FirebaseDatabase.getInstance().getReference();
        sRef = rootRef.child("Cities");
        sRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("demo","snap"+dataSnapshot.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo","values "+dataSnapshot.getValue());
               saveCityObjectArrayList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SaveCityObject object12 = snapshot.getValue(SaveCityObject.class);
                    Log.d("demo","OBJ "+object12.toString());
                    saveCityObjectArrayList.add(object12);
                    Log.d("demo","saveCityObjectArrayList.size()  ////"+saveCityObjectArrayList.size());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dailyForecast = (TextView)findViewById(R.id.textViewDailyForecast);
        textviewHeadline = (TextView)findViewById(R.id.textViewHeadline);
        forecastOnDate = (TextView)findViewById(R.id.textViewForecastOnDate);
        tempMinMax = (TextView)findViewById(R.id.textViewTempMinMax);
        imageViewDay = (ImageView)findViewById(R.id.imageViewDay);
        imageViewNight = (ImageView)findViewById(R.id.imageViewNight);
        dayPhrase = (TextView)findViewById(R.id.textViewDayPhrase);
        nightPhrase = (TextView)findViewById(R.id.textViewNightPhrase);

        city = getIntent().getStringExtra(City);
        country = getIntent().getStringExtra(Country);
        stringJsonFile = getIntent().getStringExtra(Json);

        dailyForecast.setText("Daily forecast for "+city+", "+country);

       /* LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text_toast);
        text.setText("This is a custom toast");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, -20, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();*/
        /*String url = "http://dataservice.accuweather.com/locations/v1/"+country+"/search?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www&q="+city;

        new FetchCityKeyAsync(CityWeatherActivity.this).execute(url);*/
        Log.d("demo","JSON FILE "+stringJsonFile);
        getCityDataKey();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flag!=0){
            displayOneDayInfo(0);
        }
        else{

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_weather_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.saveCity:

                Pcity = sRef.push();                      //Pcity = ParticularCity
                String Uid = Pcity.getKey();
                object = new SaveCityObject();
                object.setUid1(Uid);
                object.setCityKey(key);
                object.setCityName(city);
                object.setCountry(country);
                object.setFavorite(false);
                ////////////
                Date date1 = new Date(System.currentTimeMillis());
                String str = String.valueOf(date1);
                SimpleDateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
                Log.d("demo","df "+df);
                Date date = null;
                try {
                    date = df.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long epoch = date.getTime();
                String cityEpochTime = String.valueOf(epoch);
                object.setTime(cityEpochTime);
                new GetCurrentCityWeatherAsync(CityWeatherActivity.this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+key+"?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www");
                //int tempValue = Integer.parseInt(tempInCelsius);
                return true;

            case R.id.setCurrentCity:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear().commit();
                editor.putString("Key",key);
                editor.putString("City", cityName);
                editor.putString("Country", countryName);
                editor.putString("CountryId", countryId);
                editor.commit();
                return true;

            case R.id.settings1:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUpData(String s) {
        stringJsonFile = s;


    }

    public void getCityDataKey(){

        try {
            JSONArray jsonArray = new JSONArray(stringJsonFile);

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            key = jsonObject.getString("Key");
            Log.d("demo","KEY  "+key);
            cityName = jsonObject.getString("LocalizedName");
            Log.d("demo","City Name  "+cityName);

            JSONObject jsonObject1 = jsonObject.getJSONObject("Country");
            countryName = jsonObject1.getString("LocalizedName");
            Log.d("demo","Country Name  "+countryName);
            countryId = jsonObject1.getString("ID");
            Log.d("demo","Country ID  "+countryId);

            getFiveDaysWeather();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getFiveDaysWeather(){
        String url = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/"+key+"?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www";
               // http://dataservice.accuweather.com/forecasts/v1/daily/5day/349818?apikey=RDYCDygCNABevym2K9oLmMQB3PXy5www
        new FetchFiveDaysWeatherAsync(CityWeatherActivity.this).execute(url);

    }

    @Override
    public void setUpFiveDaysData(String s) {
        fiveDaysJson = s;
        displayFiveDaysData();
        dialog.dismiss();
    }

    public  void displayFiveDaysData(){

        try {
            JSONObject jsonObject = new JSONObject(fiveDaysJson);
            JSONObject jsonObject1 = jsonObject.getJSONObject("Headline");
            String headline = jsonObject1.getString("Text");
            extMobileLink = jsonObject1.getString("MobileLink");
            textviewHeadline.setText(headline);

            JSONArray jsonArray = jsonObject.getJSONArray("DailyForecasts");
            weatherObjectArrayList = new ArrayList<>();

            for(int i =0;i<jsonArray.length();i++){

                FiveDaysWeatherObject weatherObject = new FiveDaysWeatherObject();
///////////////////////////////
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                String date = jsonObject2.getString("Date");
                date = date.substring(0,10);
                Log.d("demo","date substring"+date);

                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                Date new_date = null;
                try {
                    new_date = dt.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMM yy");
                String displayNewDate = fmtOut.format(new_date);
                //Log.d("demo","displayNewDate "+displayNewDate);
                weatherObject.setDate(displayNewDate);
                JSONObject jsonObjectTemp = jsonObject2.getJSONObject("Temperature");
                JSONObject jsonObjectMin = jsonObjectTemp.getJSONObject("Minimum");
                double minValue = jsonObjectMin.getDouble("Value");
                weatherObject.setMinVal(minValue);
                String unit = jsonObjectMin.getString("Unit");
                weatherObject.setUnit(unit);
                JSONObject jsonObjectMax = jsonObjectTemp.getJSONObject("Maximum");
                double maxValue = jsonObjectMax.getDouble("Value");
                weatherObject.setMaxVal(maxValue);

                JSONObject jsonObjectDay = jsonObject2.getJSONObject("Day");
                int dayIcon = jsonObjectDay.getInt("Icon");
                weatherObject.setDayIcon(dayIcon);
                String dayIconPhrase = jsonObjectDay.getString("IconPhrase");
                weatherObject.setDayIconPhrase(dayIconPhrase);

                JSONObject jsonObjectNight = jsonObject2.getJSONObject("Night");
                int nightIcon = jsonObjectNight.getInt("Icon");
                weatherObject.setNightIcon(nightIcon);
                String nightIconPhrase = jsonObjectNight.getString("IconPhrase");
                weatherObject.setNightIconPhrase(nightIconPhrase);

                /*JSONObject jsonObjectMobileLink = jsonObject2.getJSONObject("MobileLink");*/
                String mobileLink = jsonObject2.getString("MobileLink");
                weatherObject.setMobileLink(mobileLink);

                weatherObjectArrayList.add(weatherObject);
                flag =1;
                displayOneDayInfo(0);

            }
            Log.d("demo","weatherObjectArrayList  "+weatherObjectArrayList.toString());

            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            adapter = new WeatherAdapter(weatherObjectArrayList, this);
            recyclerView.setAdapter(adapter);
            Log.d("demo","DONE SET");
            adapter.setItemClickListener(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void displayOneDayInfo(int i){
        FiveDaysWeatherObject object = weatherObjectArrayList.get(i);

        moreDetailsLink = object.getMobileLink();
        String forecastOnDateString = object.getDate();
        SimpleDateFormat dt = new SimpleDateFormat("dd MMM yy");
        Date new_date = null;
        try {
            new_date = dt.parse(forecastOnDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("MMMM dd, yyyy");
        String displayNewDate = fmtOut.format(new_date);
       /* String month = displayNewDate.substring(3,8);
        String year12 = displayNewDate.substring(9,12);
        String date123 = displayNewDate.substring(1,2);*/
        forecastOnDate.setText("Forecast on "+displayNewDate);
        int minValue = (int) object.getMinVal();
        int maxValue = (int) object.getMaxVal();
        String DEGREE  = "\u00b0";
        if(sharedpreferences.getString("TempUnit","Celsius").equals("Celsius")){
            int minC =  (((minValue-32)*5)/9);
            int maxC = (((maxValue-32)*5)/9);
            tempMinMax.setText("Temperature : "+maxC+DEGREE+"/ "+minC+DEGREE);
        }
        else {
            tempMinMax.setText("Temperature : "+maxValue+DEGREE+"/ "+minValue+DEGREE);
        }

        int dayIconId = object.getDayIcon();
        if(dayIconId<10){
            /*new DisplayCurrentConditionsAsync().execute("http://developer.accuweather.com/sites/default/files/0"+wId+"-s.png");*/
            String url = "http://developer.accuweather.com/sites/default/files/0"+dayIconId+"-s.png";
            Picasso.with(this).load(url).fit().centerCrop().into(imageViewDay);
        }
        else {
            // new DisplayCurrentConditionsAsync().execute("http://developer.accuweather.com/sites/default/files/"+wId+"-s.png");
            String url = "http://developer.accuweather.com/sites/default/files/"+dayIconId+"-s.png";
            Picasso.with(this).load(url).fit().centerCrop().into(imageViewDay);
        }
        int nightIconId = object.getNightIcon();
        if(nightIconId<10){
            /*new DisplayCurrentConditionsAsync().execute("http://developer.accuweather.com/sites/default/files/0"+wId+"-s.png");*/
            String url = "http://developer.accuweather.com/sites/default/files/0"+nightIconId+"-s.png";
            Picasso.with(this).load(url).fit().centerCrop().into(imageViewNight);
        }
        else {
            // new DisplayCurrentConditionsAsync().execute("http://developer.accuweather.com/sites/default/files/"+wId+"-s.png");
            String url = "http://developer.accuweather.com/sites/default/files/"+nightIconId+"-s.png";
            Picasso.with(this).load(url).fit().centerCrop().into(imageViewNight);
        }

        String dayPhraseString = object.getDayIconPhrase();
        dayPhrase.setText(dayPhraseString);

        String nightPhraseString = object.getNightIconPhrase();
        nightPhrase.setText(nightPhraseString);
    }

    @Override
    public void setUpCityData(String s) {
        int pos=-1;
        tempInCelsius = s;
        object.setTempInC(tempInCelsius);
        for(int i=0;i<saveCityObjectArrayList.size();i++){
            if(saveCityObjectArrayList.get(i).getCityKey().equals(object.getCityKey())){
                pos = i;

                break;
            }
            else{
                continue;
            }
        }

        if(pos==-1){
            Pcity.setValue(object);
            saveCityObjectArrayList.add(object);
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));

            TextView text = (TextView) layout.findViewById(R.id.text_toast);
            text.setText("City Added");

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, -20, 100);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
        else {
            SaveCityObject object1 = saveCityObjectArrayList.get(pos);
            object1.setTempInC(object.getTempInC());
            Date date1 = new Date(System.currentTimeMillis());
            String str = String.valueOf(date1);
            SimpleDateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
            Log.d("demo","df "+df);
            Date date = null;
            try {
                date = df.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long epoch = date.getTime();
            String cityEpochTime = String.valueOf(epoch);
            object1.setTime(cityEpochTime);
            saveCityObjectArrayList.set(pos,object1);
            Log.d("demo","saveCityObjectArrayList SAVED "+saveCityObjectArrayList.toString());
            DatabaseReference reference = sRef.child(object1.getUid1());
            reference.setValue(object1);
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));

            TextView text = (TextView) layout.findViewById(R.id.text_toast);
            text.setText("City Updated");

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, -20, 100);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }

        /*if(saveCityObjectArrayList.contains(object)){
            DatabaseReference reference = sRef.child(object.getUid());

        }else{
        Pcity.setValue(object);
        saveCityObjectArrayList.add(object);
        }*/
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void onOtherItemClick(int position) {
        Log.d("demo","position "+position);
        displayOneDayInfo(position);

    }

    @Override
    public void onItemLongClick(int position) {

    }
}
