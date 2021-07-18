package com.example.pingpong

import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView


class easymode : AppCompatActivity() {

    private lateinit var mGameBoard:GameBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_easymode)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mGameBoard = GameBoard(this.applicationContext)
        Log.d("gameboard","initialize")

    }

   /* override fun onResume() {
        gameOn = true
        super.onResume()
        Log.d("callingResume","${gameOn}")
        //mGameBoard.start()
    }

    */

    override fun onPause() {
        gameOn = false
        super.onPause()
        Log.d("callingPause","${gameOn}")

    }

    override fun onDestroy() {
        gameOn = false
        super.onDestroy()
        Log.d("callingDestroy","callingDestroy")

    }

    override fun onBackPressed() {
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }


}









