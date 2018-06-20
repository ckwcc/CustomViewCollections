package com.ckw.customviewcollections.study.interpolator_evaluator

import android.view.animation.Interpolator

/**
 * Created by ckw
 * on 2018/6/20.
 */
class ReverseInterpolator : Interpolator {
    override fun getInterpolation(input: Float): Float {
        return 1 - input
    }
}