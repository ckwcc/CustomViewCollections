package com.ckw.customviewcollections.study.interpolator_evaluator

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation.INFINITE
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.TextView
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/20.
 * 自定义动画插值器以及Evaluator的使用
 */
class InterpolatorEvaluatorActivity : AppCompatActivity() {

    private var mBtnStart: Button? = null
    private var mBtnEnd: Button? = null
    private var mTvShow: TextView? = null

    private var mAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interpolator)

        mBtnStart = findViewById(R.id.btn_start)
        mBtnEnd = findViewById(R.id.btn_end)
        mTvShow = findViewById(R.id.tv_interpolator)

        mBtnStart!!.setOnClickListener {
            startAnim()
        }

        mBtnEnd!!.setOnClickListener {
            stopAnim()
        }

    }

    @SuppressLint("WrongConstant")
    private fun startAnim() {
        mAnimator = ValueAnimator.ofFloat(0f, 400f)
        mAnimator!!.addUpdateListener {
            val currentValue = it.getAnimatedValue() as Float
            mTvShow!!.layout(mTvShow!!.left,currentValue.toInt(),mTvShow!!.right,(currentValue.toInt() + mTvShow!!.measuredHeight))
        }

        mAnimator!!.duration = 2000
        mAnimator!!.repeatMode = INFINITE
        mAnimator!!.repeatCount = INFINITE
        //弹跳插值器
//        mAnimator!!.interpolator = BounceInterpolator()
        //自定义反转插值器
//        mAnimator!!.interpolator = ReverseInterpolator()

        //自定义Evaluator
        mAnimator!!.setEvaluator(MyEvaluator())
        mAnimator!!.start()
    }

    private fun stopAnim(){
        mAnimator!!.removeAllUpdateListeners()
        mAnimator!!.cancel()
    }


}