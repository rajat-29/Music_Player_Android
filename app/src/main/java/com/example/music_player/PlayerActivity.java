package com.example.music_player;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static android.graphics.PorterDuff.Mode.SRC_IN;

public class PlayerActivity extends AppCompatActivity {

    Button btn_next,btn_prev,btn_pause;
    TextView songTextLabel,positionStart,positionEnd;
    SeekBar songSeekbar;

    static MediaPlayer myMediaPlayer;
    int position;
    String sname;

    ArrayList<File> mySongs;
    Thread updateseekBar;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_next = (Button)findViewById(R.id.next);
        btn_prev = (Button)findViewById(R.id.prev);
        btn_pause = (Button)findViewById(R.id.pause);

        songTextLabel = (TextView)findViewById(R.id.songLabel);

        songSeekbar = (SeekBar)findViewById(R.id.seekBar);

        positionEnd=(TextView)findViewById(R.id.positionEnd);
        positionStart=(TextView) findViewById(R.id.positionStart);

        //change title bar an back button
        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // thread to update seek bar
        updateseekBar = new Thread()
        {
            @Override
            public void run() {

                int totalDuration = myMediaPlayer.getDuration();

                long minutes = TimeUnit.MILLISECONDS.toMinutes(totalDuration);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(totalDuration);
                seconds=seconds-60*minutes;
                String dur;
                if(minutes==0)
                    dur="00:"+String.valueOf(seconds);
                else
                    dur=String.valueOf(minutes)+":"+String.valueOf(seconds);
                positionEnd.setText(String.valueOf(dur));

                int currentPosition = 0;

                while(currentPosition<totalDuration)
                {
                    try
                    {
                        sleep(1000);
                        currentPosition = myMediaPlayer.getCurrentPosition();
                        songSeekbar.setProgress(currentPosition);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        };

        if(myMediaPlayer!=null)
        {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        // to extract putExtra arrayList and song Name
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");

        sname = mySongs.get(position).getName().toString();

        String songName = i.getStringExtra("songname");

        songTextLabel.setText(songName);
        songTextLabel.setSelected(true);

        position = bundle.getInt("pos",0);

        Uri u = Uri.parse(mySongs.get(position).toString());

        myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

        myMediaPlayer.start();
        songSeekbar.setMax(myMediaPlayer.getDuration());

        updateseekBar.start();

        songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songSeekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), SRC_IN);

        // seekbar updations
        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                long minutes = TimeUnit.MILLISECONDS.toMinutes(progress);
                progress=progress-(int)minutes*60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(progress);
                seconds=seconds-60*minutes;
                String dur;
                if(minutes==0)
                    dur="0:"+String.valueOf(seconds);
                else
                    dur=String.valueOf(minutes)+":"+String.valueOf(seconds);


                positionStart.setText(dur);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        //pause button method
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                songSeekbar.setMax(myMediaPlayer.getDuration());

                if(myMediaPlayer.isPlaying())
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();
                }
                else
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();
                }
            }
        });

        // next button method
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position+1)%mySongs.size());

                Uri u = Uri.parse(mySongs.get(position).toString());

                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                sname = mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);

                myMediaPlayer.start();

            }
        });

        //prev button method
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position-1)<0)?(mySongs.size()-1):(position-1);

                Uri u = Uri.parse(mySongs.get(position).toString());

                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                sname = mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);

                myMediaPlayer.start();
            }
        });
    }

    // back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
