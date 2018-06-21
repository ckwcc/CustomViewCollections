package com.ckw.customviewcollections.playpause_view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/21.
 */
class PlayPauseActivity : AppCompatActivity(), PlayPauseView.PlayPauseListener {
    override fun play() {
        Toast.makeText(this,"现在处于播放状态",Toast.LENGTH_SHORT).show()
    }

    override fun pause() {
        Toast.makeText(this,"现在处于暂停状态",Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_pause)
        val playPauseView = findViewById<PlayPauseView>(R.id.play_pause_view)
        playPauseView.setPlayPauseListener(this)
    }
}