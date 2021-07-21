package com.example.pingpong

import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

var pingponghitSound = 0
var failureSound = 0
var powerupsound = 0
lateinit var soundPoolBallHit : SoundPool
lateinit var soundPoolFailure : SoundPool
lateinit var soundPoolPowerup : SoundPool


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val actionbar: ActionBar? = supportActionBar
        actionbar?.hide()


        if (Build.VERSION.SDK_INT >= 21) {
            val audioAttrib = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            val builder = SoundPool.Builder()
            builder.setAudioAttributes(audioAttrib).setMaxStreams(6)
            soundPoolBallHit = builder.build()
            soundPoolFailure = builder.build()
            soundPoolPowerup = builder.build()

        } else {
            soundPoolBallHit = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
            soundPoolFailure = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
            soundPoolPowerup = SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        }


        pingponghitSound = soundPoolBallHit.load(this, R.raw.pingponghit, 1)
        failureSound = soundPoolFailure.load(this, R.raw.failbuzzer, 1)
        powerupsound = soundPoolPowerup.load(this, R.raw.powerupsound, 1)


        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            val intent = Intent(this, easymode::class.java)
            startActivity(intent)
        }
        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            val intent = Intent(this, hardmode::class.java)
            startActivity(intent)
        }
        val button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener {
            val intent = Intent(this, intermediatemode::class.java)
            startActivity(intent)


        }
    }
}
