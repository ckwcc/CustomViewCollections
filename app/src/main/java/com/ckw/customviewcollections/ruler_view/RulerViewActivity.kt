package com.ckw.customviewcollections.ruler_view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/7.
 */
class RulerViewActivity: AppCompatActivity(), RulerView.OnValueChangeListener {
    override fun onValueChange(value: Float) {
        mTvShow!!.text = value.toString()
    }

    private var mTvShow: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruler_view)

        val rulerView = findViewById<RulerView>(R.id.ruler_view)
        mTvShow = findViewById(R.id.tv_show_value)
        rulerView.setRulerViewParams(155f,100f,200f,0.1f)
        rulerView.setOnValueChangeListener(this)

    }
}