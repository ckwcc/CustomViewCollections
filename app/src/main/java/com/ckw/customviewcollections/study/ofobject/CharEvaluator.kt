package com.ckw.customviewcollections.study.ofobject

import android.animation.TypeEvaluator

/**
 * Created by ckw
 * on 2018/6/20.
 */
class CharEvaluator : TypeEvaluator<Char> {
    override fun evaluate(fraction: Float, startValue: Char?, endValue: Char?): Char {
        val start = startValue!!.toInt()
        val end = endValue!!.toInt()
        val current = ((end - start) * fraction + start)
        return current.toChar()
    }
}