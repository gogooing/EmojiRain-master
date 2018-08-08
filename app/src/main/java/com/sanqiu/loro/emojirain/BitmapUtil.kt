package com.sanqiu.loro.emojirain

import android.graphics.*
import android.util.Log


class BitmapUtil private constructor() {
    internal companion object {

        /**
         * r,g,b,a 的数值
         */
        fun getColorBitmap(baseBitmap: Bitmap, r: Float, g: Float, b: Float, a: Float): Bitmap {
            // 获取一个与baseBitmap大小一致的可编辑的空图片
            val afterBitmap = Bitmap.createBitmap(baseBitmap.width,
                    baseBitmap.height, baseBitmap.config)
            val canvas = Canvas(afterBitmap)
            val paint = Paint()

            val progressR = r / 128f
            val progressG = g / 128f
            val progressB = b / 128f
            val progressA = a / 128f
            Log.i("main", "R：G：B=$progressR：$progressG：$progressB")
            // 根据SeekBar定义RGBA的矩阵
            val src = floatArrayOf(progressR, 0f, 0f, 0f, 0f, 0f, progressG, 0f, 0f, 0f, 0f, 0f, progressB, 0f, 0f, 0f, 0f, 0f, progressA, 0f)
            // 定义ColorMatrix，并指定RGBA矩阵
            val colorMatrix = ColorMatrix()
            colorMatrix.set(src)
            // 设置Paint的颜色
            paint.colorFilter = ColorMatrixColorFilter(src)
            // 通过指定了RGBA矩阵的Paint把原图画到空白图片上
            canvas.drawBitmap(baseBitmap, Matrix(), paint)
            return afterBitmap
        }
    }
}