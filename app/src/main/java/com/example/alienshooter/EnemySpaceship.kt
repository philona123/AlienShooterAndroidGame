package com.example.alienshooter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

class EnemySpaceship(var context: Context) {
    var enemySpaceship: Bitmap
    var ex: Int
    var ey: Int
    var enemyVelocity: Int
    var random: Random

    init {
        enemySpaceship = BitmapFactory.decodeResource(context.resources, R.drawable.rocket2)
        random = Random()
        ex = 200 + random.nextInt(400)
        ey = 0
        enemyVelocity = 14 + random.nextInt(10)
    }

    val enemySpaceshipWidth: Int
        get() = enemySpaceship.width
    val enemySpaceshipHeight: Int
        get() = enemySpaceship.height
}