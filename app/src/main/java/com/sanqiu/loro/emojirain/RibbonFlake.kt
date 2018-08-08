package com.domobile.pixelworld.ui.widget

import android.graphics.*
import android.graphics.Bitmap
import com.sanqiu.loro.emojirain.Random
import com.sanqiu.loro.emojirain.recycleSafety


class RibbonFlake(private val mUuid: Int, private val mPoint: Point, private var mAngle: Float, private val mIncrement: Float, private val mScale: Float, private val mPaint: Paint, private val index: Int, private val rotate: Float) {
    private var mFlakeSize: Float = 1f
    private var mRotate: Float = 0f

    fun getIndex(): Int {
        return index
    }

    fun getUuid(): Int {
        return mUuid
    }

    fun draw(canvas: Canvas, flakeBitmap: Bitmap): Boolean {
        mFlakeSize = flakeBitmap.width * mScale
        val width = canvas.width
        val height = canvas.height
        val hasRemove = move(width, height)

        val bounds = RectF(0f, 0f, (flakeBitmap!!.width * mScale), (flakeBitmap!!.height * mScale))
        bounds.offset(mPoint.x.toFloat(), mPoint.y.toFloat())
        mPaint.isFilterBitmap = true
        mPaint.flags = 0
        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG and Paint.FILTER_BITMAP_FLAG)

        // Matrix类进行图片处理（缩小或者旋转）
        val matrix1 = Matrix()
        // 旋转
        mRotate += rotate
        matrix1.postRotate(mRotate)
        // 生成新的图片
        val dstbmp = Bitmap.createBitmap(flakeBitmap, 0, 0, flakeBitmap.width,
                flakeBitmap.height, matrix1, true)
        canvas.drawBitmap(dstbmp, null, bounds, mPaint)
        dstbmp.recycleSafety()
        return hasRemove
    }

    fun draw(canvas: Canvas, flakeBitmap: Bitmap, widthPixel: Int, allWidth: Int): Boolean {
        mFlakeSize = flakeBitmap.width * mScale
        val width = canvas.width
        val height = canvas.height
        val hasRemove = move(widthPixel, height)
        val mpax = allWidth - widthPixel

        val bounds = RectF(0f, 0f, (flakeBitmap!!.width * mScale), (flakeBitmap!!.height * mScale))
        bounds.offset(mPoint.x.toFloat() + mpax, mPoint.y.toFloat())
        mPaint.isFilterBitmap = true
        mPaint.flags = 0
        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG and Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(flakeBitmap, null, bounds, mPaint)
        return hasRemove
    }

    private fun move(width: Int, height: Int): Boolean {
        var x = mPoint.x + mIncrement * Math.cos(mAngle.toDouble()) * mScale
        if (x < 0) {
            x = width - x
        }
        if (x > width) {
            x -= width
        }
        val y = mPoint.y + mIncrement * Math.sin(mAngle.toDouble()) * mScale
        mAngle += mRandom.getRandom(-ANGLE_SEED * mScale, ANGLE_SEED * mScale) / ANGLE_DIVISOR
        mPoint.set(x.toInt(), y.toInt())

        if (isInside(height)) {
            return false
        }
        return true
//        if (!isInside(width, height)) {
//            reset(width)
//        }
    }

    private fun isInside(height: Int): Boolean {
        val y = mPoint.y
        return y <= height
    }

    private fun reset(width: Int) {
        mPoint.x = mRandom.getRandom(width)
        mPoint.y = (-mFlakeSize - 1).toInt()
        mAngle = mRandom.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE
    }

    companion object {
        private const val ANGE_RANGE = 0.3f //默认的倾斜
        private const val HALF_ANGLE_RANGE = ANGE_RANGE / 2f //倾斜角度范围
        private const val HALF_PI = Math.PI.toFloat() / 2f
        private const val ANGLE_SEED = 50f
        private const val ANGLE_DIVISOR = 10000f
        private const val INCREMENT_LOWER = 2f //增量最低
        private const val INCREMENT_UPPER = 4f //增量最高
        private val mRandom = Random()

        fun create(uuid: Int, width: Int, paint: Paint, scale: Float, index: Int): RibbonFlake {
            val x = mRandom.getRandom(width)
            val y = -mRandom.getRandom(width)
            val point = Point(x, y)
            val angle = mRandom.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE
            val increment = mRandom.getRandom(INCREMENT_LOWER, INCREMENT_UPPER)

            val ratate = mRandom.getRandom(-20f, 20f)
            return RibbonFlake(uuid, point, angle, increment, scale, paint, index, ratate)
        }
    }


}
