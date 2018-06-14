package com.ckw.customviewcollections.star_view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/14.
 */
class StarViewActivity : AppCompatActivity(), StarViewLayout.StarClickListener {
    override fun setOnStarClick(position: Int) {
        Toast.makeText(this,"点亮了"+position+"颗星星",Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star_view)
        val starViewLayout = findViewById<StarViewLayout>(R.id.star_view_layout)
        starViewLayout.setStarClickListener(this)

    }
}