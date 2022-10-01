package com.example.alienshooter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

class OurSpaceship(var context: Context) {
    var ourSpaceship: Bitmap
    var ox: Int
    var oy: Int
    var random: Random

    init {
        ourSpaceship = BitmapFactory.decodeResource(context.resources, R.drawable.rocket1)
        random = Random()
        ox = random.nextInt(AlienShooter.screenWidth)
        oy = AlienShooter.screenHeight - ourSpaceship.height
    }

    val ourSpaceshipWidth: Int
        get() = ourSpaceship.width
}