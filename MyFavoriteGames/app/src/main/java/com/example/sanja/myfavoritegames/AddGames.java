package com.example.sanja.myfavoritegames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddGames extends AppCompatActivity {

    String s_name;
    String s_des;
    String s_genre;
    int s_rating;
    String s_year;
    String s_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_games);

        final EditText name =(EditText)findViewById(R.id.editText);
        final EditText desc = (EditText)findViewById(R.id.editText2);
        final Spinner genre = (Spinner)findViewById(R.id.spinner);
        final SeekBar rating =  (SeekBar)findViewById(R.id.seekBar);

        rating.setProgress(0);
        rating.incrementProgressBy(1);
        rating.setMax(5);

        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress >= 0 && progress <= seekBar.getMax()) {

                        s_rating = progress;

                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final EditText year = (EditText)findViewById(R.id.editText3);
        final EditText url = (EditText)findViewById(R.id.editText4);


        Button add =  (Button)findViewById(R.id.button6);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s_name =  name.getText().toString();
                s_des =  desc.getText().toString();
                s_genre = (String) genre.getSelectedItem();

                s_year = year.getText().toString();
                s_url = url.getText().toString();


                if(s_name.length()==0 || s_des.length()==0 || s_year.length()==0 || s_url.length()==0 || s_genre.equals("Select One")){

                    Toast.makeText(getApplicationContext(),
                            "Invalid Inputs", Toast.LENGTH_LONG).show();

                } else{

                    Game game = new Game(s_name,s_des,s_genre,s_rating,s_year,s_url);
                    Intent i = new Intent();
                    i.putExtra(MainActivity.value_key,game);
                    setResult(RESULT_OK,i);
                    Log.d("demo","toString  "+game.toString());
                    finish();

                }




            }
        });




    }
}
