package com.ckw.customviewcollections.study.ofobject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.ckw.customviewcollections.R
import android.view.animation.AccelerateInterpolator
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator


/**
 * Created by ckw
 * on 2018/6/20.
 */
class OfObjectActivity : AppCompatActivity() {
    private var mBtnStart: Button? = null
    private var mBtnEnd: Button? = null
    private var mTvShow: TextView? = null

    private var mAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ofobject)

        mBtnStart = findViewById(R.id.btn_start)
        mBtnEnd = findViewById(R.id.btn_end)
        mTvShow = findViewById(R.id.tv_ofobject)

        mBtnStart!!.setOnClickListener {
            startAnim()
        }

        mBtnEnd!!.setOnClickListener {
            stopAnim()
        }

    }

    fun startAnim() {
        mAnimator = ValueAnimator.ofObject(CharEvaluator(), 'A', 'Z')
        mAnimator!!.addUpdateListener { animation ->
            val text = animation.animatedValue as Char
            mTvShow!!.setText(text.toString())
        }
        mAnimator!!.duration = 12000
        mAnimator!!.interpolator = LinearInterpolator()
        mAnimator!!.start()
    }

    private fun stopAnim(){
        mAnimator!!.removeAllUpdateListeners()
        mAnimator!!.cancel()
    }
}