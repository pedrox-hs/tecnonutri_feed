package br.com.pedrosilva.tecnonutri.features.common.widget

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader.TileMode
import com.squareup.picasso.Transformation

class CircleTransformation : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val size = source.width.coerceAtMost(source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }
        val bitmap = Bitmap.createBitmap(size, size, source.config)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            shader = BitmapShader(squaredBitmap, TileMode.CLAMP, TileMode.CLAMP)
            isAntiAlias = true
        }
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        squaredBitmap.recycle()
        return bitmap
    }

    override fun key() = "circle"
}