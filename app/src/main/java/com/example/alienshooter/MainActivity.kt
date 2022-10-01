package com.example.alienshooter

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(AlienShooter(this))
        val music = MediaPlayer.create(this@MainActivity, R.raw.music)
        music.isLooping = true
        music.setVolume(100f, 100f)
        music.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        val music = MediaPlayer.create(this@MainActivity, R.raw.music)
        //        music.setLooping(true);
        music.setVolume(100f, 100f)
        music.start()
    }

    override fun onPause() {
        super.onPause()
    }
}