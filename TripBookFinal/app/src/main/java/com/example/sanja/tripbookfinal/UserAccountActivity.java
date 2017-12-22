package com.example.sanja.tripbookfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class UserAccountActivity extends AppCompatActivity {

    final String User1 = "details";
    final String Edit = "details";
    final String UId = "uid";
    final String Temp1MY = "myfrnds";
    final String Temp2SR = "showReq";
    final String MyFrnds = "friends";

    User user = null;

    Button getDeal;
    Button createTrip;
    Button tripList;
    Button wishList;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        setTitle("Summer Trip");


        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if(getIntent().getSerializableExtra(User1)!= null){
            user = (User) getIntent().getSerializableExtra(User1);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            //details of logged in user saved in shared preferences.
            editor.putString("email",user.getEmail());
            editor.putString("fname",user.getFname());
            editor.putString("lname",user.getLname());
            editor.putString("uid", user.getUid());
            editor.putString("gender", user.getGender());
            editor.commit();
            Log.d("demoACTIVITY M","user"+user.toString());
            //displayPicture(user);
        }
        else {
            Log.d("demo","null get intent");
        }

        getDeal = (Button)findViewById(R.id.buttonGetDeal);
        createTrip = (Button)findViewById(R.id.buttonCreateTrip);
        tripList = (Button)findViewById(R.id.buttonTripList);
        wishList = (Button)findViewById(R.id.buttonWishList);

        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAccountActivity.this,CreateTripActivity.class);
                intent.putExtra(Temp1MY,user);
                startActivity(intent);
            }
        });


        getDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserAccountActivity.this,DealsActivity.class);
                startActivity(intent);

            }
        });



    }
}
