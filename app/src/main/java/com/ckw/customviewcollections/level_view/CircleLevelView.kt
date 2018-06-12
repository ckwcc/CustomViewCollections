package com.ckw.customviewcollections.level_view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/8.
 * <attr name="externalRadius" format="dimension"/> 外部圆的半径
 * <attr name="externalColor" format="color"/>  外部圆的颜色
 * <attr name="innerRadius" format="dimension"/> 内部圆的半径
 * <attr name="innerColor" format="color"/> 内部圆的颜色
 * <attr name="percentColor" format="color"/> 进度条的颜色
 * <attr name="startAngle" format="float"/> 进度条开始的角度
 * <attr name="endAngle" format="float"/> 结束的角度
 * <attr name="centerTextColor" format="color"/> 中心文字的颜色
 * <attr name="centerTextSize" format="dimension"/> 中心文字的大小
 * <attr name="isPercent" format="boolean"/> 是否需要显示百分比
 */
class CircleLevelView : View {

    /*
    * 外圆半径
    * */
    private var mExternalRadius: Float = 100f
    /*
    * 内院半径
    * */
    private var mInnerRadius: Float = 90f

    /*
    * 各种颜色
    * */
    private var mInnerColor = Color.WHITE
    private var mExternalColor = Color.YELLOW
    private var mPercentColor = Color.BLACK
    private var mTextColor = Color.BLACK

    /*
    * 各种画笔
    * */
    private var mExternalPaint: Paint? = null
    private var mInnerPaint: Paint? = null
    private var mPercentPaint: Paint? = null
    private var mTextPaint: Paint? = null

    private var mTextSize = 30f
    /*
    * 是否是百分比
    * */
    private var mIsPercent = false
    /*
    * 进度条开始的角度
    * */
    private var mStartAngle: Float = 0f
    /*
    * 进度条结束的角度
    * */
    private var mEndAngle: Float = 0f
    /*
    * 当前展示的角度，由动画控制
    * */
    private var mCurrentAngle: Float = 0f

    private var mAnimationDuration = 2500L

    private var mWidth = 0
    private var mHeight = 0

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context!!,attrs!!)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleLevelView)

        mExternalRadius = typedArray.getDimension(R.styleable.CircleLevelView_externalRadius,mExternalRadius)
        mExternalColor = typedArray.getColor(R.styleable.CircleLevelView_externalColor,mExternalColor)
        mInnerRadius = typedArray.getDimension(R.styleable.CircleLevelView_innerRadius,mInnerRadius)
        mInnerColor = typedArray.getColor(R.styleable.CircleLevelView_innerColor,mInnerColor)
        mPercentColor = typedArray.getColor(R.styleable.CircleLevelView_percentColor,mPercentColor)
        mStartAngle = typedArray.getFloat(R.styleable.CircleLevelView_startAngle,mStartAngle)
        mEndAngle = typedArray.getFloat(R.styleable.CircleLevelView_endAngle,mEndAngle)
        mTextColor = typedArray.getColor(R.styleable.CircleLevelView_centerTextColor,mTextColor)
        mTextSize = typedArray.getDimension(R.styleable.CircleLevelView_centerTextSize,mTextSize)
        mIsPercent = typedArray.getBoolean(R.styleable.CircleLevelView_isPercent,false)

        mExternalPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mExternalPaint!!.style = Paint.Style.FILL
        mExternalPaint!!.color = mExternalColor

        mInnerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mInnerPaint!!.style = Paint.Style.FILL
        mInnerPaint!!.color = mInnerColor

        mPercentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPercentPaint!!.style = Paint.Style.FILL
        mPercentPaint!!.color = mPercentColor

        mTextPaint  = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.textSize = mTextSize
        mTextPaint!!.style = Paint.Style.STROKE
        mTextPaint!!.color = mTextColor

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(widthMeasureSpec,heightMeasureSpec)
        }else{
            setMeasuredDimension((mExternalRadius * 2 + 1f).toInt(),(mExternalRadius * 2 + 1).toInt())
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(w > 0 && h > 0){
            mWidth = w
            mHeight = h

        }else{
            mWidth = 100
            mHeight = 100
        }
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val left = (mWidth - mExternalRadius * 2) / 2
        val top = (mHeight - mExternalRadius * 2) / 2
        val right = mWidth - left
        val bottom = mHeight - top
        val rectF = RectF(left,top,right,bottom)

        val centerX = mWidth.div(2).toFloat()
        val centerY = mHeight.div(2).toFloat()
        canvas!!.drawCircle(centerX,centerY,mExternalRadius,mExternalPaint)
        canvas!!.drawArc(rectF,mStartAngle,mCurrentAngle,true,mPercentPaint)
        canvas!!.drawCircle(centerX,centerY,mInnerRadius,mInnerPaint)

        val percent = mCurrentAngle.div(360.0f) * 100
        var text = String.format("%.2f",percent)
        if (mIsPercent){
            text = text.plus("%")
        }
        val textWidth = mTextPaint!!.measureText(text)
        val textHeight = getFontHeight(mTextPaint!!)
        canvas!!.drawText(text,
                centerX - textWidth.div(2),
                centerY + textHeight.div(2),
                mTextPaint!!)
    }


    /*
    * 设置初始的两个角度
    * */
    fun setAngle(angleStart: Float,angleEnd: Float) {
        mStartAngle = angleStart
        mEndAngle = angleEnd
        setPercentAnimation()
    }

    /*
    * 直接设置数值
    * */
    fun setValue(value: Float) {
        if(value <0 || value > 360){
            return
        }
        mStartAngle = 0f;
        mEndAngle = value.div(100f) * 360
        setPercentAnimation()
    }

    /*
    * 设置动画时长
    * */
    fun setAnimationDuration(duration: Long) {
        mAnimationDuration = duration
    }

    fun setIsPercent(isPercent: Boolean) {
        mIsPercent = isPercent
    }

    /*
    * 设置动画
    * */
    private fun setPercentAnimation() {
        val offSet = mEndAngle - mStartAngle
        val valueAnimator = ValueAnimator.ofFloat(0f,offSet)
        valueAnimator.addUpdateListener {
            mCurrentAngle = valueAnimator.getAnimatedValue() as Float
            invalidate()
        }
        valueAnimator.duration = mAnimationDuration
        valueAnimator.start()

    }

    private fun getFontHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.descent - fm.ascent
    }
}