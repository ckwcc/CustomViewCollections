package com.ckw.customviewcollections.radar_view

import android.animation.Animator
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
 * on 2018/6/26.
 */
class RadarView : View {
    
    /*
    * 整个View的宽高
    * */
    private var mWidth = 0f
    private var mHeight = 0f

    /*
    * 六边形边界一个边长的宽度（最大）
    * */
    private var mRadarBorderWidth = 200f

    private var mBorderPaint: Paint? = null
    private var mBorderColor = Color.BLACK
    private var mBorderStroke = 1f

    /*
    * 雷达图外部的文字
    * */
    private var mTextPaint: Paint? = null
    private var mTextColor = Color.RED
    private var mTextSize = 30f

    private var mInnerTextPaint: Paint? = null
    private var mInnerTextColor = Color.BLUE
    private var mInnerTextSize = 16f
    private var mShowInnerText = true
    private var mTimeToShowInnerText = false

    /*
    * 进度背景
    * */
    private var mProgressPaint: Paint? = null
    private var mProgressColor = Color.RED
    private var mProgressAlpha = 30

    /*
    * 雷达图背景
    * */
    private var mBgPaint: Paint? = null
    private var mBgColor = Color.WHITE
    private var mBgAlpha = 255

    /*
    * 雷达图文字
    * mTextOffsetHeight 由A点高度决定
    * mTextOffsetWidth 由B、C点较宽的点决定
    * */
    private var mTextOffsetHeight = 0f
    private var mTextOffsetWidth = 0f
    private var mTextMargin = 20f

    private var mAnimDuration = 1500

    private var mStrA = ""
    private var mStrB = ""
    private var mStrC = ""
    private var mStrD = ""
    private var mStrE = ""
    private var mStrF = ""

    private var mProgressA = 0f
    private var mProgressB = 0f
    private var mProgressC = 0f
    private var mProgressD = 0f
    private var mProgressE = 0f
    private var mProgressF = 0f

    private var mCurrentA = 0f
    private var mCurrentB = 0f
    private var mCurrentC = 0f
    private var mCurrentD = 0f
    private var mCurrentE = 0f
    private var mCurrentF = 0f

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context!!,attrs!!)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarView)

        mRadarBorderWidth = typedArray.getDimension(R.styleable.RadarView_radarBorderWidth,mRadarBorderWidth)
        mAnimDuration = typedArray.getInteger(R.styleable.RadarView_radarAnimDuration,mAnimDuration)
        mBorderColor = typedArray.getColor(R.styleable.RadarView_radarBorderColor,mBorderColor)
        mTextColor = typedArray.getColor(R.styleable.RadarView_radarOuterTextColor,mTextColor)
        mTextSize = typedArray.getDimension(R.styleable.RadarView_radarOuterTextSize,mTextSize)
        mInnerTextColor = typedArray.getColor(R.styleable.RadarView_radarInnerTextColor,mInnerTextColor)
        mInnerTextSize = typedArray.getDimension(R.styleable.RadarView_radarInnerTextSize,mInnerTextSize)
        mShowInnerText = typedArray.getBoolean(R.styleable.RadarView_radarShowInnerText,mShowInnerText)
        mProgressColor = typedArray.getColor(R.styleable.RadarView_radarProgressColor,mProgressColor)
        mProgressAlpha = typedArray.getInteger(R.styleable.RadarView_radarProgressAlpha,mProgressAlpha)
        mBgColor = typedArray.getColor(R.styleable.RadarView_radarBackgroundColor,mBgColor)
        mBgAlpha = typedArray.getInteger(R.styleable.RadarView_radarBackgroundAlpha,mBgAlpha)
        mTextMargin = typedArray.getDimension(R.styleable.RadarView_radarOuterTextMargin,mTextMargin)

        if (mProgressAlpha < 0){
            mProgressAlpha = 0
        }else if(mProgressAlpha > 255){
            mProgressAlpha = 255
        }

        if (mBgAlpha < 0){
            mBgAlpha = 0
        }else if(mBgAlpha > 255){
            mBgAlpha = 255
        }

        mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBorderPaint!!.color = mBorderColor
        mBorderPaint!!.strokeWidth = mBorderStroke
        mBorderPaint!!.style = Paint.Style.STROKE

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.color = mTextColor
        mTextPaint!!.style = Paint.Style.FILL
        mTextPaint!!.textSize = mTextSize

        mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBgPaint!!.color = mBgColor
        mBgPaint!!.style = Paint.Style.FILL
        mBgPaint!!.alpha = mBgAlpha

        mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mProgressPaint!!.style = Paint.Style.FILL
        mProgressPaint!!.color = mProgressColor
        mProgressPaint!!.alpha = mProgressAlpha

        mInnerTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mInnerTextPaint!!.style = Paint.Style.STROKE
        mInnerTextPaint!!.color = mInnerTextColor
        mInnerTextPaint!!.textSize = mInnerTextSize

        typedArray.recycle()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val suggestWidth = MeasureSpec.getSize(widthMeasureSpec)
        val suggestHeight = MeasureSpec.getSize(heightMeasureSpec)

        mWidth = suggestWidth.toFloat()
        val height = mRadarBorderWidth * 2 + mTextMargin * 2 + 140f
        if(heightMode == MeasureSpec.EXACTLY){
            if(suggestHeight > height){
                mHeight = suggestHeight.toFloat()
                setMeasuredDimension(suggestWidth,suggestHeight)
            }else{
                mHeight = height
                setMeasuredDimension(suggestWidth,height.toInt())
            }
        }else{
            mHeight = height
            setMeasuredDimension(suggestWidth,height.toInt())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas!!.save()

        //先画整个背景
        val radarWidth = Math.sqrt(3.0) * mRadarBorderWidth
        val horizontalOffset = (mWidth - radarWidth).div(2).toFloat()
        val verticalOffset = (mHeight - mRadarBorderWidth * 2).div(2)
        canvas!!.translate(horizontalOffset,verticalOffset)
        drawRadarBackground(canvas)

        //画雷达图线条
        for (index in 0 until 5){
            drawRadarBorder(canvas,index * 0.2f)
        }

        drawLine(canvas)


        canvas.restore()

        canvas.save()
        canvas.translate(horizontalOffset - mTextOffsetWidth,verticalOffset - mTextOffsetHeight)

        drawText(canvas)

        drawAnim(canvas)


    }


    /*
    * 绘制动态图形(包括内部的文字)
    * */
    private fun drawAnim(canvas: Canvas){

        val cons = Math.sqrt(3.0).div(2)
        val base = (Math.sqrt(3.0) * mRadarBorderWidth.div(2)).toFloat()
        //这里这么做的原因是上面已经将画布的坐标系移回去了，否则不需要再加textOffset
        val centerX = mTextOffsetWidth + base
        val centerY = mTextOffsetHeight + mRadarBorderWidth

        val path = Path()
        //绘制A点的动态坐标
        path.moveTo(centerX,(centerY - mCurrentA.div(100) * mRadarBorderWidth).toFloat())
        //绘制B点
        path.lineTo((centerX - mCurrentB.div(100) * mRadarBorderWidth * cons).toFloat(),
                (centerY - mCurrentB.div(100)*mRadarBorderWidth.div(2)).toFloat())

        //绘制C点
        path.lineTo((centerX - mCurrentC.div(100) * mRadarBorderWidth * cons).toFloat(),
                (centerY + mCurrentC.div(100) * mRadarBorderWidth.div(2)).toFloat())

        //绘制D点
        path.lineTo(centerX,(centerY + mCurrentD.div(100) * mRadarBorderWidth).toFloat())

        //绘制E点
        path.lineTo((centerX + mCurrentE.div(100) * mRadarBorderWidth * cons).toFloat(),
                (centerY + mCurrentE.div(100)*mRadarBorderWidth.div(2)).toFloat())

        //绘制F点
        path.lineTo((centerX + mCurrentF.div(100) * mRadarBorderWidth * cons).toFloat(),
                (centerY - mCurrentF.div(100)*mRadarBorderWidth.div(2)).toFloat())

        path.close()

        canvas.drawPath(path,mProgressPaint!!)

        if(mShowInnerText && mTimeToShowInnerText){
            val measureWidthA = mInnerTextPaint!!.measureText(mProgressA.toString())
            canvas.drawText(mProgressA.toString(),centerX - measureWidthA.div(2),(centerY - mProgressA.div(100) * mRadarBorderWidth).toFloat(),mInnerTextPaint!!)

            val measureWidthB = mInnerTextPaint!!.measureText(mProgressB.toString())
            canvas.drawText(mProgressB.toString(),(centerX - mProgressB.div(100) * mRadarBorderWidth * cons - measureWidthB).toFloat(),
                    (centerY - mProgressB.div(100)*mRadarBorderWidth.div(2)).toFloat(),mInnerTextPaint!!)

            val measureWidthC = mInnerTextPaint!!.measureText(mProgressC.toString())
            canvas.drawText(mProgressC.toString(),(centerX - mProgressC.div(100) * mRadarBorderWidth * cons - measureWidthC).toFloat(),
                    (centerY + mProgressC.div(100)*mRadarBorderWidth.div(2)).toFloat(),mInnerTextPaint!!)

            val measureWidthD = mInnerTextPaint!!.measureText(mProgressD.toString())
            val measureHeightD = getFontHeight(mInnerTextPaint!!)
            canvas.drawText(mProgressD.toString(),centerX - measureWidthD.div(2),(centerY + mProgressD.div(100) * mRadarBorderWidth + measureHeightD).toFloat(),mInnerTextPaint!!)

            canvas.drawText(mProgressE.toString(),(centerX + mProgressE.div(100) * mRadarBorderWidth * cons).toFloat(),
                    (centerY + mProgressE.div(100)*mRadarBorderWidth.div(2)).toFloat(),mInnerTextPaint!!)

            canvas.drawText(mProgressF.toString(),(centerX + mProgressF.div(100) * mRadarBorderWidth * cons).toFloat(),
                    (centerY - mProgressF.div(100)*mRadarBorderWidth.div(2)).toFloat(),mInnerTextPaint!!)
        }


    }


    /*
    * 绘制雷达图的背景
    * */
    private fun drawRadarBackground(canvas: Canvas){
        //根号三
        val sqrt = Math.sqrt(3.0)
        //二分之根号三的mRadarBorderWidth
        val base = (sqrt * mRadarBorderWidth.div(2)).toFloat()
        val bgPath = Path()
        bgPath.moveTo( base,0f ) // A
        bgPath.lineTo(0f , mRadarBorderWidth.div(2) )// B
        bgPath.lineTo(0f , mRadarBorderWidth.div(2) * 3 )// C
        bgPath.lineTo(base, mRadarBorderWidth * 2 )// D
        bgPath.lineTo(base * 2 , mRadarBorderWidth.div(2) * 3 )// E
        bgPath.lineTo(base * 2 , mRadarBorderWidth.div(2) )// F
        bgPath.close()

        canvas.drawPath(bgPath,mBgPaint!!)
    }

    /*
    * 绘制雷达图外侧的文字
    * */
    private fun drawText(canvas: Canvas) {
        //二分之根号三的mRadarBorderWidth
        val base = (Math.sqrt(3.0) * mRadarBorderWidth.div(2)).toFloat()
        val textWidthA = mTextPaint!!.measureText(mStrA)
        val textHeight = getFontHeight(mTextPaint!!)
        //mTextOffsetHeight 由A点决定
        mTextOffsetHeight = textHeight + mTextMargin


        //mTextOffsetWidth 由 B C 中较宽的决定
        val widthB = mTextPaint!!.measureText(mStrB)
        val widthC = mTextPaint!!.measureText(mStrC)
        var offset = Math.abs((widthB - widthC))
        if(widthB > widthC){
            mTextOffsetWidth = widthB + mTextMargin
            canvas.drawText(mStrB,0f,mTextOffsetHeight + mRadarBorderWidth.div(2),mTextPaint)//B
            canvas.drawText(mStrC,offset + 0f,mTextOffsetHeight + mRadarBorderWidth.div(2) * 3,mTextPaint)//C
        }else{
            mTextOffsetWidth = widthC + mTextMargin
            canvas.drawText(mStrB,offset + 0f,mTextOffsetHeight + mRadarBorderWidth.div(2),mTextPaint)//B
            canvas.drawText(mStrC,0f,mTextOffsetHeight + mRadarBorderWidth.div(2) * 3,mTextPaint)//C
        }
        canvas.drawText(mStrA,mTextOffsetWidth + base - textWidthA.div(2),textHeight,mTextPaint)//A


        //D点的文字
        val textWidthD = mTextPaint!!.measureText(mStrD)
        canvas.drawText(mStrD,mTextOffsetWidth + base - textWidthD.div(2),textHeight * 2 + mRadarBorderWidth * 2 + mTextMargin * 2,mTextPaint)

        //E点坐标的文字
        canvas.drawText(mStrE,base * 2 + mTextOffsetWidth + mTextMargin,mTextOffsetHeight + mRadarBorderWidth.div(2) * 3,mTextPaint)

        //F点坐标的文字
        canvas.drawText(mStrF,base * 2 + mTextOffsetWidth + mTextMargin,mTextOffsetHeight + mRadarBorderWidth.div(2),mTextPaint)

    }

    /*
    * 绘制雷达图的六边形边框
    * */
    private fun drawRadarBorder(canvas: Canvas,percent: Float) {
        //根号三
        val sqrt = Math.sqrt(3.0)
        //二分之根号三的mRadarBorderWidth
        val base = (sqrt * mRadarBorderWidth.div(2)).toFloat()
        val out = Path()
        out.moveTo( base,0f + mRadarBorderWidth * percent) // A
        out.lineTo(0f + base * percent, mRadarBorderWidth.div(2) + mRadarBorderWidth.div(2) * percent)// B
        out.lineTo(0f + base * percent, mRadarBorderWidth.div(2) * 3 - mRadarBorderWidth.div(2) * percent)// C
        out.lineTo(base, mRadarBorderWidth * 2 - mRadarBorderWidth * percent)// D
        out.lineTo(base * 2 - base * percent, mRadarBorderWidth.div(2) * 3 - mRadarBorderWidth.div(2) * percent)// E
        out.lineTo(base * 2 - base * percent, mRadarBorderWidth.div(2) + mRadarBorderWidth.div(2) * percent)// F
        out.close()

        canvas.drawPath(out,mBorderPaint)
    }

    /*
    * 画雷达图内部的直线
    * */
    private fun drawLine(canvas: Canvas) {
        //二分之根号三的mRadarBorderWidth
        val base = (Math.sqrt(3.0) * mRadarBorderWidth.div(2)).toFloat()

        canvas.drawLine( base, 0f, base, mRadarBorderWidth * 2,mBorderPaint)//A - D
        canvas.drawLine( 0f, mRadarBorderWidth.div(2), base * 2,mRadarBorderWidth.div(2) * 3,mBorderPaint)//B - E
        canvas.drawLine( 0f, mRadarBorderWidth.div(2) * 3, base * 2 ,mRadarBorderWidth.div(2),mBorderPaint)//C - F

    }

    /*
    * 得到文字的高度
    * */
    private fun getFontHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.descent - fm.ascent
    }

    /*
    * 开启动画
    * */
    private fun startAnim(){
        val animatorA = ValueAnimator.ofFloat(0f, mProgressA)
        animatorA.addUpdateListener {
            mCurrentA = it.animatedValue as Float
            invalidate()
        }
        animatorA.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                mTimeToShowInnerText = true
                invalidate()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }


        })
        animatorA.duration = mAnimDuration.toLong()
        animatorA.start()

        val animatorB = ValueAnimator.ofFloat(0f, mProgressB)
        animatorB.addUpdateListener {
            mCurrentB = it.animatedValue as Float
            invalidate()
        }
        animatorB.duration = mAnimDuration.toLong()
        animatorB.start()

        val animatorC = ValueAnimator.ofFloat(0f, mProgressC)
        animatorC.addUpdateListener {
            mCurrentC = it.animatedValue as Float
            invalidate()
        }
        animatorC.duration = mAnimDuration.toLong()
        animatorC.start()

        val animatorD = ValueAnimator.ofFloat(0f, mProgressD)
        animatorD.addUpdateListener {
            mCurrentD = it.animatedValue as Float
            invalidate()
        }
        animatorD.duration = mAnimDuration.toLong()
        animatorD.start()

        val animatorE = ValueAnimator.ofFloat(0f, mProgressE)
        animatorE.addUpdateListener {
            mCurrentE = it.animatedValue as Float
            invalidate()
        }
        animatorE.duration = mAnimDuration.toLong()
        animatorE.start()

        val animatorF = ValueAnimator.ofFloat(0f, mProgressF)
        animatorF.addUpdateListener {
            mCurrentF = it.animatedValue as Float
            invalidate()
        }
        animatorF.duration = mAnimDuration.toLong()
        animatorF.start()
    }

    //////////////////////////////////////////////////////////////
    /*
    * 设置六个点的进度
    * */
    fun setRadarProgress(doubleA: Float,doubleB: Float,doubleC: Float,doubleD: Float,doubleE: Float,doubleF: Float){
        mProgressA = doubleA
        mProgressB = doubleB
        mProgressC = doubleC
        mProgressD = doubleD
        mProgressE = doubleE
        mProgressF = doubleF
        startAnim()
    }

    /*
    * 设置六个点的文字
    * */
    fun setRadarStrings(a: String,b: String,c: String,d: String,e: String,f: String) {
        mStrA = a
        mStrB = b
        mStrC = c
        mStrD = d
        mStrE = e
        mStrF = f
    }


}