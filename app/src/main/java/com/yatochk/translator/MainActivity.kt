package com.yatochk.translator

import android.animation.AnimatorInflater
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.input_words.view.*

class MainActivity : AppCompatActivity() {

    private val openAnimator = AnimatorInflater.loadAnimator(this, R.animator.open_translated)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openAnimator.setTarget(input_layout)
        input_layout.go_translate.setOnClickListener {
            openTranslate()
        }
    }

    fun openTranslate() {
        openAnimator.start()
    }
}
