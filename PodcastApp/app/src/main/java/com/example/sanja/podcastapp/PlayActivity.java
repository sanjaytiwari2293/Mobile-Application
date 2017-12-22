package com.example.sanja.podcastapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PlayActivity extends AppCompatActivity {

    final String data = "episode_object";
    String audio_url;
    Episodes episode;
    TextView title;
    ImageView image;
    TextView desc;
    TextView pub_date;
    TextView duration;
    ImageButton play_btn;
    SeekBar seekBar;

    MediaPlayer mediaPlayer;
    Handler mHandler;

    boolean check = true;
    int length;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        episode = (Episodes) getIntent().getSerializableExtra(data);
        Log.d("demo","play activity  "+episode.toString());

        title = (TextView)findViewById(R.id.textViewPlayTitle);
        image = (ImageView)findViewById(R.id.imageViewPlayImg);
        desc = (TextView)findViewById(R.id.textViewPlayDesc);
        pub_date = (TextView)findViewById(R.id.textViewPlayPubDate);
        duration = (TextView)findViewById(R.id.textViewPlayDur);
        play_btn = (ImageButton)findViewById(R.id.imageButtonPlay);
        play_btn.setTag("play");
        seekBar = (SeekBar)findViewById(R.id.seekBarPlay);

        if(episode.getTitle()!=null  && episode.getTitle().length() !=0 ) {
            title.setText(episode.getTitle());
        }else{
            title.setText("");
        }
        String img_url = episode.getImg_url();
        if(img_url!=null  && img_url.length() !=0 ) {
            Picasso.with(this).load(img_url).into(image);
        }else{
            image.setImageResource(R.drawable.refreshbutton);
        }
        if(episode.getDescription()!=null && episode.getDescription().length()!=0) {
            desc.setText(episode.getDescription());
        }
        else{
            desc.setText("");
        }
        if(episode.getRel_date()!=null && episode.getRel_date().length()!=0){
            pub_date.setText(episode.getRel_date());
        }
        else{
            pub_date.setText("");
        }
        if(episode.getDuration()!=null && episode.getDuration().length()!=0){
            duration.setText(episode.getDuration());
            seekBar.setMax(Integer.parseInt(episode.getDuration()));
        }
        else{
            duration.setText("");
            seekBar.setMax(0);
        }

        if(episode.getAudio_url()!=null && episode.getAudio_url().length()!=0){
            audio_url = episode.getAudio_url();
        }
        else{
            audio_url ="";
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setProgress(0);

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

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(check){
                    try {
                        mediaPlayer.setDataSource(episode.getAudio_url());
                        mediaPlayer.prepare();
                        check =false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(play_btn.getTag().equals("play") && !mediaPlayer.isPlaying()){
                    play_btn.setTag("pause");
                    play_btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    play_btn.setImageResource(R.drawable.pausebutton);

                    mediaPlayer.start();

                }
                else if(play_btn.getTag().equals("pause") && mediaPlayer.isPlaying()){

                    play_btn.setTag("play");
                    play_btn.setImageResource(R.drawable.playbutton);
                    mediaPlayer.pause();

                }
                else if (play_btn.getTag().equals("pause") && !mediaPlayer.isPlaying()){
                    play_btn.setTag("play");
                    Log.d("demo","ENDED");
                    play_btn.setImageResource(R.drawable.playbutton);
                    seekBar.setProgress(0);
                }
                else{
                    Log.d("demo","button error");
                }

               // mediaPlayer.start();
                seekBar.setVisibility(View.VISIBLE);
                mHandler = new Handler();
//Make sure you update Seekbar on UI thread
                PlayActivity.this.runOnUiThread(new Runnable() {

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
                            play_btn.setTag("play");
                            play_btn.setImageResource(R.drawable.playbutton);

                            mediaPlayer.reset();
                            check =true;
                        }
                        mHandler.postDelayed(this, 1000);
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
       // mediaPlayer.release();
    }
}
