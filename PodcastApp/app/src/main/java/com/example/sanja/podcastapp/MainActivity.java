package com.example.sanja.podcastapp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FetchDataAsync.IData , EpisodeAdapter.ItemClickListener{

    final String data = "episode_object";

    ProgressBar pb;
    SeekBar seekBar;
    ImageButton play_pause_btn;
    RecyclerView recyclerView;
    MediaPlayer mediaPlayer;
    Handler mHandler1;
    boolean check = true;
    boolean list_view =true;
    Episodes episode;

    ArrayList<Episodes> episodesArrayList = new ArrayList<>();
    EpisodeAdapter adapter;
    String audio_url1;
    String duration1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        play_pause_btn = (ImageButton)findViewById(R.id.imageButton2);
        play_pause_btn.setVisibility(View.INVISIBLE);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setProgress(0);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        new FetchDataAsync(this).execute("https://www.npr.org/rss/podcast.php?id=510298");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(check){
                    try {
                        mediaPlayer.setDataSource(episode.getAudio_url());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        check =false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(mediaPlayer.isPlaying() && play_pause_btn.getTag().equals("pause")){
                    play_pause_btn.setImageResource(R.drawable.playbutton);
                    play_pause_btn.setTag("play");
                    mediaPlayer.pause();
                }
                else if (!mediaPlayer.isPlaying() && play_pause_btn.getTag().equals("play")){
                    play_pause_btn.setImageResource(R.drawable.pausebutton);
                    play_pause_btn.setTag("pause");
                    Log.d("demo","ended");
                    mediaPlayer.start();
                }
                /*else if (!mediaPlayer.isPlaying() && seekBar.getMax() ==seekBar.getProgress()){
                    play_pause_btn.setImageResource(R.drawable.pausebutton);
                    play_pause_btn.setTag("pause");
                    seekBar.setProgress(0);
                    Log.d("demo","ended");
                    mediaPlayer.start();
                }*/

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.refresh:
                if(list_view){


                    adapter = new EpisodeAdapter( episodesArrayList , this);
                    recyclerView.setAdapter(adapter);
                    adapter.setItemClickListener(this);

                }
                else{

                }

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
            seekBar.setProgress(0);
            seekBar.setVisibility(View.INVISIBLE);
            play_pause_btn.setVisibility(View.INVISIBLE);
            play_pause_btn.setImageResource(R.drawable.pausebutton);
        }
    }

    @Override
    public void setUpData(ArrayList<Episodes> episodes) {
        episodesArrayList = episodes;
        if(episodesArrayList.size()==0){
            Toast.makeText(getApplicationContext(),"Try again",Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d("demo","Success!!");
            Log.d("demo","episodesArrayList in MAIN...."+episodesArrayList.toString());
            pb.setVisibility(View.INVISIBLE);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new EpisodeAdapter( episodesArrayList , this);
            recyclerView.setAdapter(adapter);
            adapter.setItemClickListener(this);
           /* listView.setAdapter(adapter);
            adapter.setNotifyOnChange(true);*/
        }
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void onOtherItemClick(int position) {
        Log.d("demo","view clicked");
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
            seekBar.setProgress(0);
            seekBar.setVisibility(View.INVISIBLE);
            play_pause_btn.setVisibility(View.INVISIBLE);
            play_pause_btn.setImageResource(R.drawable.pausebutton);
        }
        Episodes episode = episodesArrayList.get(position);

        Intent intent = new Intent(this , PlayActivity.class);
        intent.putExtra(data , episode);
        startActivity(intent);
    }

    @Override
    public void onPlayButtonClick(int postion) {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        Log.d("demo","play clicked");
        seekBar.setVisibility(View.VISIBLE);
        play_pause_btn.setVisibility(View.VISIBLE);
        play_pause_btn.setImageResource(R.drawable.pausebutton);
        play_pause_btn.setTag("pause");
        episode = episodesArrayList.get(postion);
        mHandler1 = new Handler();

        if(episode.getAudio_url()!=null && episode.getAudio_url().length()!=0){
            audio_url1 = episode.getAudio_url();
        }
        else{
            audio_url1 = "";
        }
        if(episode.getDuration()!=null && episode.getDuration().length()!=0){
            duration1 = episode.getDuration();
            seekBar.setMax(Integer.parseInt(duration1));
        }
        else{
            duration1 = "";
        }

        try {
            mediaPlayer.setDataSource(episode.getAudio_url());
            mediaPlayer.prepare();
            mediaPlayer.start();
            check =false;
        } catch (IOException e) {
            e.printStackTrace();
        }


        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                        /*if(seekBar.getMax() == Integer.parseInt(episode.getDuration())){
                            play_btn.setTag("play");
                            play_btn.setImageResource(R.drawable.playbutton);
                            mediaPlayer.pause();

                        }*/
                if (!mediaPlayer.isPlaying() && seekBar.getProgress()==seekBar.getMax()){
                    play_pause_btn.setTag("play");
                    play_pause_btn.setImageResource(R.drawable.playbutton);

                    /*mediaPlayer.reset();*/
                    /*check =true;*/
                }
                mHandler1.postDelayed(this, 1000);
            }
        });




    }
}
