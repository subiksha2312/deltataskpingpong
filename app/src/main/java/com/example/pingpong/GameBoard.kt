package com.example.pingpong

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import java.lang.Thread.sleep
import java.util.logging.Handler
import kotlin.random.Random

/**
 * TODO: document your custom view class.
 */

@Volatile var gameOn = false

class GameBoard : View {

    private val paintGameBoard: Paint = Paint()
    private val paintBandP: Paint = Paint()
    private val paintScoreText: Paint = Paint()
    private val paintHighScoreText: Paint = Paint()
    private val paintSystemScoreText: Paint = Paint()
    private val paintEndCard: Paint = Paint()
    private val paintreplay: Paint = Paint()
    private val paintreplaytext: Paint = Paint()
    private val paintpaddle2: Paint = Paint()

    private var PADDLE_WIDTH = 200F
    private val PADDLE_HEIGHT = 50F
    private var cxPaddle = 0F
    private var cxTouch = 0F
    private var cxSystempaddle = 0F

    private var cxBall = 100F
    private var cyBall = 100F
    private var cRadius = 30F

    private var gameEnd = false

    private var mX = 5F
    private var mY = 5F

    private lateinit var tvscore: TextView
    private lateinit var tvhighscore: TextView
    private lateinit var tvSystemscore: TextView

    private var mWidth = 0
    private var mHeight = 0

    private var mAllTimeHighScore = 0
    private var score = 0
    private var systemscore = 0

    var level2: Boolean = true
    var level3: Boolean = true
    var level4: Boolean = true
    var moveright: Boolean = true

    var mMode: String = ""
    var randomint: Int = 0

    private var xpos: Int =0
    private var ypos: Int =0

    lateinit var hsPref: SharedPreferences


    constructor (context: Context) : super(context) {

        Log.d("constructor1", "constructor1")
        init(null, context)

    }

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        Log.d("constructor2", "constructor2")
        init(attrs, context)


    }

    private fun init(attrs: AttributeSet?, context: Context) {

        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.GameBoard, 0, 0)
        mMode = attributeArray.getString(R.styleable.GameBoard_gameMode) ?: "easy"


        paintGameBoard.color = Color.BLACK
        paintBandP.color = Color.WHITE
        paintScoreText.color = Color.WHITE
        paintHighScoreText.color = Color.DKGRAY
        paintSystemScoreText.color = Color.WHITE
        paintEndCard.color = Color.DKGRAY
        paintreplay.color = Color.BLACK
        paintreplaytext.color = Color.WHITE
        paintpaddle2.color = Color.BLUE


        paintGameBoard.style = Paint.Style.FILL
        paintBandP.style = Paint.Style.FILL
        paintEndCard.style = Paint.Style.FILL
        paintpaddle2.style = Paint.Style.FILL

        paintScoreText.style = Paint.Style.STROKE
        paintScoreText.textSize = 100F
        paintSystemScoreText.textSize = 100F
        paintHighScoreText.style = Paint.Style.FILL_AND_STROKE
        paintHighScoreText.textSize = 100F
        paintreplaytext.textSize = 50F




        hsPref =
            context.getSharedPreferences((R.string.HighScoreKey).toString(), Context.MODE_PRIVATE)
        if (hsPref.contains((R.string.HighScore).toString()) == false) {
            with(hsPref.edit()) {
                putInt((R.string.HighScore).toString(), 0)
                apply()
                commit()
            }
        }

        mAllTimeHighScore = hsPref.getInt((R.string.HighScore).toString(), 0)

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = width
        mHeight = height
        cxBall = (Random.nextInt(1, mWidth)).toFloat()
        cyBall = (mHeight / 6).toFloat()
        cxPaddle = (width / 2 - PADDLE_WIDTH / 2).toFloat()
        cxSystempaddle = (width / 2 - PADDLE_WIDTH / 2).toFloat()
        paintScoreText.textSize = height / 20f
        paintHighScoreText.textSize = height / 20f


        paintScoreText.textAlign = Paint.Align.CENTER
         xpos =(width / 2)
         ypos = ( ((height / 2) - ((paintScoreText.descent() + paintScoreText.ascent()) / 2))-230f).toInt()

        paintreplaytext.textAlign = Paint.Align.CENTER
        paintreplay.textAlign = Paint.Align.CENTER


        Log.d("onsizechanged", "starting thread")
        start()


    }

    override fun onDraw(canvas: Canvas) {

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintGameBoard)
        Log.d("mmode value", "mmode value = $mMode")

        if (mMode == "hard" || mMode == "intermediate") {

            canvas?.drawRect(
                cxSystempaddle,
                0f,
                cxSystempaddle + PADDLE_WIDTH,
                PADDLE_HEIGHT, paintpaddle2
            )

            canvas?.drawRect(
                cxPaddle,
                (height - PADDLE_HEIGHT).toFloat(),
                cxPaddle + PADDLE_WIDTH,
                height.toFloat(), paintBandP
            )

            canvas?.drawCircle(cxBall, cyBall, cRadius, paintBandP)
        }
        else {
            canvas?.drawRect(
                cxPaddle,
                (height - PADDLE_HEIGHT).toFloat(),
                cxPaddle + PADDLE_WIDTH,
                height.toFloat(), paintBandP
            )

            canvas?.drawCircle(cxBall, cyBall, cRadius, paintBandP)
           
        }


        if (gameEnd == true) {

            canvas?.drawRect(
                0f,
                (height / 3).toFloat(),
                width.toFloat(),
                ((height / 3) + 300).toFloat(),
                paintEndCard
            )

            canvas?.drawRect(
                ((width / 2)-200f).toFloat(),
                ((height / 3) + 150).toFloat(),
                ((width / 2) + 200).toFloat(),
                ((height / 3) + 225).toFloat(),
                /*
                (width / 2).toFloat(),
                ((height / 3) + 150).toFloat(),
                ((width / 2) + 180).toFloat(),
                ((height / 3) + 225).toFloat(),

                 */
                paintreplay
            )

            canvas?.drawText(
                "Replay!",
                ((width / 2) + 10).toFloat(),
                ((height / 3) + 200).toFloat(),
                paintreplaytext
            )

           if(mMode == "easy") {
               canvas?.drawText(
                   "Your score is: $score",
                   (xpos).toFloat(),
                   (ypos).toFloat(),
                   paintScoreText
               )
           }
           if (mMode == "hard") {
               canvas?.drawText(
                   "You lose ://",
                   (xpos).toFloat(),
                   (ypos).toFloat(),
                   paintScoreText
               )
           }
            else if (mMode == "intermediate"){
                if(score == systemscore){
                    canvas?.drawText(
                        "You lose ://",
                        (xpos).toFloat(),
                        (ypos).toFloat(),
                        paintScoreText
                    )
                }
               else {
                    canvas?.drawText(
                        "You win :))",
                        (xpos).toFloat(),
                        (ypos).toFloat(),
                        paintScoreText
                    )

                }
           }

        }
        super.onDraw(canvas)

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (gameEnd == false) {
                    cxTouch = event.x
                } else {
                    if (checkPointInReplayRect(event.x, event.y) == true) {
                        gameEnd = false
                        resetGame()
                        start()
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (gameEnd == false) {
                    movePaddle(event)
                }


            }
        }

        return true
    }


    fun checkPointInReplayRect(x: Float, y: Float): Boolean {
        if (x > ((width / 2)-200f).toFloat() &&
            x < ((width / 2) + 150).toFloat() &&
            y > ((height / 3) + 200).toFloat() &&
            y < ((height / 3) + 225).toFloat()
        ) {

            return true
        }
        return false
    }

    private fun movePaddle(event: MotionEvent) {
        cxPaddle = cxPaddle - (cxTouch - event.x)
        cxTouch = event.x

        if (cxPaddle < 0) {
            cxPaddle = 0F
        } else if (cxPaddle > width - PADDLE_WIDTH) {
            cxPaddle = width - PADDLE_WIDTH
        }
        invalidate()

    }

    fun start() {
        gameOn = true
        GameThread().start()
    }

    fun stop() {
        gameOn = false
    }


    fun resetGame() {

        if (score > mAllTimeHighScore) {
            mAllTimeHighScore = score
            with(hsPref.edit()) {
                putInt((R.string.HighScore).toString(), mAllTimeHighScore)
                apply()
            }

        }
        cxBall = (Random.nextInt(1, mWidth)).toFloat()
        cyBall = (mHeight / 6).toFloat()
        mX = 5F
        mY = 5F
        score = 0
        systemscore = 0
        if (mMode == "hard") {
            tvSystemscore.setText("$systemscore")
        }
        tvscore.setText("$score")
        invalidate()
    }

    fun playpingpongHitSound() {
        soundPoolBallHit.play(pingponghitSound, 1f, 1f, 4, 0, 2f)
    }

    fun playpingpongFailSound() {
        soundPoolFailure.play(failureSound, 1f, 1f, 1, 0, 2f)
    }

    fun increaseVelocity() {
        when {
            score in 6..11 -> {
                if (level2) {
                    mX *= 1.5f
                    mY *= 1.5f
                    level2 = false
                }
            }
            score in 12..15 -> {
                if (level3) {
                    mX *= 2.0f
                    mY *= 2.0f
                    level3 = false
                }
            }
            score in 16..20 -> {
                if (level4) {
                    mX *= 2.3f
                    mY *= 2.3f
                    level4 = false
                }
            }
            else -> {
                mX *= 4.0f
                mY *= 4.0f
            }

        }

    }

    fun setMode(mode: String) {
        mMode = mode
    }

    fun increasepanellength() {
        PADDLE_WIDTH = 300F
    }


    fun tomovepaddle() {
        if (moveright) {
            cxSystempaddle += 15
        }
        else {
            cxSystempaddle -= 15
        }

        Log.d("movepaddle1","$cxSystempaddle")
        Log.d("movepaddle2","$mWidth")
        Log.d("movepaddle3","$PADDLE_WIDTH")
        if (cxSystempaddle >= (mWidth - PADDLE_WIDTH).toFloat()) {
            moveright = false
        }
        else if (cxSystempaddle <= 0f) {
            moveright = true
        }


    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mMode == "easy") {
            tvhighscore = (parent as View).findViewById(R.id.highscore) as TextView
            tvscore = (parent as View).findViewById(R.id.userscore) as TextView
            tvhighscore.setText("High score is:$mAllTimeHighScore")
            tvscore.setText("$score")

        } else if (mMode == "hard" || mMode == "intermediate") {
            tvSystemscore = (parent as View).findViewById(R.id.systemscore) as TextView
            tvscore = (parent as View).findViewById(R.id.userscore) as TextView
            tvscore.setText("$score")
            tvSystemscore.setText("$systemscore")
        }
    }

    inner class GameThread : Thread() {
        override fun run() {
            Log.d("thread", "starting thread")
            randomint = (1..2).random()
           // tvSystemscore.setText("$randomint")
            while (gameOn) {
                Log.d("beginloop", "thread execution $name")
                cxBall += mX
                cyBall += mY

                if (mMode == "intermediate") {
                    if (randomint == 2 || randomint == 1)  {
                        tomovepaddle()
                        if (cyBall < PADDLE_HEIGHT + cRadius) {
                            Log.d("cyBall", "check hit")
                            if (cxBall in (cxSystempaddle)..(cxSystempaddle + PADDLE_WIDTH)) {
                                cyBall = PADDLE_HEIGHT + cRadius
                                mY *= -1
                                playpingpongHitSound()
                                systemscore++
                                tvSystemscore.setText("$systemscore")
                                if (score > 5) {
                                    increaseVelocity()
                                    if (score % 5 == 0 || score % 5 == 1) {
                                        increasepanellength()
                                    } else {
                                        PADDLE_WIDTH = 200F
                                    }
                                }

                            }
                        }

                         if (cyBall < cRadius) {
                            playpingpongFailSound()
                            gameOn = false
                            gameEnd = true
                        }

                    }
                }
                    if (mMode == "hard") {
                        if (mY < 0) {
                            cxSystempaddle = cxBall - PADDLE_WIDTH / 2f
                            Log.d("hardmode", "not hitting")
                            if (cyBall < PADDLE_HEIGHT + cRadius) {
                                Log.d("cyBall", "check hit")
                                if (cxBall in (cxSystempaddle)..(cxSystempaddle + PADDLE_WIDTH)) {
                                    cyBall = PADDLE_HEIGHT + cRadius
                                    mY *= -1
                                    playpingpongHitSound()
                                    systemscore++
                                    tvSystemscore.setText("$systemscore")
                                    if (score > 5) {
                                        increaseVelocity()
                                        if (score % 5 == 0 || score % 5 == 1) {
                                            increasepanellength()
                                        } else {
                                            PADDLE_WIDTH = 200F
                                        }
                                    }

                                }
                            }
                        }
                    }
                    // Handling the touch of the background
                    if (cxBall > mWidth - cRadius) { //ball hitting right side of screen
                        playpingpongHitSound()
                        cxBall = mWidth - cRadius
                        mX *= -1
                    } else if (cxBall < cRadius) // ball hitting left side of the screen
                    {
                        playpingpongHitSound()
                        cxBall = cRadius
                        mX *= -1
                    }

                    if (cyBall >= mHeight - cRadius + 10f - PADDLE_HEIGHT) {
                        if (cxBall in (cxPaddle - cRadius)..(cxPaddle + PADDLE_WIDTH + cRadius))//hitting the paddle
                        {
                            playpingpongHitSound()
                            cyBall = mHeight - cRadius - PADDLE_HEIGHT
                            mY *= -1
                            if (mMode == "hard" || mMode == "intermediate") {
                                score++
                                tvscore.setText("$score")
                            }
                        } else   //losing the game
                        {
                            playpingpongFailSound()
                            gameOn = false
                            gameEnd = true
                        }
                    } else if (cyBall < cRadius)  //hitting top part of screen
                    {
                        playpingpongHitSound()
                        cyBall = cRadius
                        mY *= -1
                        if (mMode == "easy") {
                            score++
                            tvscore.setText("$score")
                        }
                        if (score > 5) {
                            increaseVelocity()
                            if (score % 5 == 0 || score % 5 == 1) {
                                increasepanellength()
                            } else {
                                PADDLE_WIDTH = 200F
                            }
                        }
                    }

                    postInvalidate()
                    sleep(10)
                }
            }
        }
    }


