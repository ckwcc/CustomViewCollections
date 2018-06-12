package com.ckw.customviewcollections.level_view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/11.
 */
class CircleVerticalView : View{

    private var mWidth = 0f
    private var mHeight = 0f

    private var mCirclePaint: Paint? = null
    private var mCircleBgColor = Color.WHITE
    private var mCirclePath: Path? = null
    /*
    * 圆形半径
    * */
    private var mViewRadius = 50f

    /*
    * 设置的垂直方向的百分比
    * */
    private var mVerticalPercent = 0f

    /*
    * 随着动画的进行不断上涨，直到达到设定的progress
    * */
    private var mProgressHeight = 0f

    /*
    * 波浪画笔
    * */
    private var mWavePaint: Paint? = null
    private var mWaveColor = Color.YELLOW
    /*
    * 半个波长的宽度
    * */
    private var mWaveWidth = 60f
    /*
    * 横向的偏移量
    * */
    private var mOffset = 0f

    /*
    * 波浪的高度
    * */
    private var mWaveHeight = 10f

    private var mTextPaint: Paint? = null
    private var mTextSize = 24f
    private var mTextColor = Color.RED

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context!!,attrs!!)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleVerticalView)

        mViewRadius = typedArray.getDimension(R.styleable.CircleVerticalView_circleVerticalViewRadius,mViewRadius)
        mCircleBgColor = typedArray.getColor(R.styleable.CircleVerticalView_circleVerticalViewBgColor,mCircleBgColor)
        val progress = typedArray.getFloat(R.styleable.CircleVerticalView_verticalPercent,mVerticalPercent)
        mVerticalPercent = progress * mViewRadius .div(50f)
        mWaveWidth = typedArray.getDimension(R.styleable.CircleVerticalView_waveWidth,mWaveWidth)
        mWaveHeight = typedArray.getDimension(R.styleable.CircleVerticalView_waveHeight,mWaveHeight)
        mWaveColor = typedArray.getColor(R.styleable.CircleVerticalView_waveColor,mWaveColor)
        mTextSize = typedArray.getDimension(R.styleable.CircleVerticalView_circleVerticalTextSize,mTextSize)
        mTextColor = typedArray.getColor(R.styleable.CircleVerticalView_circleVerticalTextColor,mTextColor)

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.textSize = mTextSize
        mTextPaint!!.color = mTextColor
        mTextPaint!!.style = Paint.Style.FILL
        mTextPaint!!.strokeWidth = 1f

        mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCirclePaint!!.style = Paint.Style.FILL
        mCirclePaint!!.color = mCircleBgColor

        mWavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mWavePaint!!.style = Paint.Style.FILL
        mWavePaint!!.color = mWaveColor

        mCirclePath = Path()

        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(w > 0 && h > 0){
            mWidth = w.toFloat()
            mHeight = h.toFloat()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(widthMeasureSpec,heightMeasureSpec)
        }else{
            setMeasuredDimension((mViewRadius * 2 + 1f).toInt(),(mViewRadius * 2 + 1).toInt())
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //将画布裁剪成圆形
        mCirclePath!!.addCircle(width.div(2).toFloat(),height.div(2).toFloat(),mViewRadius,Path.Direction.CW)
        canvas!!.clipPath(mCirclePath)
        canvas.drawPaint(mCirclePaint)
        canvas.drawCircle(width.div(2).toFloat(),height.div(2).toFloat(),mViewRadius,mCirclePaint)

        canvas.drawPath(drawWave(),mWavePaint)

        val centerX = mWidth.div(2).toFloat()
        val centerY = mHeight.div(2).toFloat()

        var text = String.format("%.2f",mProgressHeight.div(mViewRadius * 2) * 100).plus("%")
        val textWidth = mTextPaint!!.measureText(text)
        val textHeight = getFontHeight(mTextPaint!!)
        canvas!!.drawText(text,
                centerX - textWidth.div(2),
                centerY + textHeight.div(2),
                mTextPaint!!)
    }

    /*
    * 画波浪
    * */
    private fun drawWave(): Path {
        //半个波长，也就是sin的二分之一
        val itemWidth = mWaveWidth.div(2)
        //屏幕中需要出现的半波长个数
        val itemNumber = (mWidth.div(mWaveWidth) * 2).toInt() + 1
        val path = Path()
        //起始坐标
        path.moveTo(-itemWidth * 3,mHeight)

        for (i in -3 until itemNumber){
            val startX = itemWidth * i
            path.quadTo(startX+itemWidth.div(2)+mOffset,
                    getWaveHeight(i),
                    startX + itemWidth + mOffset,
                    mHeight - mProgressHeight)
        }

        path.lineTo(mWidth,mHeight)
        path.lineTo(0f,mHeight)
        path.close()

        return path
    }

    /*
    * 得到波浪在Y轴上的高度（最高点和最低点）
    * */
    private fun getWaveHeight(i: Int): Float{
        if(i % 2 == 0){
            return mHeight - mWaveHeight - mProgressHeight
        }else{
            return mHeight + mWaveHeight - mProgressHeight
        }
    }

    /*
    * 开启动画
    * */
    private fun startVerticalAnimation() {
        val verticalAnimator = ObjectAnimator.ofFloat(0f,mVerticalPercent)
        val horizontalAnimator = ObjectAnimator.ofFloat(0f,mWaveWidth)
        horizontalAnimator.repeatCount = ObjectAnimator.INFINITE
        horizontalAnimator.interpolator = LinearInterpolator()

        verticalAnimator.addUpdateListener {
            mProgressHeight = verticalAnimator.getAnimatedValue() as Float
            invalidate()
        }

        horizontalAnimator.addUpdateListener {
            mOffset = horizontalAnimator.getAnimatedValue() as Float
            invalidate()
        }

        horizontalAnimator.duration = 2500
        verticalAnimator.duration = 8000
        horizontalAnimator.start()
        verticalAnimator.start()

    }

    private fun getFontHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.descent - fm.ascent
    }

    /*
    * 如果在xml中没有设置mVerticalPercent，可以在这个方法中设置百分比
    * */
    fun setVerticalProgress(progress: Float) {
        if(mHeight != 0f){
            mVerticalPercent = progress * mHeight .div(100f)
        }else{
            mVerticalPercent = progress * mViewRadius .div(50f)
        }
    }

    fun start() {
        startVerticalAnimation()
    }





}