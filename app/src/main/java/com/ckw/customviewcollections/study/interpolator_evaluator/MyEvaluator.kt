package com.ckw.customviewcollections.study.interpolator_evaluator

import android.animation.TypeEvaluator

/**
 * Created by ckw
 * on 2018/6/20.
 */
class MyEvaluator : TypeEvaluator<Float> {
    override fun evaluate(fraction: Float, startValue: Float?, endValue: Float?): Float {
        //默认的是这样的
        val fl = startValue!! + (endValue!! - startValue!!) * fraction

        return  startValue!! + (endValue!! - startValue!!) * fraction * 2 + 200
    }
}