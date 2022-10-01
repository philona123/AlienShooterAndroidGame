package com.example.alienshooter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOver : AppCompatActivity() {
    var tvPoints: TextView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)
        val points = intent.extras!!.getInt("points")
        tvPoints = findViewById(R.id.tvPoints)
        tvPoints?.text = "" + points
    }

    fun restart(view: View?) {
        val intent = Intent(this@GameOver, StartUp::class.java)
        startActivity(intent)
        finish()
    }

    fun exit(view: View?) {
        finish()
    }
}