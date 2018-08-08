package com.sanqiu.loro.emojirain

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Point
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import java.util.*

// SETTINGS，系统设置包名
const val PKG_SETTINGS = "com.android.settings"

// ==========================Bitmap==========================
/**
 * 安全回收bitmap
 */
fun Bitmap.recycleSafety() {
    if (!this.isRecycled)
        this.recycle()
}
// ==========================Bitmap==========================
