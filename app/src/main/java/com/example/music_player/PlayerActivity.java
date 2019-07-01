package com.example.music_player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button btn_next,btn_prev,btn_pause;
    TextView songTextLabel;
    SeekBar songSeekbar;

    static MediaPlayer myMediaPlayer;
    int position;
    String sname;

    ArrayList<File> mySongs;
    Thread updateseekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_next = (Button)findViewById(R.id.next);
        btn_prev = (Button)findViewById(R.id.prev);
        btn_pause = (Button)findViewById(R.id.pause);

        songTextLabel = (TextView)findViewById(R.id.songLabel);

        songSeekbar = (SeekBar)findViewById(R.id.seekBar);

        updateseekBar = new Thread()
        {
            @Override
            public void run() {

                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition = 0;

                while(currentPosition<totalDuration)
                {
                    try
                    {
                        sleep(500);
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

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");

        sname = mySongs.get(position).getName().toString();

        String songName = i.getStringExtra("songname");

        songTextLabel.setText(songName);
        songTextLabel.setSelected(true);
    }
}
