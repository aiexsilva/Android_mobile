package ipca.example.spacefighter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.File
import java.nio.charset.Charset

class GameView : SurfaceView, Runnable {

    var playing = false
    var gameThread : Thread? = null
    lateinit var surfaceHolder : SurfaceHolder
    lateinit var canvas : Canvas

    lateinit var paint :Paint
    var stars = arrayListOf<Star>()
    var enemies = arrayListOf<Enemy>()
    var coins = arrayListOf<Coin>()
    var attack = arrayListOf<Attack>()
    lateinit var player : Player
    lateinit var boom : Boom
    lateinit var warrior : Warrior
    lateinit var enemy: Enemy

    var lives = 5
    var score = 0

    var onGameOver : () -> Unit = {}

    //function to read txt files
    fun getHighscoreFile(context: Context): File {
        return File(context.filesDir, "highscore.txt")
    }

    fun readText(context: Context): String {
        val file = getHighscoreFile(context)
        return if (file.exists()) file.readText(Charset.defaultCharset()) else "0"
    }

    fun writeText(context: Context, value: Any) {
        val file = getHighscoreFile(context)
        file.writeText(value.toString())
    }

    private fun init(context: Context, width: Int, height: Int){

        surfaceHolder = holder
        paint = Paint()

        val highscoreFile = getHighscoreFile(context)
        if (!highscoreFile.exists()) highscoreFile.writeText("0")

        for (i in 0..100){
            stars.add(Star(width, height))
        }

        for(i in 0..4){
            coins.add(Coin(context,width,height))
        }

        for (i in 0..2){
            enemies.add(Enemy(context,width, height))
        }

        player = Player(context, width, height)
        warrior = Warrior(context, width, height)
        boom = Boom(context, width, height)

    }

    constructor(context: Context?, width: Int, height: Int) : super(context) {
        init(context!!, width, height)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init(context!!, 0, 0)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context!!, 0, 0)
    }

    fun resume() {
        playing = true
        gameThread = Thread(this)
        gameThread?.start()
    }


    override fun run() {
        while (playing){
            update()
            draw()
            control()
        }
    }

    fun update(){

        boom.x = -300
        boom.y = -300

        for (s in stars){
            s.update(player.speed)
        }

        for (c in coins){
            c.update(player.speed)
            if (Rect.intersects(player.detectCollision, c.detectCollision)) {

                c.x = -300

                score += 15
                player.speed += 10

            }
        }

        for (e in enemies){
            e.update(player.speed)
            if (Rect.intersects(player.detectCollision, e.detectCollision)) {


                boom.x = e.x
                boom.y = e.y

                e.x = -300

                lives -= 1


            }

        }

        for (a in attack){
            a.update()
            if (Rect.intersects(a.detectCollision, enemy.detectCollision)) {


                boom.x = enemy.x
                boom.y = enemy.y

                a.x = -300
                enemy.x = -300

            }

        }

        player.update()
        warrior.update()
    }

    fun draw(){
        if (surfaceHolder.surface.isValid){
            canvas = surfaceHolder.lockCanvas()

            canvas.drawColor(Color.BLACK)

            paint.color = Color.YELLOW

            for (star in stars) {
                paint.strokeWidth = star.starWidth.toFloat()
                canvas.drawPoint(star.x.toFloat(), star.y.toFloat(), paint)
            }

            for (coin in coins){
                canvas.drawBitmap(coin.bitmap, coin.x.toFloat(), coin.y.toFloat(), paint)
            }

            canvas.drawBitmap(player.bitmap, player.x.toFloat(), player.y.toFloat(), paint)

            for (e in enemies) {
                canvas.drawBitmap(e.bitmap, e.x.toFloat(), e.y.toFloat(), paint)
            }
            canvas.drawBitmap(boom.bitmap, boom.x.toFloat(), boom.y.toFloat(), paint)

            //canvas.drawBitmap(warrior.bitmap, warrior.x.toFloat(), warrior.y.toFloat(), paint)

            paint.textSize = 42f
            canvas.drawText("Lives: $lives", 10f, 100f, paint)
            canvas.drawText("Score: $score", 10f, 200f, paint)

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    var callGameOverOnce = false
    fun control(){
        Thread.sleep(17)
        if (lives == 0 ){
            playing = false
            val currentHighscore = readText(context).toInt()
            if(score > currentHighscore){
                writeText(context, score)
            }
            Handler(Looper.getMainLooper()).post {
                if (!callGameOverOnce) {
                    onGameOver()
                    callGameOverOnce = true
                }
                gameThread?.join()
            }
        }
    }

    private var activePointers = 0

    private val touchX = FloatArray(10) { -1f } // Store X coordinates of touch points (up to 10 fingers)
    private val touchY = FloatArray(10) { -1f }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                player.boosting = true
                warrior.x = event.x.toInt()
                warrior.y = event.y.toInt()
            }
            MotionEvent.ACTION_UP -> {
                player.boosting = false
            }
            MotionEvent.ACTION_MOVE -> {
                warrior.x = event.x.toInt()
                warrior.y = event.y.toInt()
            }
        }

        activePointers = event?.pointerCount?:0

        // Process each touch point
        for (i in 0 until activePointers) {
            val pointerId = event?.getPointerId(i)?:0
            touchX[pointerId] = event?.getX(i)?:-1f
            touchY[pointerId] = event?.getY(i)?:-1f
        }

        // Handle touch actions
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                // A new finger touched the screen
                for (a in attack) {
                    canvas.drawBitmap(a.bitmap, a.x.toFloat(), a.y.toFloat(), paint)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                // Handle movement of each finger
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                // A finger was lifted up
                val pointerIndex = event.actionIndex
                val pointerId = event.getPointerId(pointerIndex)
                touchX[pointerId] = -1f
                touchY[pointerId] = -1f
            }
            MotionEvent.ACTION_CANCEL -> {
                // Reset all points on cancel
                for (i in touchX.indices) {
                    touchX[i] = -1f
                    touchY[i] = -1f
                }
            }
        }


        return true
    }

}