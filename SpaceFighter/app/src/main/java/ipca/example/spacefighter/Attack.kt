package ipca.example.spacefighter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import java.util.Random

class Attack {
    var x = 0
    var y = 0
    var maxX = 0
    var maxY = 0
    var minX = 0
    var minY = 0

    var bitmap : Bitmap
    var boosting = false

    val generator = Random()

    var detectCollision : Rect

    constructor(context: Context, width: Int, height: Int){
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ray)

        minX = 0
        maxX = width

        maxY = height - bitmap.height
        minY = 0

        x = maxX
        y = generator.nextInt(maxY)

        detectCollision = Rect(x, y, bitmap.width, bitmap.height)
    }

    fun update() {

        if (x < minX - bitmap.width){
            x = maxX
            y = generator.nextInt(maxY)
        }

        detectCollision.left = x
        detectCollision.top = y
        detectCollision.right = x + bitmap.width
        detectCollision.bottom = y + bitmap.height
    }
}