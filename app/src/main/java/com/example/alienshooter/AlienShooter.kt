package com.example.alienshooter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import java.util.*

class AlienShooter
    (context: Context) : View(
    context
) {
    var background: Bitmap
    var lifeImage: Bitmap
    //var handler: Handler?
    var UPDATE_MILLIS: Long = 30
    var points = 0
    var life = 3
    var scorePaint: Paint
    var TEXT_SIZE = 80
    var paused = false
    var ourSpaceship: OurSpaceship
    var enemySpaceship: EnemySpaceship
    var random: Random
    var enemyShots: ArrayList<Shot>
    var ourShots: ArrayList<Shot>
    var explosion: Explosion? = null
    var explosions: ArrayList<Explosion>
    var enemyShotAction = false
    val runnable = Runnable { invalidate() }

    init {
        val display = (getContext() as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        screenHeight = size.y
        random = Random()
        enemyShots = ArrayList()
        ourShots = ArrayList()
        explosions = ArrayList()
        ourSpaceship = OurSpaceship(context)
        enemySpaceship = EnemySpaceship(context)
        //handler = Handler(Looper.getMainLooper())
        background = BitmapFactory.decodeResource(context.resources, R.drawable.background)
        lifeImage = BitmapFactory.decodeResource(context.resources, R.drawable.life)
        scorePaint = Paint()
        scorePaint.color = Color.RED
        scorePaint.textSize = TEXT_SIZE.toFloat()
        scorePaint.textAlign = Paint.Align.LEFT
    }

    override fun onDraw(canvas: Canvas) {
        // Draw background, Points and life on Canvas
        canvas.drawBitmap(background, 0f, 0f, null)
        canvas.drawText("POINTS: $points", 0f, TEXT_SIZE.toFloat(), scorePaint)
        for (i in life downTo 1) {
            canvas.drawBitmap(lifeImage, (screenWidth - lifeImage.width * i).toFloat(), 0f, null)
        }
        // When life becomes 0, stop game and launch GameOver Activity with points
        if (life == 0) {
            paused = true
            //handler = null
            val intent = Intent(context, GameOver::class.java)
            intent.putExtra("points", points)
            context.startActivity(intent)
            (context as Activity).finish()
        }
        // Move enemySpaceship
        enemySpaceship.ex += enemySpaceship.enemyVelocity
        // If enemySpaceship collides with right wall, reverse enemyVelocity
        if (enemySpaceship.ex + enemySpaceship.enemySpaceshipWidth >= screenWidth) {
            enemySpaceship.enemyVelocity *= -1
        }
        // If enemySpaceship collides with left wall, again reverse enemyVelocity
        if (enemySpaceship.ex <= 0) {
            enemySpaceship.enemyVelocity *= -1
        }
        // Till enemyShotAction is false, enemy should fire shots from random travelled distance
        if (enemyShotAction == false) {
            if (enemySpaceship.ex >= 200 + random.nextInt(400)) {
                val enemyShot = Shot(
                    context,
                    enemySpaceship.ex + enemySpaceship.enemySpaceshipWidth / 2,
                    enemySpaceship.ey
                )
                enemyShots.add(enemyShot)
                // We're making enemyShotAction to true so that enemy can take a short at a time
                enemyShotAction = true
            }
            enemyShotAction = if (enemySpaceship.ex >= 400 + random.nextInt(800)) {
                val enemyShot = Shot(
                    context,
                    enemySpaceship.ex + enemySpaceship.enemySpaceshipWidth / 2,
                    enemySpaceship.ey
                )
                enemyShots.add(enemyShot)
                // We're making enemyShotAction to true so that enemy can take a short at a time
                true
            } else {
                val enemyShot = Shot(
                    context,
                    enemySpaceship.ex + enemySpaceship.enemySpaceshipWidth / 2,
                    enemySpaceship.ey
                )
                enemyShots.add(enemyShot)
                // We're making enemyShotAction to true so that enemy can take a short at a time
                true
            }
        }
        // Draw the enemy Spaceship
        canvas.drawBitmap(
            enemySpaceship.enemySpaceship,
            enemySpaceship.ex.toFloat(),
            enemySpaceship.ey.toFloat(),
            null
        )
        // Draw our spaceship between the left and right edge of the screen
        if (ourSpaceship.ox > screenWidth - ourSpaceship.ourSpaceshipWidth) {
            ourSpaceship.ox = screenWidth - ourSpaceship.ourSpaceshipWidth
        } else if (ourSpaceship.ox < 0) {
            ourSpaceship.ox = 0
        }
        // Draw our Spaceship
        canvas.drawBitmap(
            ourSpaceship.ourSpaceship,
            ourSpaceship.ox.toFloat(),
            ourSpaceship.oy.toFloat(),
            null
        )
        // Draw the enemy shot downwards our spaceship and if it's being hit, decrement life, remove
        // the shot object from enemyShots ArrayList and show an explosion.
        // Else if, it goes away through the bottom edge of the screen also remove
        // the shot object from enemyShots.
        // When there is no enemyShots no the screen, change enemyShotAction to false, so that enemy
        // can shot.
        for (i in enemyShots.indices) {
            enemyShots[i].shy += 15
            canvas.drawBitmap(
                enemyShots[i].shot,
                enemyShots[i].shx.toFloat(),
                enemyShots[i].shy.toFloat(),
                null
            )
            if (enemyShots[i].shx >= ourSpaceship.ox
                && enemyShots[i].shx <= ourSpaceship.ox + ourSpaceship.ourSpaceshipWidth && enemyShots[i].shy >= ourSpaceship.oy && enemyShots[i].shy <= screenHeight
            ) {
                life--
                enemyShots.removeAt(i)
                explosion = Explosion(context, ourSpaceship.ox, ourSpaceship.oy)
                explosions.add(explosion!!)
            } else if (enemyShots[i].shy >= screenHeight) {
                enemyShots.removeAt(i)
            }
            if (enemyShots.size < 1) {
                enemyShotAction = false
            }
        }
        // Draw our spaceship shots towards the enemy. If there is a collision between our shot and enemy
        // spaceship, increment points, remove the shot from ourShots and create a new Explosion object.
        // Else if, our shot goes away through the top edge of the screen also remove
        // the shot object from enemyShots ArrayList.
        for (i in ourShots.indices) {
            ourShots[i].shy -= 15
            canvas.drawBitmap(
                ourShots[i].shot,
                ourShots[i].shx.toFloat(),
                ourShots[i].shy.toFloat(),
                null
            )
            if (ourShots[i].shx >= enemySpaceship.ex
                && ourShots[i].shx <= enemySpaceship.ex + enemySpaceship.enemySpaceshipWidth && ourShots[i].shy <= enemySpaceship.enemySpaceshipWidth && ourShots[i].shy >= enemySpaceship.ey
            ) {
                points++
                ourShots.removeAt(i)
                explosion = Explosion(context, enemySpaceship.ex, enemySpaceship.ey)
                explosions.add(explosion!!)
            } else if (ourShots[i].shy <= 0) {
                ourShots.removeAt(i)
            }
        }
        // Do the explosion
        for (i in explosions.indices) {
            canvas.drawBitmap(
                explosions[i].getExplosion(explosions[i].explosionFrame)!!,
                explosions[i].eX.toFloat(),
                explosions[i].eY.toFloat(),
                null
            )
            explosions[i].explosionFrame++
            if (explosions[i].explosionFrame > 8) {
                explosions.removeAt(i)
            }
        }
        // If not paused, we’ll call the postDelayed() method on handler object which will cause the
        // run method inside Runnable to be executed after 30 milliseconds, that is the value inside
        // UPDATE_MILLIS.
        if (!paused) handler!!.postDelayed(runnable, UPDATE_MILLIS)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x.toInt()
        // When event.getAction() is MotionEvent.ACTION_UP, if ourShots arraylist size < 1,
        // create a new Shot.
        // This way we restrict ourselves of making just one shot at a time, on the screen.
        if (event.action == MotionEvent.ACTION_UP) {
            if (ourShots.size < 1) {
                val ourShot = Shot(
                    context,
                    ourSpaceship.ox + ourSpaceship.ourSpaceshipWidth / 2,
                    ourSpaceship.oy
                )
                ourShots.add(ourShot)
            }
        }
        // When event.getAction() is MotionEvent.ACTION_DOWN, control ourSpaceship
        if (event.action == MotionEvent.ACTION_DOWN) {
            ourSpaceship.ox = touchX
        }
        // When event.getAction() is MotionEvent.ACTION_MOVE, control ourSpaceship
        // along with the touch.
        if (event.action == MotionEvent.ACTION_MOVE) {
            ourSpaceship.ox = touchX
        }
        // Returning true in an onTouchEvent() tells Android system that you already handled
        // the touch event and no further handling is required.
        return true
    }

    companion object {
        var screenWidth: Int = 0
        var screenHeight: Int = 0
    }
}