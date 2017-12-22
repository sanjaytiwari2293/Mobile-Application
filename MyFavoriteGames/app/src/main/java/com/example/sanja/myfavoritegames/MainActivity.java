package com.example.sanja.myfavoritegames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int req_code =100;
    public static final String value_key = "add";
    public ArrayList<Game> new_games = new ArrayList();
    public ArrayList<String> new_game_names = new ArrayList();

    String game_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button add= (Button)findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, AddGames.class);
                startActivityForResult(intent, req_code);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == req_code){

            if(resultCode == RESULT_OK){

                Game g = (Game) data.getExtras().getSerializable(value_key);
                new_games.add(g);
                game_name =  g.name;
                new_game_names.add(game_name);
                Log.d("demo","game  "+g.toString());
                Log.d("demo","game name  "+new_game_names);

            }
        }

    }
}
