package com.ckw.customviewcollections.drag_view

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.DragEvent
import android.view.View
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/14.
 */
class DragViewActivity: AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_view)
        val dragView = findViewById<DragView>(R.id.drag_view)

    }
}