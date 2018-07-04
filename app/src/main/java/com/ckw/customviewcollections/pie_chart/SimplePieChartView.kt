package com.ckw.customviewcollections.pie_chart

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ckw.customviewcollections.KLogUtil
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/29.
 */
class SimplePieChartView : View {

    private var mRadius = 100f
    private var mRectF: RectF? = null

    private var mPercentPaint: Paint? = null
    private var mPathPaint: Paint? = null
    private var mLinePath: Path? = null
    private var mTextPaint: Paint? = null
    private var mTextSize = 20f

    private var mTouchDegree = 0f

    private var mDataList: MutableList<PieBean>? = null
    private var mProgressList: FloatArray? = null

    private var mShouldShowText = true

    private var mPieClickListener: PieClickListener? = null

    private var mCenterX = 0f
    private var mCenterY = 0f

    fun setPieClickListener(pieClickListener: PieClickListener) {
        mPieClickListener = pieClickListener

    }


    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context!!,attrs!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val suggestWidth = MeasureSpec.getSize(widthMeasureSpec)
        val suggestHeigth = MeasureSpec.getSize(heightMeasureSpec)

        if(widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mRadius.toInt() * 4,mRadius.toInt() * 4)
        }else{
            setMeasuredDimension(suggestWidth,suggestHeigth)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

         mCenterX = width.div(2).toFloat()
         mCenterY = height.div(2).toFloat()
        mRectF = RectF(mCenterX - mRadius,mCenterY - mRadius,mCenterX + mRadius,mCenterY + mRadius)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimplePieChartView)
        mRadius = typedArray.getDimension(R.styleable.SimplePieChartView_pieChartRadius,mRadius)
        mShouldShowText = typedArray.getBoolean(R.styleable.SimplePieChartView_pieChartShowText,mShouldShowText)
        mDataList = ArrayList()

        mLinePath = Path()
        mPercentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPercentPaint!!.style = Paint.Style.FILL

        mPathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPathPaint!!.style = Paint.Style.STROKE
        mPathPaint!!.strokeWidth = 1f

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.style = Paint.Style.STROKE
        mTextPaint!!.textSize = mTextSize

        typedArray.recycle()

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mCenterX = width.div(2).toFloat()
        mCenterY = height.div(2).toFloat()


        var startAngle = 0f
        var pathPercent = 0f

        for (index in 0 until mDataList!!.size){
            mLinePath!!.reset()
            val float = mDataList!![index].percent!!
            val color = mDataList!![index].picColor!!
            val percent = float.div(100) * 360

            mPathPaint!!.color = color
            mPercentPaint!!.color = color
            mTextPaint!!.color = color

            pathPercent = startAngle + percent.div(2)


            if(mShouldShowText){
                if(pathPercent >= 0 && pathPercent < 90){
                    drawPathAndText(float,pathPercent,0f,1,1,0,canvas!!)
                }else if(pathPercent >= 90 && pathPercent < 180){
                    drawPathAndText(float,pathPercent,90f,-1,1,1,canvas!!)
                }else if(pathPercent >= 180 && pathPercent < 270){
                    drawPathAndText(float,pathPercent,180f,-1,-1,1,canvas!!)
                }else{
                    drawPathAndText(float,pathPercent,270f,1,-1,0,canvas!!)
                }
            }

            if(mTouchDegree > startAngle && mTouchDegree < (startAngle + percent)){
                mRectF = RectF(mCenterX - mRadius - mRadius.div(10),mCenterY - mRadius - mRadius.div(10),
                        mCenterX + mRadius + mRadius.div(10) ,mCenterY + mRadius + mRadius.div(10))
                canvas!!.drawArc(mRectF,startAngle,mProgressList!![index],true,mPercentPaint)
            }else{
                mRectF = RectF(mCenterX - mRadius,mCenterY - mRadius,mCenterX + mRadius,mCenterY + mRadius)
                canvas!!.drawArc(mRectF,startAngle,mProgressList!![index],true,mPercentPaint)
            }

            startAngle += percent
        }
    }

    /*
    * 绘制圆外的path和文字
    * countDegree 需要减掉的度数
    * xCoefficient x坐标的系数
    * yCoefficient y坐标的系数
    * pathPercent 路径需要绘制的起点的角度
    * float 当前绘制的圆弧角度
    * textOffset 绘制的文字是否需要偏移
    * */
    private fun drawPathAndText(float: Float,pathPercent:Float,countDegree: Float,xCoefficient: Int,yCoefficient: Int,textOffset: Int,canvas: Canvas){
        val sin = Math.sin(Math.toRadians((pathPercent - countDegree).toDouble()))
        val cos = Math.cos(Math.toRadians((pathPercent - countDegree).toDouble()))
        val pathX = (mCenterX + xCoefficient * mRadius * sin).toFloat()
        val pathY = (mCenterY + yCoefficient * mRadius * cos).toFloat()
        val nextX = (mCenterX + xCoefficient * (mRadius.div(2) * 3) * sin).toFloat()
        val nextY = (mCenterY + yCoefficient * (mRadius.div(2) * 3) * cos).toFloat()
        val endX = (mCenterX + xCoefficient * (mRadius.div(2) * 3) * sin + xCoefficient * mRadius.div(4)).toFloat()
        mLinePath!!.moveTo(pathX,pathY)
        mLinePath!!.lineTo(nextX,nextY)
        mLinePath!!.lineTo(endX,nextY)

        canvas.drawPath(mLinePath,mPathPaint)
        canvas.drawText(float.toString().plus("%"),endX - textOffset * mTextPaint!!.measureText(float.toString().plus("%")),nextY,mTextPaint)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when(event!!.action){
            MotionEvent.ACTION_DOWN -> {
                mTouchDegree = getRotationBetweenLines(event.x,event.y)

                var temp = 0f
                for (index in 0 until mDataList!!.size){
                    val float = mDataList!![index].percent!!
                    val percent = float.div(100) * 360
                    if(mTouchDegree > temp && mTouchDegree <= temp + percent){
                        mPieClickListener!!.onPieClick(index,float)
                    }
                    temp += percent
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                
            }
        }
        return true

    }




    /*
    * 开启动画
    * */
    fun startAnim(){
        mProgressList = FloatArray(mDataList!!.size)

        val animatorSet = AnimatorSet()
        val list = mutableListOf<ValueAnimator>()

        for (index in 0 until mDataList!!.size){
            val float = mDataList!![index].percent!!
            val percent = float.div(100) * 360
            val animator = ValueAnimator.ofFloat(percent)
            animator.duration = 2500
            animator.addUpdateListener {
                mProgressList!![index] = it.animatedValue as Float
                invalidate()
            }

            list.add(animator)
        }

        for (index in 0 until mDataList!!.size - 1){
            animatorSet.play(list[index]).with(list[index + 1])
        }

        animatorSet.start()

    }

    /**
     * 获取两条线的夹角
     */
    private fun getRotationBetweenLines(xInView: Float, yInView: Float): Float {

        val centerX = width.div(2).toFloat()
        val centerY = height.div(2).toFloat()
        var rotation = 0.0

        val k1 = (centerY - centerY).toDouble() / (centerX * 2 - centerX)
        val k2 = (yInView - centerY).toDouble() / (xInView - centerX)
        val tmpDegree = Math.atan(Math.abs(k1 - k2) / (1 + k1 * k2)) / Math.PI * 180

        if (xInView > centerX && yInView < centerY) {  //第一象限
            rotation = tmpDegree + 270
        } else if (xInView > centerX && yInView > centerY) {//第四象限
            rotation = tmpDegree
        } else if (xInView < centerX && yInView > centerY) { //第三象限
            rotation = tmpDegree + 90
        } else if (xInView < centerX && yInView < centerY) { //第二象限
            rotation = tmpDegree + 180
        } else if (xInView == centerX && yInView < centerY) {
            rotation = 0.0
        } else if (xInView == centerX && yInView > centerY) {
            rotation = 180.0
        }
        return rotation.toFloat()
    }

    /*
    * 设置数据
    * */
    fun setPieBean(bean: PieBean) {
        mDataList!!.add(bean)
        invalidate()
    }

    interface PieClickListener{
        fun onPieClick(position: Int,percent: Float)
    }
}