package com.ckw.customviewcollections.tag_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.ckw.customviewcollections.KLogUtil
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/13.
 */
class TagView : View {

    private var mWidth = 0f;
    private var mHeight = 0f;

    private var mBgColor = Color.YELLOW
    private var mTextSize = 20f
    private var mTextColor = Color.WHITE
    private var mTagText:String = "tag"

    private var mCornerRadius = 20f

    private var mBgPaint: Paint? = null
    private var mTextPaint: Paint? = null

    private var mContext: Context? = null

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context!!,attrs);
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mContext = context
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagView)
        mBgColor = typedArray.getColor(R.styleable.TagView_tagViewBgColor,mBgColor)
        mTextSize = typedArray.getDimension(R.styleable.TagView_tagViewTextSize,mTextSize)
        mTextColor = typedArray.getColor(R.styleable.TagView_tagViewTextColor,mTextColor)
        if(typedArray.getString(R.styleable.TagView_tagViewText) != null){
            mTagText = typedArray.getString(R.styleable.TagView_tagViewText)
        }
        mCornerRadius = typedArray.getDimension(R.styleable.TagView_tagViewCorner,mCornerRadius)

        mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBgPaint!!.color = mBgColor
        mBgPaint!!.style = Paint.Style.FILL

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.textSize = mTextSize
        mTextPaint!!.color = mTextColor
        mTextPaint!!.style = Paint.Style.STROKE

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var suggestWidth = MeasureSpec.getSize(widthMeasureSpec)
        var suggestHeight = MeasureSpec.getSize(heightMeasureSpec)

        if(widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST){
            val textWidth = mTextPaint!!.measureText(mTagText)
            suggestWidth = (textWidth.div(4) * 5 ).toInt()
            suggestHeight = (getFontHeight(mTextPaint!!) + 20).toInt()
        }

        setMeasuredDimension(suggestWidth,suggestHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(w > 0 && h > 0){
            mWidth = w.toFloat()
            mHeight = h.toFloat()
        }else{
            mWidth = 60f
            mHeight = 30f
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val rectF = RectF(0f,0f,mWidth,mHeight)
        canvas!!.drawRoundRect(rectF,mCornerRadius,mCornerRadius,mBgPaint!!)

        val textWidth = mTextPaint!!.measureText(mTagText)
        val textHeight = getFontHeight(mTextPaint!!)

        canvas.drawText(mTagText,
                mWidth.div(2) - textWidth.div(2),
                mHeight.div(2) + textHeight.div(2) - 4,
                mTextPaint!!)

    }

    private fun getFontHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.descent - fm.ascent
    }

    /*
    * 设置背景颜色
    * */
    fun setTagViewBackground(bgColor: Int) {
        mBgColor = bgColor
        mBgPaint!!.color = mBgColor
        invalidate()
    }

    /*
    * 设置文字
    * */
    fun setTagViewText(text: String) {
        mTagText = text
        invalidate()
    }

    /*
    * 设置文字大小
    * */
    fun setTagViewTextSize(size: Int) {
        mTextSize = dip2px(mContext!!,size.toFloat()).toFloat()
        mTextPaint!!.textSize = mTextSize
        invalidate()
    }

    /*
    * 设置文字的颜色
    * */
    fun setTagViewTextColor(color: Int) {
        mTextColor = color
        mTextPaint!!.color = mTextColor
        invalidate()
    }

    /*
    * 设置圆角
    * */
    fun setTagViewCorner(corner: Int) {
        mCornerRadius = dip2px(mContext!!,corner.toFloat()).toFloat()
        invalidate()
    }

    /*
    * 为TagView设置Margin属性，单位是dp
    * */
    fun setMargins(left: Int,top: Int,right: Int,bottom: Int) {
        val marginLayoutParams: ViewGroup.MarginLayoutParams =
                ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        marginLayoutParams.leftMargin = dip2px(mContext!!,left.toFloat())
        marginLayoutParams.topMargin = dip2px(mContext!!,top.toFloat())
        marginLayoutParams.rightMargin = dip2px(mContext!!,right.toFloat())
        marginLayoutParams.bottomMargin = dip2px(mContext!!,bottom.toFloat())
        this.layoutParams = marginLayoutParams

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


}