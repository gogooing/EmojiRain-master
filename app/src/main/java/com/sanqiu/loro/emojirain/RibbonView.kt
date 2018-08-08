package com.domobile.pixelworld.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.sanqiu.loro.emojirain.BitmapUtil

import com.sanqiu.loro.emojirain.R
import com.sanqiu.loro.emojirain.Random
import com.sanqiu.loro.emojirain.recycleSafety

/**
 * Created by loro
 */

class RibbonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    private var mFlakesDensity = DEFAULT_FLAKES_DENSITY
    private var mBitmapCount = DEFAULT_BITMAP_COUNT
    private var mDelay = DEFAULT_DELAY
    private var mFlakes: Array<RibbonFlake?>? = null
    private var mFlakeBitmaps: MutableList<Bitmap> = mutableListOf() //所有彩带的图片
    private var mRemoveCount: MutableMap<Int, RibbonFlake> = mutableMapOf()
    private var mPaint: Paint? = null
    private var mImgId: Int = 0
    private var mScale: Float = 0f
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mRawWidth: Int = 0
    private var isRun: Boolean = false

    private val mRunnable = Runnable { invalidate() }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        setBackgroundColor(Color.TRANSPARENT)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.RibbonView)
            mImgId = a.getResourceId(R.styleable.RibbonView_flakeSrc, R.drawable.ribbon)
            mScale = a.getFloat(R.styleable.RibbonView_flakeScale, DEFAULT_SCALE)
            mFlakesDensity = a.getInt(R.styleable.RibbonView_flakeDensity, DEFAULT_FLAKES_DENSITY)
            mDelay = a.getInt(R.styleable.RibbonView_fallingDelay, DEFAULT_DELAY)

        }
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = Color.WHITE
        mPaint!!.style = Paint.Style.FILL

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            mWidth = w
            mHeight = h
            initRibbon()
        }
    }

    private fun initRibbon() {
        mRawWidth = initScale(mScale)
        initDenstity(mWidth, mHeight, mRawWidth)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (!isRun) return
        mFlakes?.let {
            for (flake in it) {
                val hasRemove = flake!!.draw(canvas!!, mFlakeBitmaps[flake.getIndex()]!!)
                if (hasRemove) {
                    mRemoveCount[flake.getUuid()] = flake
                }
            }
        }
        if (mRemoveCount.size < mFlakes!!.size) {
            handler.postDelayed(mRunnable, mDelay.toLong())
        }
    }

    private fun initDenstity(w: Int, h: Int, rawWidth: Int) {
        mRemoveCount?.clear()
        mFlakes = null

        mFlakes = arrayOfNulls(mFlakesDensity)
        for (i in 0 until mFlakesDensity) {
            mFlakes!![i] = RibbonFlake.create(i, w, mPaint!!, rawWidth / mScale, Random().getRandom(mFlakeBitmaps.size))
        }
    }

    private fun initScale(scale: Float): Int {
        mFlakeBitmaps?.forEach {
            it.recycleSafety()
        }
        mFlakeBitmaps?.clear()

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, mImgId, options)
        val rawWidth = options.outWidth
        mRawWidth = rawWidth
        options.inSampleSize = scale.toInt()
        options.inJustDecodeBounds = false
        val flakeBitmap = BitmapFactory.decodeResource(resources, mImgId, options)

        for (i in 0 until mBitmapCount) {
            val r = Random().getRandom(255) + 1
            val g = Random().getRandom(255) + 1
            val b = Random().getRandom(255) + 1
            mFlakeBitmaps.add(BitmapUtil.getColorBitmap(flakeBitmap, r.toFloat(), g.toFloat(), b.toFloat(), 255f))
        }

        flakeBitmap.recycleSafety()
        return rawWidth
    }


    fun start() {
        isRun = true
        invalidate()
    }

    fun stop() {
        isRun = false
    }

    fun clean() {
        cleanFlake()
        invalidate()
    }

    /**
     * 设置碎片的图片,默认的图片是彩带
     */
    fun setImageResource(imgId: Int) {
        this.mImgId = imgId
        initScale(mScale)
    }

    /**
     * 设置碎片的大小，数值越大，碎片越小，默认值是3
     */
    fun setScale(scale: Float) {
        initScale(scale)
    }

    /**
     * 设置数量，数值越大，碎片越密集,默认值是30
     */
    fun setDensity(density: Int) {
        this.mFlakesDensity = density
        if (mWidth > 0 && mHeight > 0) {
            initDenstity(mWidth, mHeight, mRawWidth)
        }
    }

    /**
     * 设置碎片飘落的速度，数值越大，飘落的越慢，默认值是30
     */
    fun setDelay(delay: Int) {
        this.mDelay = delay
    }

    /**
     * 清除资源
     */
    private fun cleanFlake() {
        mFlakeBitmaps?.forEach {
            it.recycleSafety()
        }
        mFlakeBitmaps?.clear()
        mRemoveCount?.clear()
        mFlakes = null
    }

    companion object {
        private val TAG = RibbonView::class.java.name
        private const val DEFAULT_FLAKES_DENSITY = 30 //多少个
        private const val DEFAULT_BITMAP_COUNT = 10 //多少个
        private const val DEFAULT_DELAY = 30 //速度
        private const val DEFAULT_SCALE = 3f //数值越大，碎片越小，默认值是3
    }
}
