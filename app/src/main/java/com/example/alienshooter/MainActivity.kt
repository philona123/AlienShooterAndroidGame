package com.example.alienshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new AlienShooter(this));
        MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.music);
        music.setLooping(true);
        music.setVolume(100,100);
        music.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.music);
//        music.setLooping(true);
        music.setVolume(100,100);
        music.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }




}