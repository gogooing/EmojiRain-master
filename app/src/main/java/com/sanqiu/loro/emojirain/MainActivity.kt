package com.sanqiu.loro.emojirain

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        ribbonView.start()
    }

    override fun onPause() {
        super.onPause()
        ribbonView.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        ribbonView.clean()
    }
}
