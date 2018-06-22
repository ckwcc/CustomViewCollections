package com.ckw.customviewcollections.level_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/22.
 */
class CirclePointView : View {

    /*
   * 控件整体的宽高
   * */
    private var mWidth = 0f
    private var mHeight = 0f

    private var mPointPaint: Paint? = null
    private var mDefaultColor = Color.RED
    private var mSelectedColor = Color.BLACK

    private var mOuterCircleRadius = 100f
    private var mPointRadius = 0f

    private var mPercent = 0

    private var mTextSize = 30f
    private var mTextPaint: Paint? = null
    private var mTextColor = Color.BLACK

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context!!,attrs!!)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePointView)
        mDefaultColor = typedArray.getColor(R.styleable.CirclePointView_circlePointDefColor,mDefaultColor)
        mSelectedColor = typedArray.getColor(R.styleable.CirclePointView_circlePointAchieveColor,mSelectedColor)
        mTextColor = typedArray.getColor(R.styleable.CirclePointView_circlePointTextColor,mTextColor)
        mTextSize = typedArray.getDimension(R.styleable.CirclePointView_circlePointTextSize,mTextSize)
        mPercent = typedArray.getInteger(R.styleable.CirclePointView_circlePointAchievePercent,mPercent)

        mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPointPaint!!.style = Paint.Style.FILL
        mPointPaint!!.color = mSelectedColor

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.style = Paint.Style.STROKE
        mTextPaint!!.color = mTextColor
        mTextPaint!!.textSize = mTextSize

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)

        when(widthMode){
            MeasureSpec.EXACTLY ->{
                mWidth = Math.min(measureWidth,measureHeight).toFloat()
                mHeight = Math.min(measureWidth,measureHeight).toFloat()
                setMeasuredDimension(mWidth.toInt(),mHeight.toInt())
            }

            MeasureSpec.AT_MOST -> {
                mWidth = mOuterCircleRadius * 2
                mHeight = mOuterCircleRadius * 2
                setMeasuredDimension(mWidth.toInt(),mHeight.toInt())
            }

            MeasureSpec.UNSPECIFIED -> {

            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = w.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        mOuterCircleRadius = mWidth.div(2)
        //按照公式应该是0.9,360/200 = 1.8 每个圆占1.8度，1.8/2 = 0.9画出来圆点间隔有点大，就用了1.0
        val sin1 = Math.sin(Math.toRadians(1.0))
        mPointRadius = (sin1 * mOuterCircleRadius / (1 + sin1)).toFloat()

        mPointPaint!!.color = mSelectedColor
        for (index in 0 until mPercent){
            canvas!!.drawCircle(mOuterCircleRadius,mPointRadius,mPointRadius,mPointPaint)
            canvas.rotate(3.6f,mOuterCircleRadius,mOuterCircleRadius)
        }

        mPointPaint!!.color = mDefaultColor
        for (index in mPercent until 100){
            canvas!!.drawCircle(mOuterCircleRadius,mPointRadius,mPointRadius,mPointPaint)
            canvas.rotate(3.6f,mOuterCircleRadius,mOuterCircleRadius)
        }

        val text = mPercent.toString().plus("%")
        val textWidth = mTextPaint!!.measureText(text)
        val textHeight = getFontHeight(mTextPaint!!)
        canvas!!.drawText(text,
                mOuterCircleRadius - textWidth.div(2),
                mOuterCircleRadius + textHeight.div(2),
                mTextPaint!!)
    }

    fun setAchievePercent(percent: Int) {
        mPercent = percent
        invalidate()
    }

    private fun getFontHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.descent - fm.ascent
    }


}