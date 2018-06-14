package com.ckw.customviewcollections.tag_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup

/**
 * Created by ckw
 * on 2018/6/13.
 */
class TagViewLayout : ViewGroup {

    private var mContext: Context? = null

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        mContext = context!!
    }


    /*
    * 让子控件能获取距离父控件的margin属性
    * */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(mContext,attrs)
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val suggestWidth = MeasureSpec.getSize(widthMeasureSpec)
        val suggestHeight = MeasureSpec.getSize(heightMeasureSpec)

        //测量子view的尺寸信息
        measureChildren(widthMeasureSpec,heightMeasureSpec)

        var cWidth: Int//childView的宽度
        var cHeight: Int
        var lineWidth: Int = paddingLeft + paddingRight//当前行的宽度
        var maxLineWidth: Int = lineWidth // 用于记录的最大行宽度，但是不能超过suggestWidth
        var singleLineHeight: Int = 0
        var maxLineHeight: Int = paddingTop + paddingBottom//用于记录控件的最大高度,同上
        var childParams: MarginLayoutParams//childView的margin属性
        var resultWidth: Int = suggestWidth//最终的宽度
        var resultHeight: Int = suggestHeight//最终的高度

        for (i in 0 until childCount){
            val view = getChildAt(i)

            if(view.layoutParams is MarginLayoutParams){
                childParams = view.layoutParams as MarginLayoutParams
            }else{
                childParams = ViewGroup.MarginLayoutParams(view.layoutParams)
            }

            cWidth = view.measuredWidth + childParams.leftMargin + childParams.rightMargin
            cHeight = view.measuredHeight + childParams.topMargin + childParams.bottomMargin

            /**如果后者不判断的话，当出现widthMode为MeasureSpec.EXACTLY,
             * 而heightMode == MeasureSpec.AT_MOST时
             * 会出现最后设置的高度为零的情况,导致界面不显示
             * */
            if(widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST){
                if(lineWidth + cWidth > suggestWidth){//需要换行
                    lineWidth = cWidth + paddingLeft + paddingRight//新的一行的宽度
                    maxLineHeight += singleLineHeight//将上一行的高度加上
                    singleLineHeight = cHeight//新的一行的高度
                }else{
                    lineWidth += cWidth
                    if(lineWidth > maxLineWidth){
                        maxLineWidth = lineWidth//用于记录这么多行中宽度最大的一行
                    }
                }

                if(singleLineHeight < cHeight){//目的是设置单行的高度为这一行中最高的childView
                    singleLineHeight = cHeight
                }

                if(i == childCount - 1){//当来到最后一个childview的时候，不管这行有没有满，都要加上这一行的高度
                    maxLineHeight += singleLineHeight
                }
            }


        }

        if(widthMode == MeasureSpec.AT_MOST){
            resultWidth = maxLineWidth
        }

        if(heightMode == MeasureSpec.AT_MOST){
            resultHeight = maxLineHeight
            if(resultHeight > suggestHeight){
                resultHeight = suggestHeight
            }
        }

        setMeasuredDimension(resultWidth,resultHeight)

    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left: Int = paddingLeft
        var right: Int = width - paddingRight
        var top: Int = paddingTop
        var bottom: Int = height - paddingBottom

        var singleLineHeight: Int = 0
        var lp: MarginLayoutParams
        var childWidth: Int
        var childHeight: Int

        for (index in 0 until childCount){
            val view = getChildAt(index)

            if(view.layoutParams is MarginLayoutParams){
                lp = view.layoutParams as MarginLayoutParams
            }else{
                lp = ViewGroup.MarginLayoutParams(view.layoutParams)
            }
            childWidth = view.measuredWidth + lp.leftMargin + lp.rightMargin
            childHeight = view.measuredHeight + lp.topMargin + lp.bottomMargin

            if(left + childWidth > right){//该换行了
                left = paddingLeft//新起点都是这个
                top += singleLineHeight
                singleLineHeight = childHeight
            }else{
                if(singleLineHeight < childHeight){
                    singleLineHeight = childHeight
                }
            }

            if(top >= bottom){
                break
            }

            //绘制子view的位置
            view.layout(left + lp.leftMargin,top + lp.topMargin,left + childWidth,top + childHeight)

            left += childWidth//绘制完一个view后，left的位置显然要增加上刚刚绘制的view的宽度
        }
    }

    /*
    * 添加childView
    * */
    fun addTagView(tagView: TagView) {
        addView(tagView)
    }
}