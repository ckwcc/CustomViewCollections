package com.ckw.customviewcollections.drag_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

/**
 * Created by ckw
 * on 2018/6/14.
 */
class DragView : View {

    private var mBgPaint: Paint? = null

    private var mDownX = 0f;
    private var mDownY = 0f;

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context!!,attrs!!)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val systemService = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBgPaint!!.color = Color.YELLOW
        mBgPaint!!.style = Paint.Style.FILL_AND_STROKE


    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val rectF = RectF(0f,0f,width.toFloat(),height.toFloat())
        canvas!!.drawRect(rectF,mBgPaint)

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        if(isEnabled){
            when(event!!.action){
                MotionEvent.ACTION_DOWN -> {
                    mDownX = event.x
                    mDownY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val moveX = event.x - mDownX
                    val moveY = event.y - mDownY

                    if(moveX != 0f && moveY != 0f){
                        val l = left + moveX
                        val t = top + moveY
                        val r = right + moveX
                        val b = bottom + moveY

                        this.layout(l.toInt(),t.toInt(),r.toInt(),b.toInt())

                    }
                }

                MotionEvent.ACTION_UP -> {
                    isPressed = false
                }

                MotionEvent.ACTION_CANCEL -> {
                    isPressed = false
                }
            }

            return true
        }

        return false

    }



}