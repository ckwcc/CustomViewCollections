package com.ckw.customviewcollections.star_view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/14.
 */
class StarViewLayout : ViewGroup {

    private var mDefaultResourceId: Int? = null
    private var mLightResourceId: Int? = null
    private var mStarNumber = 5
    private var mStarMargin = 8

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context!!,attrs!!)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StarViewLayout)

        mDefaultResourceId = typedArray.getResourceId(R.styleable.StarViewLayout_defaultStar, R.mipmap.ic_launcher)
        mLightResourceId = typedArray.getResourceId(R.styleable.StarViewLayout_lightStar,R.mipmap.ic_launcher_round)
        mStarNumber = typedArray.getInteger(R.styleable.StarViewLayout_starNumber,mStarNumber)
        val starMargin = typedArray.getDimension(R.styleable.StarViewLayout_starMargin, 8f)
        mStarMargin = dip2px(context,starMargin)

        for (index in 0 until mStarNumber){
            val child = ImageView(context)
            child.setImageResource(mDefaultResourceId!!)
            addView(child)
        }

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        measureChildren(widthMeasureSpec,heightMeasureSpec)
        var childWidth: Int
        var childHeight: Int = 0

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var suggestWidth = MeasureSpec.getSize(widthMeasureSpec)
        var suggestHeight = MeasureSpec.getSize(heightMeasureSpec)
        var totalWidth = 0

        if(widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST){

            val view = getChildAt(0)
            childWidth = view.measuredWidth
            childHeight = view.measuredHeight + mStarMargin * 2
            totalWidth = childWidth * childCount + mStarMargin * (childCount + 1)

            if(totalWidth < suggestWidth){
                suggestWidth = totalWidth
            }
            if(childHeight < suggestHeight){
                suggestHeight = childHeight
            }
        }


        setMeasuredDimension(suggestWidth,suggestHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childWidth: Int
        var childHeight: Int

        var left: Int = mStarMargin
        var top: Int = mStarMargin

        for (index in 0 until childCount){

            val view = getChildAt(index)
            view.setOnClickListener {
                for (i in 0 .. index){
                    val changeView = getChildAt(i) as ImageView
                    changeView.setImageResource(mLightResourceId!!)
                }

                for (j in (index + 1) until childCount){
                    val defaultView = getChildAt(j) as ImageView
                    defaultView.setImageResource(mDefaultResourceId!!)
                }

                mListener!!.setOnStarClick(index + 1)
            }
            childWidth = view.measuredWidth
            childHeight = view.measuredHeight

            view.layout(left ,top ,left + childWidth,top + childHeight)
            left += childWidth + mStarMargin
        }
    }

    /*
    * 回复默认状态
    * */
    fun revertToDefaultState() {
        for (index in 0 until childCount){
            val view = getChildAt(index) as ImageView
            view.setImageResource(mDefaultResourceId!!)
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private var mListener: StarClickListener? = null

    fun setStarClickListener(listener: StarClickListener){
        mListener = listener
    }

    interface StarClickListener{
        fun setOnStarClick(position: Int)
    }

}