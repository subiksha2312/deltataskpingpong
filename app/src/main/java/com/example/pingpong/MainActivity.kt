package com.example.pingpong

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private lateinit var mGameBoard:GameBoard


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        mGameBoard = GameBoard(this.applicationContext)

    }

    override fun onResume() {
        super.onResume()
        // mGameBoard.start()
    }

    override fun onPause() {
        super.onPause()
        mGameBoard.stop()
    }



}