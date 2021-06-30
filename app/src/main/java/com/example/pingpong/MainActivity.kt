package com.example.pingpong

import android.content.pm.ActivityInfo
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

var pingponghitSound = 0
var failureSound = 0
lateinit var soundPoolBallHit : SoundPool
lateinit var soundPoolFailure : SoundPool


class MainActivity : AppCompatActivity() {

    private lateinit var mGameBoard:GameBoard


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (Build.VERSION.SDK_INT >= 21) {
            val audioAttrib = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            val builder = SoundPool.Builder()
            builder.setAudioAttributes(audioAttrib).setMaxStreams(6)
            soundPoolBallHit = builder.build()
            soundPoolFailure = builder.build()

        }
        else {
            soundPoolBallHit = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
            soundPoolFailure = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        }


        pingponghitSound = soundPoolBallHit.load(this, R.raw.pingponghit,1)
        failureSound = soundPoolFailure.load(this, R.raw.failbuzzer,1)

        mGameBoard = GameBoard(this.applicationContext)





    }

    override fun onResume() {
        super.onResume()
        //mGameBoard.start()
    }

    override fun onPause() {
        super.onPause()
        mGameBoard.stop()
    }



}