package com.ckw.customviewcollections.playpause_view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/21.
 */
class PlayPauseView : View {

    /*
    * 控件整体的宽高
    * */
    private var mWidth = 0f
    private var mHeight = 0f

    private var mRadius = 0f

    /*
    * 矩形条的宽高
    * */
    private var mBarWidth = 20f
    private var mBarHeight = 60f

    /*
    * 两个矩形条的间距
    * */
    private var mBarSpace = 20f

    /*
    * 矩形距离原点的padding
    * */
    private var mPadding = 20f

    private var mBgColor = Color.WHITE
    private var mBarColor = Color.BLUE

    private var mBgPaint: Paint? = null
    private var mBarPaint: Paint? = null

    /*
    * 根据动画变的进度
    * */
    private var mProgress = 0f

    private var mLeftPath: Path? = null
    private var mRightPath: Path? = null

    private var isPlaying = true
    /*
    * 是否顺时针旋转
    * */
    private var isClockWise = false

    /*
    * 对外提供的监听器
    * */
    var mPlayPauseListener: PlayPauseListener? = null

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context!!,attrs!!)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlayPauseView)
        mBarColor = typedArray.getColor(R.styleable.PlayPauseView_barColor,mBarColor)
        mBgColor = typedArray.getColor(R.styleable.PlayPauseView_barBgColor,mBgColor)
        isClockWise = typedArray.getBoolean(R.styleable.PlayPauseView_barClockWise,isClockWise)
        isPlaying = typedArray.getBoolean(R.styleable.PlayPauseView_barPlayingState,isPlaying)

        mBarWidth = typedArray.getDimension(R.styleable.PlayPauseView_barWidth,10 * getDensity())
        mBarHeight = typedArray.getDimension(R.styleable.PlayPauseView_barHeight,30 * getDensity())
        mPadding = typedArray.getDimension(R.styleable.PlayPauseView_barPadding,10 * getDensity())
        //可以通过上面的三个参数计算出下面的参数值，所以不再通过xml设置
        mBarSpace = mBarHeight - mBarWidth * 2
        mRadius = mBarWidth + mBarSpace.div(2) + mPadding
        mWidth = mRadius * 2
        mHeight = mRadius * 2

        if(isPlaying){
            mProgress = 0f
        }else{
            mProgress = 1f
        }

        mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBgPaint!!.color = mBgColor
        mBgPaint!!.style = Paint.Style.FILL

        mBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBarPaint!!.color = mBarColor
        mBarPaint!!.style = Paint.Style.FILL

        mLeftPath = Path()
        mRightPath = Path()

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
                mWidth = mRadius * 2
                mHeight = mRadius * 2
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

        mLeftPath!!.rewind()
        mRightPath!!.rewind()

        mRadius = mWidth.div(2)
        canvas!!.drawCircle(mWidth.div(2),mHeight.div(2),mRadius,mBgPaint)

        if(isClockWise){
            mLeftPath!!.moveTo(mPadding + (mBarWidth + mBarSpace.div(2)) * mProgress ,mPadding)
            mLeftPath!!.lineTo(mPadding ,mPadding + mBarHeight)
            mLeftPath!!.lineTo(mPadding + mBarWidth + mBarSpace.div(2) * mProgress,mPadding + mBarHeight)
            mLeftPath!!.lineTo(mPadding + mBarWidth + mBarSpace.div(2) * mProgress,mPadding)
            mLeftPath!!.close()

            mRightPath!!.moveTo(mPadding + mBarWidth + mBarSpace - mBarSpace.div(2) * mProgress,mPadding)
            mRightPath!!.lineTo(mPadding + mBarWidth + mBarSpace - mBarSpace.div(2) * mProgress,mPadding + mBarHeight)
            mRightPath!!.lineTo(mPadding + mBarWidth * 2 + mBarSpace ,mPadding + mBarHeight)
            mRightPath!!.lineTo(mPadding + mBarWidth * 2 + mBarSpace - (mBarWidth + mBarSpace.div(2)) * mProgress,mPadding)
            mRightPath!!.close()
        }else{
            mLeftPath!!.moveTo(mPadding,mPadding)
            mLeftPath!!.lineTo(mPadding + (mBarWidth + mBarSpace.div(2)) * mProgress,mPadding + mBarHeight)
            mLeftPath!!.lineTo(mPadding + mBarWidth + mBarSpace.div(2) * mProgress,mPadding + mBarHeight)
            mLeftPath!!.lineTo(mPadding + mBarWidth + mBarSpace.div(2) * mProgress,mPadding)
            mLeftPath!!.close()

            mRightPath!!.moveTo(mPadding + mBarWidth + mBarSpace - mBarSpace.div(2) * mProgress,mPadding)
            mRightPath!!.lineTo(mPadding + mBarWidth + mBarSpace - mBarSpace.div(2) * mProgress,mPadding + mBarHeight)
            mRightPath!!.lineTo(mPadding + mBarWidth * 2 + mBarSpace - (mBarWidth + mBarSpace.div(2)) * mProgress,mPadding + mBarHeight)
            mRightPath!!.lineTo(mPadding + mBarWidth * 2 + mBarSpace,mPadding)
            mRightPath!!.close()
        }

        var corner = 0
        if(isClockWise){
            corner = 90
        }else{
            corner = -90
        }

        val rotation = corner * mProgress
        canvas.rotate(rotation,mWidth.div(2),mHeight.div(2))

        canvas.drawPath(mLeftPath!!,mBarPaint)
        canvas.drawPath(mRightPath!!,mBarPaint)
    }

    /*
    * 设置动画
    * */
    private fun getAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(if (isPlaying) 1f else 0f, if (isPlaying) 0f else 1f)
        valueAnimator.duration = 200
        valueAnimator.addUpdateListener {
            mProgress = it.animatedValue as Float
            invalidate()
        }

        return valueAnimator

    }

    private fun play() {
        getAnimator().cancel()
        setPlaying(true)
        getAnimator().start()
    }

    private fun pause() {
        getAnimator().cancel()
        setPlaying(false)
        getAnimator().start()
    }

    private fun setPlaying(isPlaying: Boolean) {
        this.isPlaying = isPlaying
    }

    private fun getDensity(): Float {
        return resources.displayMetrics.density
    }

    /*
    * 设置监听器
    * */
    fun setPlayPauseListener(listener: PlayPauseListener) {
        mPlayPauseListener = listener

        setOnClickListener {
            if(isPlaying){
                pause()
                mPlayPauseListener!!.pause()
            }else{
                play()
                mPlayPauseListener!!.play()
            }
        }
    }


    interface PlayPauseListener{
        fun play()
        fun pause()
    }


}