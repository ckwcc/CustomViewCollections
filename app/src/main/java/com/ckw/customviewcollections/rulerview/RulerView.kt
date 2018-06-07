package com.ckw.customviewcollections.rulerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/7.
 *
 */
class RulerView : View{

    private var mWidth: Int = 0

    private val mLineColor = Color.GRAY   //刻度的颜色
    private val mTextColor = Color.BLACK    //文字的颜色

    /*
    * 三种标线的高度
    * */
    private var mMaxLineHeight: Float = 100f
    private var mMidLineHeight: Float = 60f
    private var mMinLineHeight: Float = 40f

    /*
    *标线的数量 和 中间间隔 和 标线本身的宽度
    * */
    private var mTotalLineNumber: Int = 0
    private var mLineSpaceWidth: Float = 0f
    private var mLineWidth: Float = 2f
    /*
    * 可以偏移的距离,负数
    * */
    private var mMaxOffset: Float = 0f

    private var mMaxValue: Float = 100f
    private var mMinValue: Float = 0f
    private var mSelectedValue: Float = 50f
    private var mPerValue: Float = 1f

    /*
    * 偏移量，手指从右向左，控件实际上是往左滑动，这里设置滑动的为mOffset为负数
    * */
    private var mOffset: Float = 0f
    /*
    * 手指滑动过程中记录的距离，从上一个点滑动到下一个点的距离，向左滑动为正数
    * */
    private var mMove: Int = 0
    /*
    * 上一次down的点的x坐标
    * */
    private var mLastX: Int = 0

    /*
    * 显示的字体大小
    * */
    private var mTextSize: Float = 30f
    private var mTextHeight: Float = 0f

    /*
    * 控件左右两侧是否需要透明
    * */
    private var mAlphaEnable: Boolean = false

    private var mTextPaint: Paint? = null
    private var mLinePaint: Paint? = null

    private var mVelocityTracker: VelocityTracker? = null
    private var mScroller: Scroller? = null
    private var mMinVelocity: Int = 0

    private var mListener: OnValueChangeListener? = null

    fun setOnValueChangeListener(listener: OnValueChangeListener) {
        mListener = listener
    }

    constructor(mContext: Context) : this(mContext,null)
    constructor(mContext: Context,arrrs: AttributeSet?) : this(mContext,arrrs!!,0)
    constructor(mContext: Context, attrs: AttributeSet, defStyleAttr:Int) : super(mContext,attrs,defStyleAttr){
        init(mContext,attrs)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(w != 0 && h != 0){
            mWidth = w
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var left: Float//显示的控件的最左侧
        var height: Float//标线的高度
        var alpha: Int = 1
        var scale: Float//透明比例

        val centerX = mWidth.div(2)
        for(i in 0 until mTotalLineNumber){

            left = centerX + mOffset + i * mLineSpaceWidth
            if (left < 0 || left > mWidth){
                continue
            }

            if (i % 10 == 0){
                height = mMaxLineHeight
            }else if (i % 5 == 0){
                height = mMidLineHeight
            }else{
                height = mMinLineHeight
            }

            if(mAlphaEnable){
                scale = 1 - Math.abs(left - centerX) / centerX
                alpha = ((255 * scale * scale).toInt())

                mLinePaint!!.alpha = alpha
            }

            canvas!!.drawLine(left,0f,left,height,mLinePaint!!)

            if( i % 10 == 0){
                val number = mMinValue + (i * mPerValue).div(10)
                if(mAlphaEnable){
                    mTextPaint!!.alpha = alpha
                }

                canvas.drawText(number.toString(),left - mTextPaint!!.measureText(number.toString()).div(2),
                        height + mTextHeight ,mTextPaint)
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val action = event!!.action
        val xPosition = event.x.toInt()
        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)

        when(action){
            MotionEvent.ACTION_DOWN -> {
                mScroller!!.forceFinished(true)
                mLastX = xPosition
                mMove = 0
            }
            MotionEvent.ACTION_MOVE -> {
                mMove = mLastX.minus(xPosition)
                changeMoveAndValue()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                countMoveEnd()
                countVelocityTracker()
                return false
            }
            else -> {
            }
        }

        mLastX = xPosition
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller!!.computeScrollOffset()) {     //mScroller.computeScrollOffset()返回 true表示滑动还没有结束
            if (mScroller!!.getCurrX() == mScroller!!.getFinalX()) {
                countMoveEnd()
            } else {
                val xPosition = mScroller!!.getCurrX()
                mMove = mLastX - xPosition
                changeMoveAndValue()
                mLastX = xPosition
            }
        }
    }

    private fun changeMoveAndValue() {
        mOffset -= mMove

        if(mOffset <= mMaxOffset){
            mOffset = mMaxOffset
            mMove = 0
            mScroller!!.forceFinished(true)
        }else if (mOffset >= 0){
            mOffset = 0f
            mMove = 0
            mScroller!!.forceFinished(true)
        }

        mSelectedValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mLineSpaceWidth) * mPerValue / 10.0f

        notifyValueChange()
        postInvalidate()
    }

    private fun countMoveEnd() {

        mOffset -= mMove.toFloat()
        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset
        } else if (mOffset >= 0) {
            mOffset = 0f
        }

        mLastX = 0
        mMove = 0

        mSelectedValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mLineSpaceWidth) * mPerValue / 10.0f
        mOffset = (mMinValue - mSelectedValue) * 10.0f / mPerValue * mLineSpaceWidth



        notifyValueChange()
        postInvalidate()
    }

    private fun countVelocityTracker() {
        mVelocityTracker!!.computeCurrentVelocity(1000)

        val xVelocity = mVelocityTracker!!.xVelocity

        if(Math.abs(xVelocity) > mMinVelocity){
            mScroller!!.fling(0,0,xVelocity.toInt(),0,Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0)
        }

    }

    private fun init(mContext: Context, mAttrs: AttributeSet) {
        val typedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.RulerView)
        mAlphaEnable = typedArray.getBoolean(R.styleable.RulerView_alphaEnable,false)
        mLineSpaceWidth = typedArray.getDimension(R.styleable.RulerView_lineSpaceWidth, 25f);
        mLineWidth = typedArray.getDimension(R.styleable.RulerView_lineWidth, 2f);
        mMaxLineHeight = typedArray.getDimension(R.styleable.RulerView_lineMaxHeight, 100f);
        mMidLineHeight = typedArray.getDimension(R.styleable.RulerView_lineMidHeight, 60f);
        mMinLineHeight = typedArray.getDimension(R.styleable.RulerView_lineMinHeight, 40f);
        mTextSize = typedArray.getDimension(R.styleable.RulerView_textSize, 30f);

        mMinValue = typedArray.getFloat(R.styleable.RulerView_minValue, 0.0f);
        mMaxValue = typedArray.getFloat(R.styleable.RulerView_maxValue, 100.0f);
        mSelectedValue = typedArray.getFloat(R.styleable.RulerView_selectorValue, 50.0f);
        mPerValue = typedArray.getFloat(R.styleable.RulerView_perValue, 1f);

        mTotalLineNumber = ((mMaxValue.minus(mMinValue) * 10).div(mPerValue)).toInt() + 1
        mMaxOffset = ( - mTotalLineNumber.minus(1) * mLineSpaceWidth)

        mOffset = mMinValue.minus(mSelectedValue).div(mPerValue) * mLineSpaceWidth * 10

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.textSize = mTextSize
        mTextPaint!!.color = mTextColor
        mTextHeight = getFontHeight(mTextPaint!!)

        mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mLinePaint!!.strokeWidth = mLineWidth
        mLinePaint!!.color = mLineColor

        mScroller = Scroller(mContext)
        mMinVelocity = ViewConfiguration.get(mContext).scaledMinimumFlingVelocity

        typedArray.recycle()
    }

    private fun getFontHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.descent - fm.ascent
    }

    private fun notifyValueChange() {
        if (null != mListener) {
            mListener!!.onValueChange(mSelectedValue)
        }
    }

    /*
    * 初始选中的值、最小值、最大值、最小单位
    * */
    fun setRulerViewParams(selectorValue: Float, minValue: Float, maxValue: Float, per: Float){
        mSelectedValue = selectorValue
        mMinValue = minValue
        mMaxValue = maxValue
        mPerValue = per * 10//考虑到有一位小数点的问题，将其扩大十倍，但是其他地方需要注意/10

        mTotalLineNumber = ((mMaxValue.minus(mMinValue) * 10).div(mPerValue)).toInt() + 1
        mMaxOffset = ( - mTotalLineNumber.minus(1) * mLineSpaceWidth)

        mOffset = mMinValue.minus(mSelectedValue).div(mPerValue) * mLineSpaceWidth * 10

        invalidate()
        visibility = VISIBLE

    }

    /**
     * 滑动后的回调
     */
    interface OnValueChangeListener {
        fun onValueChange(value: Float)
    }


}