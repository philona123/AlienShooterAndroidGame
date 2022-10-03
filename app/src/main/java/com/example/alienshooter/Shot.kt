package com.example.alienshooter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Shot(var context: Context, shx: Int, shy: Int) {
    var shot: Bitmap
    var shx: Int
    var shy: Int

    init {
        shot = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.shot
        )
        this.shx = shx
        this.shy = shy
    }

    val shotWidth: Int
        get() = shot.width
    val shotHeight: Int
        get() = shot.height
}