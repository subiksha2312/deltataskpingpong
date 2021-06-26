package com.example.pingpong

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.core.view.isVisible
import kotlin.random.Random

/**
 * TODO: document your custom view class.
 */


class GameBoard : View {

    private val paintGameBoard:Paint = Paint()
    private val paintBandP:Paint = Paint()
    private val paintScoreText:Paint = Paint()
    private val paintHighScoreText:Paint = Paint()
    private val paintEndCard: Paint = Paint()
    private val paintreplay: Paint =Paint()
    private val paintreplaytext: Paint=Paint()

    private val PADDLE_WIDTH = 200F
    private val PADDLE_HEIGHT = 50F
    private var cxPaddle = 0F
    private var cxTouch = 0F

    private var cxBall = 100F
    private var cyBall = 100F
    private var cRadius = 30F

    private var gameOn = false
    private var gameEnd = false

    private var mX = 5F
    private var mY = 5F


    private var mWidth = 0
    private var mHeight = 0

    private var mAllTimeHighScore=0
    private var score = 0


    lateinit var hsPref:SharedPreferences


    constructor (context:Context) : super(context) {
        init(null, context)
    }

    constructor (context:Context, attrs:AttributeSet) : super(context, attrs)
    {
        init(attrs, context)
    }

    private fun init(attrs: AttributeSet?, context: Context) {
        paintGameBoard.color = Color.BLACK
        paintBandP.color = Color.WHITE
        paintScoreText.color = Color.WHITE
        paintHighScoreText.color = Color.DKGRAY
        paintEndCard.color = Color.DKGRAY
        paintreplay.color = Color.BLACK
        paintreplaytext.color = Color.WHITE


        paintGameBoard.style = Paint.Style.FILL
        paintBandP.style = Paint.Style.FILL
        paintEndCard.style = Paint.Style.FILL

        paintScoreText.style = Paint.Style.STROKE
        paintScoreText.textSize = 100F
        paintHighScoreText.style = Paint.Style.FILL_AND_STROKE
        paintHighScoreText.textSize = 100F
        paintreplaytext.textSize = 50F

        hsPref = context.getSharedPreferences((R.string.HighScoreKey).toString(), Context.MODE_PRIVATE)
        if (hsPref.contains((R.string.HighScore).toString()) == false) {
            with(hsPref.edit()) {
                putInt((R.string.HighScore).toString(), 0)
                apply()
                commit()
            }
        }

        mAllTimeHighScore = hsPref.getInt((R.string.HighScore).toString(),0)

    }



    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = width
        mHeight = height
        cxBall = (Random.nextInt(1, mWidth)).toFloat()
        cyBall = (mHeight/6).toFloat()
        cxPaddle = (width/2 - PADDLE_WIDTH/2).toFloat()
        paintScoreText.textSize = height / 20f
        paintHighScoreText.textSize = height / 20f


        start()

    }

    override fun onDraw(canvas: Canvas) {

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintGameBoard)


        canvas?.drawRect(
            cxPaddle,
            (height - PADDLE_HEIGHT).toFloat(),
            cxPaddle + PADDLE_WIDTH,
            height.toFloat(), paintBandP
        )
        canvas?.drawCircle(cxBall, cyBall, cRadius, paintBandP)
        canvas?.drawText(score.toString(), 20f, height / 20 - 10f, paintScoreText)
        canvas?.drawText("High Score:$mAllTimeHighScore", 400f, height/20 - 10f, paintHighScoreText)

        if(gameEnd == true){
            canvas?.drawRect(0f, (height/3).toFloat(), width.toFloat(), ((height/3)+300).toFloat(), paintEndCard)
            canvas?.drawText("      Your score is: $score", 20f,((height/3)+75).toFloat(), paintScoreText)
            canvas?.drawRect((width/2).toFloat(),((height/3)+150).toFloat(),((width/2)+180).toFloat(),((height/3)+225).toFloat(),paintreplay)
            canvas?.drawText("Replay!", ((width/2)+10).toFloat(),((height/3)+200).toFloat(), paintreplaytext)

        }
        super.onDraw(canvas)



    }

    override fun onTouchEvent(event: MotionEvent?): Boolean
    {
        when (event?.action)
        {
            MotionEvent.ACTION_DOWN ->
            {
                if(gameEnd==false){
                    cxTouch = event.x
                }
                else {
                    if(checkPointInReplayRect(event.x , event.y) == true) {
                        gameEnd = false
                        resetGame()
                        start()
                    }
                }
            }
            MotionEvent.ACTION_MOVE ->
            {
                if(gameEnd==false){
                    movePaddle(event)
                }



            }
        }

        return true
    }


    fun checkPointInReplayRect(x:Float,y:Float):Boolean{
        if (x > (width/2).toFloat() &&
            x < ((width/2)+180).toFloat() &&
            y > ((height/3)+150).toFloat() &&
            y < ((height/3)+225).toFloat()) {

                return true
        }
        return false
    }

       private fun movePaddle(event: MotionEvent){
           cxPaddle = cxPaddle - (cxTouch - event.x)
           cxTouch = event.x

           if (cxPaddle < 0) {
               cxPaddle = 0F
           }
           else if ( cxPaddle > width - PADDLE_WIDTH){
               cxPaddle = width - PADDLE_WIDTH
           }
           invalidate()

       }

        fun start(){
            gameOn = true
            GameThread().start()
        }

        fun stop(){
            gameOn = false
        }



        fun resetGame(){

            if (score > mAllTimeHighScore) {
                mAllTimeHighScore = score
                with (hsPref.edit()) {
                    putInt((R.string.HighScore).toString(), mAllTimeHighScore)
                    apply()
                }

            }
            cxBall = (Random.nextInt(1, mWidth)).toFloat()
            cyBall = (mHeight/6).toFloat()
            mX = 5F
            mY = 5F
            score = 0
            invalidate()


        }




    inner class GameThread : Thread()
    {
        override fun run()
        {
            while (gameOn)
            {
                cxBall += mX
                cyBall += mY

                // Handle of the touch of the background
                if (cxBall > mWidth - cRadius) {

                    cxBall = mWidth - cRadius
                    mX *= -1
                }
                else if (cxBall < cRadius)
                {
                    cxBall = cRadius
                    mX *= -1
                }

                if (cyBall >= mHeight - cRadius - PADDLE_HEIGHT)
                {
                    if (cxBall in cxPaddle..cxPaddle + PADDLE_WIDTH)
                    {
                        cyBall = mHeight - cRadius - PADDLE_HEIGHT
                        mY *= -1
                    }
                    else
                    {
                        gameOn = false
                        gameEnd = true
                    }
                }
                else if (cyBall < cRadius)
                {
                    cyBall = cRadius
                    mY *= -1
                    score++
                }

                postInvalidate()

                sleep(10)
            }
        }
    }
}
