package com.ckw.customviewcollections.radar_view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/26.
 */
class RadarViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radar_view)
        val radarView = findViewById<RadarView>(R.id.radar_view)
        radarView.setRadarStrings("语文","数学","化学","物理","英语","生物")
        radarView.setRadarProgress(50.5f,60.5f,70f,80.5f,90f,45f)
    }
}