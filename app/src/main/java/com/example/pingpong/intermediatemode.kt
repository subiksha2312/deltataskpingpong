package com.example.pingpong

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class intermediatemode : AppCompatActivity() {

    private lateinit var mGameBoard:GameBoard
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intermediatemode)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mGameBoard = GameBoard(this.applicationContext)
        mGameBoard.setMode("intermediate")
    }

    override fun onResume() {
        gameOn = true
        super.onResume()
        Log.d("callingResume","${gameOn}")
        //mGameBoard.start()
    }

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
        val intent= Intent(this,MainActivity::class.java)  //Creating an intent so that when the button is clicked,it moves onto another activity
        startActivity(intent)
    }



}
