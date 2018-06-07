package com.ckw.customviewcollections.rulerview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ckw.customviewcollections.R
import kotlinx.android.synthetic.main.activity_ruler_view.*

/**
 * Created by ckw
 * on 2018/6/7.
 */
class RulerViewActivity: AppCompatActivity(), RulerView.OnValueChangeListener {
    override fun onValueChange(value: Float) {
        tv_show_value.text = value.toString()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruler_view)

        ruler_view.setRulerViewParams(155f,100f,200f,0.1f)
        ruler_view.setOnValueChangeListener(this)

    }
}