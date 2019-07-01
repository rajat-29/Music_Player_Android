package com.example.music_player;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayerActivity extends AppCompatActivity {

    Button btn_next,btn_prev,btn_pause;
    TextView songTextLabel;
    SeekBar songSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_next = (Button)findViewById(R.id.next);
        btn_prev = (Button)findViewById(R.id.prev);
        btn_pause = (Button)findViewById(R.id.pause);

        songTextLabel = (TextView)findViewById(R.id.songLabel);

        songSeekbar = (SeekBar)findViewById(R.id.seekBar);
    }
}
