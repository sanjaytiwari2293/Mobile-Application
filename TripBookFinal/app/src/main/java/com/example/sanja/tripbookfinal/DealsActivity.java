package com.example.sanja.tripbookfinal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DealsActivity extends AppCompatActivity implements PlacesAdapter.ItemClickListener{

    SeekBar seekBar;
    TextView costFilter;
    EditText editTextPeople;
    Button buttonRefresh;
    Switch aSwitch;
    RecyclerView recyclerViewDeals;
    Button addToTripsDeals;
    Button addToWishDeals;
    RadioGroup radioGroup;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    String userId = null;
    static int flag=0;
    int budget;
    int numOfPeople=1;
    boolean isSwitchOn=false;
    public static final String Place = "place";
    String myPlace=null;


    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Deals deals;
    ArrayList<Deals> dealsArrayList = new ArrayList<>();
    ArrayList<Deals> tempDealsArrayList = new ArrayList<>();
    ArrayList<Deals> myTripsArrayList = new ArrayList<>();
    ArrayList<Deals> myWishArrayList = new ArrayList<>();
    ArrayList<Deals> filteredArrayList = new ArrayList<>();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("User");
    DatabaseReference mDealsRef = mRootRef.child("Deals");
    DatabaseReference mPlacesRef = mRootRef.child("Places");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);

        setTitle("Deals");
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("uid","");
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        costFilter = (TextView)findViewById(R.id.textViewCostShow);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setProgress(0);
        seekBar.incrementProgressBy(1);
        seekBar.setMax(10000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int seek = progress;
                Log.d("demo","progress "+seek);
                costFilter.setText(""+seek);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mUserRef.child(userId).child("Trips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myTripsArrayList = new ArrayList<Deals>();
                Deals deals = new Deals();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    deals = snapshot.getValue(Deals.class);
                    myTripsArrayList.add(deals);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUserRef.child(userId).child("Wishlist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Deals deals = new Deals();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    deals = snapshot.getValue(Deals.class);
                    myWishArrayList.add(deals);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editTextPeople = (EditText)findViewById(R.id.editTextNoOfPeople);
        buttonRefresh = (Button)findViewById(R.id.buttonRefresh);
        aSwitch = (Switch)findViewById(R.id.switch1);
        recyclerViewDeals = (RecyclerView)findViewById(R.id.recyclerViewDeals);
       /* addToTripsDeals = (Button)findViewById(R.id.buttonAddToTrip);
        addToWishDeals = (Button)findViewById(R.id.buttonAddToWish);*/

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("deals","switch "+isChecked);
                if (isChecked){
                    isSwitchOn=true;
                    if (filteredArrayList.size()!=0){
                        showDeals(filteredArrayList);
                    }
                    else {
                        showDeals(dealsArrayList);
                    }
                }
                else{
                    isSwitchOn=false;
                    if (filteredArrayList.size()!=0){
                        showDeals(filteredArrayList);
                    }
                    else {
                        showDeals(dealsArrayList);
                    }
                }

            }
        });

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budget = seekBar.getProgress();
                numOfPeople = Integer.parseInt(editTextPeople.getText().toString());
                Log.d("deals","budget "+budget);
                Log.d("deals","people "+numOfPeople);
                filteredArrayList = new ArrayList<Deals>(

                );
                for (int i=0;i<dealsArrayList.size();i++){
                    Deals deals = dealsArrayList.get(i);
                    long cost = deals.getcost();
                    cost = cost*numOfPeople;

                    if (cost<=budget){
                        Log.d("deals","cost "+cost);
                        filteredArrayList.add(deals);
                    }
                }
                showDeals(filteredArrayList);
            }
        });

        mDealsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dealsArrayList = new ArrayList();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Deals deals1 = snapshot.getValue(Deals.class);
                    dealsArrayList.add(deals1);

                }
                Log.d("demo","deals123 "+dealsArrayList.toString());
                showDeals(dealsArrayList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showDeals(ArrayList<Deals> dealsArrayList) {

        if (dealsArrayList.size()!=0){
            //1st time
            Log.d("demo","1st time_dealsAct");
            tempDealsArrayList = dealsArrayList;
            if (myTripsArrayList.size()!=0){
                Log.d("dealsAct","myTrips "+myTripsArrayList.toString());
                Log.d("dealsAct","size before "+dealsArrayList.size());
                dealsArrayList.removeAll(myTripsArrayList);
              /*  for (int i=0;i<myTripsArrayList.size();i++){
                    for (int j=0;j<dealsArrayList.size();j++){
                        if (myTripsArrayList.get(i).getplace().equals(dealsArrayList.get(j).getplace())){
                            Log.d("dealsAct","INSIDE!!");
                            dealsArrayList.remove(j);
                        }
                        else {
                            //Log.d("dealsAct","NOT INSIDE!!");
                        }
                    }
                }*/

            }
            if (myWishArrayList.size()!=0){
                dealsArrayList.removeAll(myWishArrayList);
            }
            else{
                Log.d("dealsAct","my trip size is 0..");
            }
            Log.d("dealsAct","size after "+dealsArrayList.size());
            if (isSwitchOn==false){
                recyclerViewDeals.setLayoutManager(new LinearLayoutManager(this));
            }
            else{
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
                recyclerViewDeals.setLayoutManager(layoutManager);
            }
            PlacesAdapter adapter = new PlacesAdapter(dealsArrayList, DealsActivity.this,numOfPeople);
            recyclerViewDeals.setAdapter(adapter);
            adapter.setItemClickListener(DealsActivity.this);

        }
        else {
            Log.d("demo","size zero_dealAct");
        }

    }


    @Override
    public void onViewMapClick(int position) {

        Log.d("deals","Opening Map...");
        if (myPlace==null){
            myPlace = getCityName(35.307748, -80.733423);
        }
        String otherPlace=null;
        if (filteredArrayList.size()!=0){
            otherPlace = filteredArrayList.get(position).getplace();
        }else {
           otherPlace = dealsArrayList.get(position).getplace();
        }

        final String uri = "http://maps.google.com/maps?daddr="+myPlace+"+to:"+otherPlace;
        final Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    private String getCityName(double lat, double lon) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getLocality();
     /*   String stateName = addresses.get(0).getAddressLine(1);
        String countryName = addresses.get(0).getAddressLine(2);
        Log.d("maps",cityName+" "+stateName+" "+countryName);*/

        return cityName;
    }

    @Override
    public void onContainerClick(final int adapterPosition) {

        final Dialog dialog = new Dialog(DealsActivity.this);
        dialog.setContentView(R.layout.custom_alert_dialog);
        dialog.setTitle("This is my custom dialog box");
        dialog.setCancelable(true);

        radioGroup = (RadioGroup)dialog.findViewById(R.id.radioGrpCustom);
        RadioButton trip = (RadioButton) dialog.findViewById(R.id.radioButtonTripAdd);
        RadioButton wish = (RadioButton) dialog.findViewById(R.id.radioButtonWishlistAdd);
        Button cancel = (Button)dialog.findViewById(R.id.buttonCustomCancel);
        Button add = (Button)dialog.findViewById(R.id.buttonCustomAdd);
        trip.setChecked(true);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Deals deals1 = tempDealsArrayList.get(adapterPosition);
                Log.d("dealsAct","temp size "+tempDealsArrayList.size());
                Log.d("dealsAct","deals1 "+deals1.toString());
                //remove
                tempDealsArrayList.remove(adapterPosition);
                Log.d("dealsAct","temp size "+tempDealsArrayList.size());
                int id =  radioGroup.getCheckedRadioButtonId();
                if (id==R.id.radioButtonTripAdd){
                    Log.d("demo","Add to trip");
                    addToMyTrip(deals1);
                }
                else if(id==R.id.radioButtonWishlistAdd){
                    Log.d("demo","Add to wishlist");
                    addToMyWishlist(deals1);
                }
                else{
                    Log.d("demo","No Selection");
                }
                dialog.dismiss();
            }
        });
        // now that the dialog is set up, it's time to show it
        dialog.show();
    }

    private void addToMyWishlist(Deals deals1) {
        myWishArrayList.add(deals1);
        mUserRef.child(userId).child("Wishlist").setValue(myWishArrayList);
        showDeals(dealsArrayList);
    }

    private void addToMyTrip(Deals deal) {

        myTripsArrayList.add(deal);
        mUserRef.child(userId).child("Trips").setValue(myTripsArrayList);
        showDeals(dealsArrayList);

    }
}
