package com.ckw.customviewcollections.level_view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/8.
 */
class LevelViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_view)

        val circleLevelView = findViewById<CircleLevelView>(R.id.circle_level_view)
        //以下两个方法的设置需要在setAngle或者setValue之前
        circleLevelView.setAnimationDuration(2500)
        circleLevelView.setIsPercent(true)
        //通过设置初始和结束的角度
//        circleLevelView.setAngle(0f,100f)
        //通过设置百分比
        circleLevelView.setValue(55.746f)
    }
}