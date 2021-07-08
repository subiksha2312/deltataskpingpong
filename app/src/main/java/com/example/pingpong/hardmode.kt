package com.example.pingpong

import android.content.pm.ActivityInfo
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log


class hardmode : AppCompatActivity() {

    private lateinit var mGameBoard:GameBoard
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hardmode)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mGameBoard = GameBoard(this.applicationContext)
        mGameBoard.setMode("hard")
    }

    override fun onResume() {
        mGameBoard.gameOn = true
        super.onResume()
        Log.d("callingResume","${mGameBoard.gameOn}")
        //mGameBoard.start()
    }

    override fun onPause() {
        mGameBoard.gameOn = false
        super.onPause()
        Log.d("callingPause","${mGameBoard.gameOn}")

    }

    override fun onDestroy() {
        mGameBoard.gameOn = false
        super.onDestroy()
        Log.d("callingDestroy","callingDestroy")

    }

    override fun onBackPressed() {

    }



}



